package com.benefitj.devtoolsquartz.quartz.job;

public enum WorkerType {
  /**
   * 新建对象
   */
  NEW_INSTANCE,
  /**
   * spring 组件名
   */
  SPRING_BEAN_NAME,
  /**
   * spring 组件类
   */
  SPRING_BEAN_CLASS;

  /**
   * 获取 WorkerType
   */
  public static WorkerType of(String type) {
    if ((type != null && !type.isEmpty())) {
      for (WorkerType wt : values()) {
        if (wt.name().equalsIgnoreCase(type)) {
          return wt;
        }
      }
    }
    return null;
  }
}
