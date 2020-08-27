package com.benefitj.devtoolsquartz.quartz;

import com.alibaba.fastjson.JSON;
import com.benefitj.core.DateFmtter;
import com.benefitj.devtoolsquartz.quartz.job.JobWorker;
import com.benefitj.devtoolsquartz.quartz.task.QrtzJobTask;
import com.benefitj.devtoolsquartz.service.QrtzJobTaskService;
import com.benefitj.spring.ctx.SpringCtxHolder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JobTaskWorker implements JobWorker {

  private static final Logger logger = LoggerFactory.getLogger(JobTaskWorker.class);

  @Override
  public void execute(JobExecutionContext context, JobDetail detail, String taskId) throws JobExecutionException {
    try {
      JobDataMap jobDataMap = detail.getJobDataMap();
      QrtzJobTaskService taskService = getBean(QrtzJobTaskService.class);
      QrtzJobTask task = taskService.get(taskId);
      System.err.println("\n-------------------------------------");
      System.err.println("task: " + JSON.toJSONString(task));
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
