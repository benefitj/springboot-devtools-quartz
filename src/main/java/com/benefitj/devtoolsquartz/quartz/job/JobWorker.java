package com.benefitj.devtoolsquartz.quartz.job;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public interface JobWorker {

  String KEY_ID = "id";
  String KEY_JOB_DATA = "jobData";
  String KEY_WORKER = "worker";
  String KEY_WORKER_TYPE = "workerType";

  /**
   * 执行方法
   *
   * @param context   上下文
   * @param jobDetail detail
   * @param taskId    任务ID
   */
  void execute(JobExecutionContext context, JobDetail jobDetail, String taskId) throws JobExecutionException;

}
