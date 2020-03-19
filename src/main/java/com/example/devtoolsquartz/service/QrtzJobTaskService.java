package com.example.devtoolsquartz.service;

import com.example.devtoolsquartz.core.IdUtils;
import com.example.devtoolsquartz.core.Tools;
import com.example.devtoolsquartz.dao.QrtzJobTaskMapper;
import com.example.devtoolsquartz.quartz.QuartzUtils;
import com.example.devtoolsquartz.quartz.job.JobType;
import com.example.devtoolsquartz.quartz.task.QrtzJobTask;
import com.example.devtoolsquartz.quartz.task.TriggerType;
import com.hsrg.spring.BeanHelper;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class QrtzJobTaskService extends BaseService<QrtzJobTask, QrtzJobTaskMapper> {

  private static final int MAX_TRY = 1000;
  private static final int LENGTH = 6;

  @Autowired
  private Scheduler scheduler;

  @Autowired
  private QrtzJobTaskMapper mapper;

  public QrtzJobTaskService() {
  }

  @Override
  protected QrtzJobTaskMapper getMapper() {
    return mapper;
  }

  /**
   * 获取调度器
   */
  public Scheduler getScheduler() {
    return scheduler;
  }

  public void useScheduler(Consumer<Scheduler> consumer) {
    try {
      consumer.accept(getScheduler());
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  public <R> R useScheduler(Function<Scheduler, R> func) {
    try {
      return func.accept(getScheduler());
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }


  /**
   * 获取 job name
   */
  private String nextJobName() {
    String name = null;
    for (int i = 0; i < MAX_TRY; i++) {
      name = IdUtils.nextId(LENGTH);
      if (countJobName(name) <= 0) {
        break;
      }
    }
    if (StringUtils.isBlank(name)) {
      throw new IllegalStateException("无法获取jobName");
    }
    return name;
  }


  /**
   * 统计 job name 出现的次数
   *
   * @param jobName job name
   * @return 返回出现的次数
   */
  public int countJobName(String jobName) {
    return getMapper().countJobName(jobName);
  }

//  /**
//   * 获取 trigger name
//   */
//  private String nextTriggerName() {
//    String triggerName = null;
//    for (int i = 0; i < MAX_TRY; i++) {
//      triggerName = IdUtils.nextId(LENGTH);
//      if (countTriggerName(triggerName) <= 0) {
//        break;
//      }
//    }
//    if (StringUtils.isBlank(triggerName)) {
//      throw new IllegalStateException("无法获取triggerName");
//    }
//    return triggerName;
//  }
//
//  /**
//   * 统计 trigger name 出现的次数
//   *
//   * @param triggerName trigger name
//   * @return 返回出现的次数
//   */
//  public int countTriggerName(String triggerName) {
//    return getMapper().countTriggerName(triggerName);
//  }

  /**
   * 通过ID获取Cron的调度任务
   *
   * @param id 调度任务的ID
   * @return 返回调度任务
   */
  public QrtzJobTask get(String id) {
    return getByPK(id);
  }

  /**
   * 创建Cron调度任务
   *
   * @param task 调度任务
   */
  public QrtzJobTask create(QrtzJobTask task) {
    try {
      if (StringUtils.isBlank(task.getDescription())) {
        throw new IllegalStateException("请给此调度任务添加一个描述");
      }

      // 任务ID
      Tools.checkBlank(task.getId(), () -> task.setId(IdUtils.uuid()));
      // 创建时间
      Tools.checkNull(task.getCreateTime(), () -> task.setCreateTime(new Date()));

      setupJobTask(task);

      QuartzUtils.scheduleJob(getScheduler(), task);
      // 保存
      super.insert(task);
      return task;
    } catch (SchedulerException e) {
      throw new IllegalStateException(e);
    }
  }

  private void setupJobTask(QrtzJobTask task) {
    TriggerType type = TriggerType.of(task.getTriggerType());
    if (type == null) {
      throw new IllegalStateException("请指定正确的触发器类型");
    }

    String jobName = nextJobName();
    // 触发器组名称
    task.setTriggerGroup(type.name());
    // 创建随机的触发器名称
    task.setTriggerName("trigger-" + jobName);
    // Job组名称
    task.setJobGroup(type.name());
    // 创建随机的 JobName
    task.setJobName(jobName);

    // 开始时间
    long now = System.currentTimeMillis();
    Tools.checkNull(task.getStartAt(), () -> task.setStartAt(now));

    // 调度的时间不能比当前时间更早
    if (task.getStartAt() < now) {
      task.setStartAt(now);
    }

    if (type == TriggerType.CRON) {
      try {
        // 验证表达式
        CronExpression.validateExpression(task.getExpression());
      } catch (ParseException e) {
        throw new IllegalStateException("[" + task.getExpression() + "]表达式错误: " + e.getMessage());
      }
      if (task.getMisfirePolicy() == null) {
        // 默认什么都不做
        task.setMisfirePolicy(TriggerType.CronPolicy.DO_NOTHING.getPolicy());
      }
    } else {
      // 验证Simple的值
      // 执行次数
      Tools.checkNull(task.getRepeatCount(), () -> task.setRepeatCount(0));
      // 间隔时间
      Tools.checkNull(task.getInterval(), () -> task.setInterval(0L));

      if (task.getMisfirePolicy() == null) {
        // 默认什么都不做
        task.setMisfirePolicy(TriggerType.SimplePolicy.SMART_POLICY.getPolicy());
      }
    }

    if (task.getEndAt() != null) {
      // 至少开始后的5秒再结束
      task.setEndAt(Math.max(task.getStartAt() + 5_000, task.getEndAt()));
    }

    // job类型
    JobType jobType = JobType.of(task.getJobType());
    // 任务类型
    task.setJobType(jobType.name());
    // 是否持久化
    task.setPersistent(jobType.isPersistent());
    // 不允许并发执行
    task.setDisallowConcurrent(jobType.isDisallowConcurrent());
  }

  /**
   * 更新调度任务
   *
   * @param task 调度任务
   * @return 返回
   */
  @Transactional(rollbackFor = Exception.class)
  public QrtzJobTask update(QrtzJobTask task) {
    QrtzJobTask existTask = get(task.getId());
    if (existTask != null) {
      // 触发器组和名称、Job组合名称 都为自动生成，触发器类型必须指定

      TriggerType type = TriggerType.of(task.getTriggerType());
      if (type == null) {
        throw new IllegalStateException("请指定正确的触发器类型");
      }

      if (!type.name().equalsIgnoreCase(existTask.getTriggerType())) {
        throw new IllegalStateException("无法修改触发器类型");
      }

      QrtzJobTask copy = BeanHelper.copy(existTask, QrtzJobTask.class);
      BeanHelper.copy(task, existTask);
      existTask.setCreateTime(copy.getCreateTime());
      existTask.setUpdateTime(new Date());
      existTask.setActive(copy.getActive());

      // 重置调度
      setupJobTask(existTask);

      try {
        // 停止任务和触发器
        return useScheduler(s -> {
          // 删除存在的job
          s.pauseTrigger(QuartzUtils.triggerKey(copy));
          s.deleteJob(QuartzUtils.jobKey(copy));
          // 重新调度
          QuartzUtils.scheduleJob(s, existTask);
          updateByPK(existTask);
          return existTask;
        });
      } catch (Exception e) {
        useScheduler((Consumer<Scheduler>) s -> s.resumeTrigger(QuartzUtils.triggerKey(existTask)));
        throw e;
      }
    }
    return existTask;
  }

  /**
   * 删除调度的任务
   *
   * @param id    任务的ID
   * @param force 是否强制删除(暂不起作用)
   * @return 返回删除的条数(0或1)
   */
  public int delete(String id, boolean force) {
    QrtzJobTask task = get(id);
    if (task != null) {
      return useScheduler(sched -> {
        sched.pauseTrigger(QuartzUtils.triggerKey(task));
        sched.deleteJob(QuartzUtils.jobKey(task));
        return deleteByPK(id);
      });
    }
    return 0;
  }

  /**
   * 改变 Job 的状态，暂停或执行
   *
   * @param id     任务ID
   * @param status 状态
   * @return 返回状态是否改变
   */
  public boolean changeActive(String id, Boolean status) {
    final QrtzJobTask task = get(id);
    if (task != null) {
      return useScheduler(scheduler -> {
        if (Boolean.TRUE.equals(status)) {
          if (Boolean.FALSE.equals(task.getActive())) {// 之前被停止了
            scheduler.resumeJob(QuartzUtils.jobKey(task));
          }
        } else {
          if (Boolean.TRUE.equals(task.getActive())) {// 正在执行中
            scheduler.pauseJob(QuartzUtils.jobKey(task));
          }
        }
        task.setActive(Boolean.TRUE.equals(status));
        task.setUpdateTime(new Date());
        return getMapper().updateByPrimaryKeySelective(task) > 0;
      });
    }
    return false;
  }

  @Override
  public List<QrtzJobTask> getList(QrtzJobTask condition, Date startTime, Date endTime, boolean multiLevel) {
    return getMapper().selectList(condition, startTime, endTime, multiLevel);
  }

  public List<QrtzJobTask> getAll() {
    return getMapper().selectAll();
  }


  @FunctionalInterface
  public interface Consumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t) throws Exception;
  }

  @FunctionalInterface
  public interface Function<T, R> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    R accept(T t) throws Exception;
  }

}
