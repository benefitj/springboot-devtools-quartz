package com.example.devtoolsquartz.quartz.task;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "qrtz_job_task")
public class QrtzJobTask extends JobTask<QrtzJobTask> implements TriggerTask<QrtzJobTask> {

  public static final int TRIGGER_PRIORITY = 100;

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
  @Column(name = "priority", columnDefinition = "integer comment '触发器的优先级'")
  private Integer priority = TRIGGER_PRIORITY;
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
  @Column(name = "per_interval", columnDefinition = "bigint comment '每次执行的间隔'")
  private Long interval = null;
  /**
   * 重复次数
   */
  @Column(name = "repeat_count", columnDefinition = "integer comment '重复次数'")
  private Integer repeatCount = null;
  /**
   * Cron表达式
   */
  @Column(name = "expression", columnDefinition = "varchar(50) comment 'Cron表达式'", length = 50)
  private String expression;

  @JSONField(serialize = false)
  @JsonIgnore
  @Override
  protected QrtzJobTask self() {
    return this;
  }

  @Override
  public String getTriggerGroup() {
    return triggerGroup;
  }

  @Override
  public QrtzJobTask setTriggerGroup(String triggerGroup) {
    this.triggerGroup = triggerGroup;
    return self();
  }

  @Override
  public String getTriggerName() {
    return triggerName;
  }

  @Override
  public QrtzJobTask setTriggerName(String triggerName) {
    this.triggerName = triggerName;
    return self();
  }

  @Override
  public Integer getPriority() {
    return priority;
  }

  @Override
  public QrtzJobTask setPriority(Integer priority) {
    this.priority = (priority != null) ? priority : TRIGGER_PRIORITY;
    return self();
  }

  @Override
  public Long getStartAt() {
    return startAt;
  }

  @Override
  public QrtzJobTask setStartAt(Long startAt) {
    this.startAt = startAt;
    return self();
  }

  @Override
  public Long getEndAt() {
    return endAt;
  }

  @Override
  public QrtzJobTask setEndAt(Long endAt) {
    this.endAt = endAt;
    return self();
  }

  @Override
  public String getCalendarName() {
    return calendarName;
  }

  @Override
  public QrtzJobTask setCalendarName(String calendarName) {
    this.calendarName = calendarName;
    return self();
  }

  @Override
  public Integer getMisfirePolicy() {
    return misfirePolicy;
  }

  @Override
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

  public Long getInterval() {
    return interval;
  }

  public QrtzJobTask setInterval(Long interval) {
    this.interval = (interval != null) ? Math.max(0, interval) : 0;
    return self();
  }

  public Integer getRepeatCount() {
    return repeatCount;
  }

  public QrtzJobTask setRepeatCount(Integer repeatCount) {
    this.repeatCount = (repeatCount != null) ? Math.max(0, repeatCount) : 0;
    return self();
  }

  public String getExpression() {
    return expression;
  }

  public QrtzJobTask setExpression(String expression) {
    this.expression = expression;
    return self();
  }
}
