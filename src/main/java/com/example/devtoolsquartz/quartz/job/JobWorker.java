package com.example.devtoolsquartz.quartz.job;

import com.example.devtoolsquartz.quartz.task.QrtzJobTask;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

public interface JobWorker {

  /**
   * 执行方法
   *
   * @param context   上下文
   * @param jobDetail detail
   * @param task      任务ID
   */
  void execute(JobExecutionContext context, JobDetail jobDetail, QrtzJobTask task);

}
