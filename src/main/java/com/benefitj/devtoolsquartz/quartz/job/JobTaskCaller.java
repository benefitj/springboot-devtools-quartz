package com.benefitj.devtoolsquartz.quartz.job;

import com.benefitj.devtoolsquartz.quartz.task.QrtzJobTask;
import com.benefitj.spring.ctx.SpringCtxHolder;
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
      String taskId = jobDataMap.getString(QrtzJobTask.KEY_ID);
      String worker = jobDataMap.getString(QrtzJobTask.KEY_WORKER);

      WorkerType workerType = WorkerType.of(jobDataMap.getString(QrtzJobTask.KEY_WORKER_TYPE));
      JobWorker jobWorker = null;
      switch (workerType) {
        case NEW_INSTANCE:
          jobWorker = classForName(worker).newInstance();
          break;
        case SPRING_BEAN_NAME:
          jobWorker = SpringCtxHolder.getBean(worker);
          break;
        case SPRING_BEAN_CLASS:
          jobWorker = SpringCtxHolder.getBean(classForName(worker));
          break;
      }
      if (jobWorker != null) {
        jobWorker.execute(context, detail, taskId);
      } else {
        logger.warn("Not found job worker instance: " + worker);
      }
    } catch (Exception e) {
      logger.error("throws: " + e.getMessage(), e);
      throw new JobExecutionException(e);
    }
  }

  public Class<? extends JobWorker> classForName(String name) throws SchedulerException {
    try {
      return (Class<? extends JobWorker>) Class.forName(name);
    } catch (ClassNotFoundException e) {
      throw new SchedulerException(e.getMessage());
    }
  }

}
