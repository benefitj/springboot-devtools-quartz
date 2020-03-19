package com.example.devtoolsquartz.quartz;

import com.example.devtoolsquartz.quartz.task.JobTask;
import com.example.devtoolsquartz.service.QrtzJobTaskService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Date;

/**
 * Quartz全局监听
 */
public final class GlobalJobListener implements JobListener {

  private static final Logger logger = LoggerFactory.getLogger(GlobalJobListener.class);

  private static final String NAME = "globalListener";

  private QrtzJobTaskService service;

  public GlobalJobListener() {
  }

  public QrtzJobTaskService getService() {
    return service;
  }

  @Lazy
  @Autowired
  public void setService(QrtzJobTaskService service) {
    this.service = service;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public void jobToBeExecuted(JobExecutionContext context) {
    // 被执行之前
  }

  @Override
  public void jobExecutionVetoed(JobExecutionContext context) {
    // 触发器优先级排序
  }

  @Override
  public void jobWasExecuted(JobExecutionContext context, JobExecutionException e) {
    try {
      // job执行完毕
      final Date nextFireTime = context.getNextFireTime();
      final JobDetail jd = context.getJobDetail();
      if (nextFireTime == null && jd != null) {
        JobDataMap jdm = jd.getJobDataMap();
        String id = jdm.getString(JobTask.KEY_ID);
        if (StringUtils.isNotBlank(id)) {
          // 删除JobTask
          getService().delete(id, true);
        }
        logger.warn("调度任务执行完毕, 删除调度任务, key[{}]  id[{}] result: {},  job throws: {}",
            jd.getKey() , id , context.getResult() , (e != null ? e.getMessage() : null));
      }
    } catch (Exception ee) {
      logger.warn("throw: {}", ee.getMessage());
    }
  }

}
