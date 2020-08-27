package com.benefitj.devtoolsquartz.quartz.job;

import org.quartz.Job;

/**
 * JOB类型
 */
public enum JobType {
  /**
   * 默认类型
   */
  DEFAULT(JobTaskCaller.class, false, false),
  /**
   * 执行后持久化数据
   */
  PERSISTENT(PersistentJobTaskCaller.class, true, false),
  /**
   * 不并发执行
   */
  DISALLOW_CONCURRENT(DisallowConcurrentJobTaskCaller.class, false, false),
  /**
   * 执行后持久化数据，并且不允许并发执行
   */
  PERSISTENT_WITH_DISALLOW_CONCURRENT(PersistentWithDisallowConcurrentJobTaskCaller.class, true, true);

  private final Class<? extends Job> jobClass;

  /**
   * 执行后是否持久化数据，默认不持久化
   */
  private final boolean persistent;
  /**
   * 是否不允许并发执行，默认不并发执行
   */
  private final boolean disallowConcurrent;

  JobType(Class<? extends Job> jobClass, boolean persistent, boolean disallowConcurrent) {
    this.jobClass = jobClass;
    this.persistent = persistent;
    this.disallowConcurrent = disallowConcurrent;
  }

  /**
   * 根据名称获取 JobType
   *
   * @param name 类型
   * @return 返回 JobType，如果没有匹配的，返回默认类型
   */
  public static JobType of(String name) {
    if (name != null && !name.isEmpty()) {
      for (JobType jt : values()) {
        if (jt.name().equalsIgnoreCase(name)) {
          return jt;
        }
      }
    }
    return DEFAULT;
  }

  /**
   * 获取 JobClass
   *
   * @param type 类型
   * @return 返回 JobClass
   */
  public static Class<? extends Job> ofJobClass(String type) {
    return of(type).getJobClass();
  }

  public boolean isPersistent() {
    return persistent;
  }

  public boolean isDisallowConcurrent() {
    return disallowConcurrent;
  }

  public Class<? extends Job> getJobClass() {
    return jobClass;
  }

}
