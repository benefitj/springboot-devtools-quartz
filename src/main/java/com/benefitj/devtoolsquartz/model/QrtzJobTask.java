package com.benefitj.devtoolsquartz.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.benefitj.devtoolsquartz.quartz.job.JobType;
import com.benefitj.devtoolsquartz.quartz.job.WorkerType;
import com.benefitj.devtoolsquartz.quartz.TriggerType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "qrtz_job_task")
public class QrtzJobTask implements Cloneable {

  public static final int TRIGGER_PRIORITY = 50;

  /**
   * 任务id
   */
  @Id
  @Column(name = "id", columnDefinition = "varchar(32) comment '调度任务的ID'", length = 32)
  private String id;
  /**
   * group名称
   */
  @Column(name = "job_group", columnDefinition = "varchar(50) comment '任务分组'", length = 50)
  private String jobGroup;
  /**
   * Job名称
   */
  @Column(name = "job_name", columnDefinition = "varchar(50) comment '任务名称'", length = 50)
  private String jobName;
  /**
   * 任务别名
   */
  @Column(name = "job_alias", columnDefinition = "varchar(50) comment '任务别名'", length = 50)
  private String jobAlias;
  /**
   * 描述
   */
  @Column(name = "description", columnDefinition = "varchar(1024) comment '任务描述'", length = 1024)
  private String description;
  /**
   * 是否异步
   */
  @Column(name = "async", columnDefinition = "tinyint(1) comment '是否异步执行程序' DEFAULT 0", length = 1)
  private Boolean async;
  /**
   * 是否不恢复
   */
  @Column(name = "recovery", columnDefinition = "tinyint(1) comment '是否恢复' DEFAULT 0", length = 1)
  private Boolean recovery;
  /**
   * 执行后是否持久化数据，默认不持久化
   */
  @Column(name = "persistent", columnDefinition = "tinyint(1) comment '执行后是否持久化数据，默认不持久化' DEFAULT 0", length = 1)
  private Boolean persistent;
  /**
   * 是否不允许并发执行，默认并发执行
   */
  @Column(name = "disallow_concurrent", columnDefinition = "tinyint(1) comment '是否不允许并发执行，默认并发执行' DEFAULT 1", length = 1)
  private Boolean disallowConcurrent;
  /**
   * 创建时间
   */
  @JSONField(serialize = false)
  @JsonIgnore
  @Column(name = "create_time", columnDefinition = "datetime comment '创建时间'")
  private Date createTime;
  /**
   * 更新时间
   */
  @JSONField(serialize = false)
  @JsonIgnore
  @Column(name = "update_time", columnDefinition = "datetime comment '更新时间'")
  private Date updateTime;
  /**
   * Job的执行类型，参考: {@link JobType }
   */
  @Column(name = "job_type", columnDefinition = "varchar(50) comment 'Job的执行类型'", length = 50)
  private String jobType;
  /**
   * JobWorker的实现
   */
  @Column(name = "worker", columnDefinition = "varchar(50) comment 'JobWorker的实现类'", length = 50)
  private String worker;
  /**
   * jobWorker的类型，参考：{@link WorkerType}
   */
  @Column(name = "worker_type", columnDefinition = "varchar(50) comment 'jobWorker的类型'", length = 40)
  private String workerType;
  /**
   * Job携带的数据
   */
  @Column(name = "job_data", columnDefinition = "varchar(1024) comment 'Job携带的数据'", length = 1024)
  private String jobData;
  /**
   * 触发器组
   */
  @Column(name = "trigger_group", columnDefinition = "varchar(50) comment '触发器组'", length = 50)
  private String triggerGroup;
  /**
   * 触发器名称
   */
  @Column(name = "trigger_name", columnDefinition = "varchar(50) comment '触发器名称'", length = 50)
  private String triggerName;
  /**
   * 触发器的优先级
   */
  @Column(name = "priority", columnDefinition = "integer comment '触发器的优先级' DEFAULT 50")
  private Integer priority;
  /**
   * 开始执行的时间
   */
  @Column(name = "start_at", columnDefinition = "bigint comment '开始执行的时间'")
  private Long startAt;
  /**
   * 结束执行的时间
   */
  @Column(name = "end_at", columnDefinition = "bigint comment '结束执行的时间'")
  private Long endAt;
  /**
   * Calendar
   */
  @Column(name = "calendar_name", columnDefinition = "varchar(50) comment 'Calendar Name'", length = 50)
  private String calendarName;
  /**
   * 失效后的策略
   */
  @Column(name = "misfire_policy", columnDefinition = "integer comment '失效后的策略'")
  private Integer misfirePolicy;
  /**
   * 触发器类型: {@link TriggerType#SIMPLE}, {@link TriggerType#CRON}
   */
  @Column(name = "trigger_type", columnDefinition = "varchar(30) comment 'Cron表达式'", length = 30)
  private String triggerType;
  /**
   * 每次执行的间隔
   */
  @Column(name = "simple_interval", columnDefinition = "bigint comment '每次执行的间隔'")
  private Long simpleInterval;
  /**
   * 重复次数
   */
  @Column(name = "simple_repeat_count", columnDefinition = "integer comment '重复次数'")
  private Integer simpleRepeatCount;
  /**
   * Cron表达式
   */
  @Column(name = "cron_expression", columnDefinition = "varchar(50) comment 'Cron表达式'", length = 50)
  private String cronExpression;
  /**
   * 可用状态
   */
  @Column(name = "active", columnDefinition = "tinyint(1) NOT NULL DEFAULT 1 comment '可用状态'", length = 1)
  private Boolean active;

  public QrtzJobTask() {
  }

  @JSONField(serialize = false)
  @JsonIgnore
  protected QrtzJobTask self() {
    return (QrtzJobTask) this;
  }

  public String getId() {
    return id;
  }

  public QrtzJobTask setId(String id) {
    this.id = id;
    return self();
  }

  public String getJobName() {
    return jobName;
  }

  public QrtzJobTask setJobName(String jobName) {
    this.jobName = jobName;
    return self();
  }

  public String getJobGroup() {
    return jobGroup;
  }

  public QrtzJobTask setJobGroup(String jobGroup) {
    this.jobGroup = jobGroup;
    return self();
  }

  public String getJobAlias() {
    return jobAlias;
  }

  public QrtzJobTask setJobAlias(String jobAlias) {
    this.jobAlias = jobAlias;
    return self();
  }

  public String getDescription() {
    return description;
  }

  public QrtzJobTask setDescription(String description) {
    this.description = description;
    return self();
  }

  public Boolean getAsync() {
    return async;
  }

  public QrtzJobTask setAsync(Boolean async) {
    this.async = async;
    return self();
  }

  public Boolean getRecovery() {
    return recovery;
  }

  public QrtzJobTask setRecovery(Boolean recovery) {
    this.recovery = recovery;
    return self();
  }

  public Boolean getPersistent() {
    return persistent;
  }

  public QrtzJobTask setPersistent(Boolean persistAfter) {
    this.persistent = persistAfter;
    return self();
  }

  public Boolean getDisallowConcurrent() {
    return disallowConcurrent;
  }

  public QrtzJobTask setDisallowConcurrent(Boolean concurrent) {
    this.disallowConcurrent = concurrent;
    return self();
  }

  public Date getCreateTime() {
    return createTime;
  }

  public QrtzJobTask setCreateTime(Date createTime) {
    this.createTime = createTime;
    return self();
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public QrtzJobTask setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
    return self();
  }

  public String getJobType() {
    return jobType;
  }

  public void setJobType(String jobType) {
    this.jobType = jobType;
  }

  public String getJobData() {
    return jobData;
  }

  public QrtzJobTask setJobData(String jobClass) {
    this.jobData = jobClass;
    return self();
  }

  public String getWorker() {
    return worker;
  }

  public QrtzJobTask setWorker(String jobMethod) {
    this.worker = jobMethod;
    return self();
  }

  public String getWorkerType() {
    return workerType;
  }

  public QrtzJobTask setWorkerType(String jobData) {
    this.workerType = jobData;
    return self();
  }

  public Boolean getActive() {
    return active;
  }

  public QrtzJobTask setActive(Boolean active) {
    this.active = active;
    return self();
  }


  public String getTriggerGroup() {
    return triggerGroup;
  }

  public QrtzJobTask setTriggerGroup(String triggerGroup) {
    this.triggerGroup = triggerGroup;
    return self();
  }

  public String getTriggerName() {
    return triggerName;
  }

  public QrtzJobTask setTriggerName(String triggerName) {
    this.triggerName = triggerName;
    return self();
  }

  public Integer getPriority() {
    return priority;
  }

  public QrtzJobTask setPriority(Integer priority) {
    this.priority = priority;
    return self();
  }

  public Long getStartAt() {
    return startAt;
  }

  public QrtzJobTask setStartAt(Long startAt) {
    this.startAt = startAt;
    return self();
  }

  public Long getEndAt() {
    return endAt;
  }

  public QrtzJobTask setEndAt(Long endAt) {
    this.endAt = endAt;
    return self();
  }

  public String getCalendarName() {
    return calendarName;
  }

  public QrtzJobTask setCalendarName(String calendarName) {
    this.calendarName = calendarName;
    return self();
  }

  public Integer getMisfirePolicy() {
    return misfirePolicy;
  }

  public QrtzJobTask setMisfirePolicy(Integer misfirePolicy) {
    this.misfirePolicy = misfirePolicy;
    return self();
  }

  public String getTriggerType() {
    return triggerType;
  }

  public QrtzJobTask setTriggerType(String taskType) {
    this.triggerType = taskType;
    return self();
  }

  public Long getSimpleInterval() {
    return simpleInterval;
  }

  public QrtzJobTask setSimpleInterval(Long simpleInterval) {
    this.simpleInterval = simpleInterval;
    return self();
  }

  public Integer getSimpleRepeatCount() {
    return simpleRepeatCount;
  }

  public QrtzJobTask setSimpleRepeatCount(Integer simpleRepeatCount) {
    this.simpleRepeatCount = simpleRepeatCount;
    return self();
  }

  public String getCronExpression() {
    return cronExpression;
  }

  public QrtzJobTask setCronExpression(String cronExpression) {
    this.cronExpression = cronExpression;
    return self();
  }


  @Override
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException ex) {
      throw new IncompatibleClassChangeError("Not Cloneable.");
    }
  }
}
