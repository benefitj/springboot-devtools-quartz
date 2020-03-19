package com.example.devtoolsquartz.quartz.job;

import com.alibaba.fastjson.JSON;
import com.example.devtoolsquartz.quartz.task.JobTask;
import com.example.devtoolsquartz.quartz.task.QrtzJobTask;
import com.example.devtoolsquartz.service.QrtzJobTaskService;
import com.hsrg.core.DateFmtter;
import com.hsrg.spring.ctx.SpringCtxHolder;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobTaskCaller implements Job {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  public JobTaskCaller() {
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    try {
      JobDetail detail = context.getJobDetail();
      JobDataMap jobDataMap = detail.getJobDataMap();
      QrtzJobTaskService taskService = getBean(QrtzJobTaskService.class);
      String taskId = jobDataMap.getString(JobTask.KEY_ID);
      QrtzJobTask task = taskService.get(taskId);
      System.err.println("\n-------------------------------------");
      System.err.println("taskId: " + JSON.toJSONString(task));
      System.err.println("now: " + DateFmtter.fmtNowS());
      System.err.println("key: " + detail.getKey());
      System.err.println("jobClass: " + detail.getJobClass());
      System.err.println("jobDataMap: " + JSON.toJSONString(jobDataMap));
      System.err.println("description: " + detail.getDescription());
      System.err.println("refireCount: " + context.getRefireCount());
      System.err.println("nextFireTime: " + fmtS(context.getNextFireTime()));
      System.err.println("thread: " + Thread.currentThread().getName());
      System.err.println("-------------------------------------\n");
    } catch (Exception e) {
      logger.error("throws: " + e.getMessage(), e);
      throw new JobExecutionException(e);
    }
  }

  public String fmtS(Object date) {
    return date != null ? DateFmtter.fmtS(date) : null;
  }

  /**
   * 获取Spring的Bean实例
   *
   * @param requiredType 类型
   * @return 返回查询到的实例
   */
  public <T> T getBean(Class<? extends T> requiredType) {
    return SpringCtxHolder.getBean(requiredType);
  }

  /**
   * 获取Spring的Bean实例
   *
   * @param beanName bean名称
   * @return 返回查询到的实例
   */
  public <T> T getBean(String beanName) {
    return SpringCtxHolder.getBean(beanName);
  }

}
