package com.example.devtoolsquartz.quartz.task;

public interface TriggerTask<T extends TriggerTask<T>> {

  /**
   * 获取触发器组
   */
  String getTriggerGroup();

  /**
   * 设置触发器组
   *
   * @param triggerGroup 触发器组
   */
  T setTriggerGroup(String triggerGroup);

  /**
   * 获取触发器名称
   */
  String getTriggerName();

  /**
   * 设置触发器名称
   *
   * @param triggerName 触发器名称
   */
  T setTriggerName(String triggerName);

  /**
   * 获取触发器的优先级
   *
   * @return 返回优先级
   */
  Integer getPriority();

  /**
   * 设置触发器的优先级
   *
   * @param priority 优先级
   */
  T setPriority(Integer priority);

  /**
   * 获取开始执行的时间
   */
  Long getStartAt();

  /**
   * 设置开始执行的时间
   *
   * @param startAt 时间
   */
  T setStartAt(Long startAt);

  /**
   * 获取停止时间
   */
  Long getEndAt();

  /**
   * 设置停止时间
   *
   * @param endAt 停止时间
   */
  T setEndAt(Long endAt);


  /**
   * 获取Calendar名称
   */
  String getCalendarName();

  /**
   * 修改 Calendar 名称
   *
   * @param calendarName Calendar 名称
   */
  T setCalendarName(String calendarName);

  /**
   * 获取失效策略
   */
  Integer getMisfirePolicy();

  /**
   * 设置失效策略
   *
   * @param misfirePolicy 失效策略
   */
  T setMisfirePolicy(Integer misfirePolicy);

}
