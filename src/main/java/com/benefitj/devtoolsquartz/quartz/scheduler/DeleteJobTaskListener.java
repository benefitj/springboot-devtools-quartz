package com.benefitj.devtoolsquartz.quartz.scheduler;

import com.benefitj.devtoolsquartz.quartz.job.JobWorker;
import com.benefitj.devtoolsquartz.service.QrtzJobTaskService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DeleteJobTaskListener implements JobListener {

  private static final Logger logger = LoggerFactory.getLogger(DeleteJobTaskListener.class);

  private static final String NAME = "deleteListener";

  private QrtzJobTaskService service;

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
        String id = jdm.getString(JobWorker.KEY_ID);
        if (StringUtils.isNotBlank(id)) {
          // 删除JobTask
          getService().delete(id, true);
        }
        logger.warn("调度任务执行完毕, 删除调度任务, key[{}]  id[{}] result: {},  job throws: {}",
            jd.getKey(), id, context.getResult(), (e != null ? e.getMessage() : null));
      }
    } catch (Exception ee) {
      logger.warn("throw: {}", ee.getMessage());
    }
  }


}
