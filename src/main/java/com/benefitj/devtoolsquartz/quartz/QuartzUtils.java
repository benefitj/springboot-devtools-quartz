package com.benefitj.devtoolsquartz.quartz;

import com.benefitj.devtoolsquartz.quartz.job.JobType;
import com.benefitj.devtoolsquartz.quartz.job.JobWorker;
import com.benefitj.devtoolsquartz.quartz.job.WorkerType;
import com.benefitj.devtoolsquartz.model.QrtzJobTask;
import com.benefitj.spring.ctx.SpringCtxHolder;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;

import java.util.Date;

public class QuartzUtils {

  /**
   * 检查 task 的 worker
   *
   * @param task
   */
  public static void checkWorker(QrtzJobTask task) {
    WorkerType workerType = WorkerType.of(task.getWorkerType());
    if (workerType == null) {
      throw new IllegalStateException("WorkerType错误!");
    }

    if (StringUtils.isBlank(task.getWorker())) {
      throw new IllegalStateException("worker不能为空");
    }

    if (workerType != WorkerType.SPRING_BEAN_NAME) {
      try {
        Class.forName(task.getWorker());
      } catch (ClassNotFoundException e) {
        throw new IllegalStateException("请指定正确的worker");
      }
    } else {
      boolean containsBean = SpringCtxHolder.getCtx().containsBean(task.getWorker());
      if (!containsBean) {
        throw new IllegalStateException("worker不存在");
      }
    }
  }

  /**
   * 构建 JobDetail
   *
   * @param task 任务
   * @return 返回 JobBuilder
   */
  public static JobBuilder job(QrtzJobTask task) {
    checkWorker(task);
    // 创建 JobDetails
    JobBuilder jb = JobBuilder.newJob();
    jb.ofType(JobType.ofJobClass(task.getJobType()));
    jb.withIdentity(task.getJobName(), task.getJobGroup());
    jb.withDescription(task.getDescription());
    jb.requestRecovery(task.getRecovery());
    // 不持久化
    jb.storeDurably(false);
    jb.usingJobData(JobWorker.KEY_ID, task.getId());
    jb.usingJobData(JobWorker.KEY_JOB_DATA, task.getJobData());
    jb.usingJobData(JobWorker.KEY_WORKER, task.getWorker());
    jb.usingJobData(JobWorker.KEY_WORKER_TYPE, task.getWorkerType());
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
    ssb.withIntervalInMilliseconds(task.getSimpleInterval());
    ssb.withRepeatCount(task.getSimpleRepeatCount());
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
    CronScheduleBuilder csb = CronScheduleBuilder.cronSchedule(task.getCronExpression());
    TriggerType.CronPolicy.schedulePolicy(csb, task.getMisfirePolicy());
    tb.withSchedule(csb);
    return tb;
  }

  /**
   * 触发器的Key
   */
  public static JobKey jobKey(QrtzJobTask task) {
    return new JobKey(task.getJobName(), task.getJobGroup());
  }

  /**
   * 触发器的Key
   */
  public static TriggerKey triggerKey(QrtzJobTask task) {
    return new TriggerKey(task.getTriggerName(), task.getTriggerGroup());
  }

  public static void scheduleJob(Scheduler scheduler, QrtzJobTask task) throws IllegalStateException {
    try {
      // 创建 JobDetails
      if (scheduler.checkExists(jobKey(task))) {
        scheduler.resumeJob(jobKey(task));
        return;
      }

      JobDetail jd = job(task).build();
      Trigger trigger = scheduler.getTrigger(triggerKey(task));
      if (trigger == null) {
        trigger = trigger(task).forJob(jd).build();
      }

      // 添加调度器
      scheduler.scheduleJob(jd, trigger);

      // 是否暂停job
      if (!Boolean.TRUE.equals(task.getActive())) {
        scheduler.pauseJob(jd.getKey());
      }

      // 开启调度器
      if (!scheduler.isStarted()) {
        scheduler.start();
      }
    } catch (SchedulerException e) {
      throw new IllegalStateException(e);
    }
  }
}
