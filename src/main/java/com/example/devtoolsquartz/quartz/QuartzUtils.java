package com.example.devtoolsquartz.quartz;

import com.example.devtoolsquartz.quartz.job.JobType;
import com.example.devtoolsquartz.quartz.task.JobTask;
import com.example.devtoolsquartz.quartz.task.QrtzJobTask;
import com.example.devtoolsquartz.quartz.task.TriggerTask;
import com.example.devtoolsquartz.quartz.task.TriggerType;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;

import java.util.Date;

public class QuartzUtils {

  /**
   * 构建 JobDetail
   *
   * @param task 任务
   * @return 返回 JobBuilder
   */
  public static JobBuilder job(JobTask<?> task) {
    // 创建 JobDetails
    JobBuilder jb = JobBuilder.newJob();
    jb.ofType(JobType.ofJobClass(task.getJobType()));
    jb.withIdentity(task.getJobName(), task.getJobGroup());
    jb.withDescription(task.getDescription());
    jb.requestRecovery(task.getRecovery());
    // 不持久化
    jb.storeDurably(false);
    jb.usingJobData(JobTask.KEY_ID, task.getId());
    jb.usingJobData(JobTask.KEY_JOB_DATA, task.getJobData());
    return jb;
  }

  /**
   * 构建触发器
   *
   * @param task 任务
   * @return 返回TriggerBuilder
   */
  public static TriggerBuilder<Trigger> trigger(QrtzJobTask task) {
    TriggerType triggerType = TriggerType.valueOf(task.getTriggerType());
    if (triggerType == null) {
      throw new IllegalStateException("触发器类型错误");
    }
    return triggerType == TriggerType.CRON ? cronTrigger(task) : simpleTrigger(task);
  }

  /**
   * 构建触发器
   *
   * @param task 任务
   * @return 返回TriggerBuilder
   */
  public static TriggerBuilder<Trigger> trigger(QrtzJobTask task, TriggerBuilder<Trigger> tb) {
    tb.withIdentity(task.getTriggerName(), task.getTriggerGroup());
    // 触发器优先级
    tb.withPriority(task.getPriority());
    // 开始执行的时间
    if (task.getStartAt() != null) {
      tb.startAt(new Date(task.getStartAt()));
    } else {
      tb.startNow();
    }
    // 结束时间
    if (task.getEndAt() != null) {
      tb.endAt(new Date(task.getEndAt()));
    }
    if (StringUtils.isNotBlank(task.getCalendarName())) {
      tb.modifiedByCalendar(task.getCalendarName());
    }
    return tb;
  }

  /**
   * 构建触发器
   *
   * @param task 任务
   * @return 返回TriggerBuilder
   */
  public static TriggerBuilder<Trigger> simpleTrigger(QrtzJobTask task) {
    TriggerBuilder<Trigger> tb = TriggerBuilder.newTrigger();
    trigger(task, tb);
    // 调度器
    SimpleScheduleBuilder ssb = SimpleScheduleBuilder.simpleSchedule();
    TriggerType.SimplePolicy.schedulePolicy(ssb, task.getMisfirePolicy());
    ssb.withIntervalInMilliseconds(task.getInterval());
    ssb.withRepeatCount(task.getRepeatCount());
    tb.withSchedule(ssb);
    return tb;
  }

  /**
   * 构建触发器
   *
   * @param task 任务
   * @return 返回TriggerBuilder
   */
  public static TriggerBuilder<Trigger> cronTrigger(QrtzJobTask task) {
    TriggerBuilder<Trigger> tb = TriggerBuilder.newTrigger();
    trigger(task, tb);
    // 调度器
    CronScheduleBuilder csb = CronScheduleBuilder.cronSchedule(task.getExpression());
    TriggerType.CronPolicy.schedulePolicy(csb, task.getMisfirePolicy());
    tb.withSchedule(csb);
    return tb;
  }

  /**
   * 触发器的Key
   */
  public static JobKey jobKey(JobTask task) {
    return new JobKey(task.getJobName(), task.getJobGroup());
  }

  /**
   * 触发器的Key
   */
  public static TriggerKey triggerKey(TriggerTask<?> task) {
    return new TriggerKey(task.getTriggerName(), task.getTriggerGroup());
  }

  public static void scheduleJob(Scheduler scheduler, QrtzJobTask task) throws SchedulerException {
    // 创建 JobDetails
    JobDetail jd = QuartzUtils.job(task).build();
    Trigger trigger = QuartzUtils.trigger(task)
        .forJob(jd)
        .build();
    // 添加调度器
    scheduler.scheduleJob(jd, trigger);

    // 开启调度器
    if (!scheduler.isStarted()) {
      scheduler.start();
    }
  }
}
