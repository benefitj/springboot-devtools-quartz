package com.example.devtoolsquartz.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalSchedulerListener implements SchedulerListener {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void jobScheduled(Trigger trigger) {
    // job调度
    logger.info("jobScheduled");
  }

  @Override
  public void jobUnscheduled(TriggerKey triggerKey) {
    // job取消调度
    logger.info("jobUnscheduled");
  }

  @Override
  public void triggerFinalized(Trigger trigger) {
    // 触发器结束
    logger.info("triggerFinalized: {}", trigger.getKey());
  }

  @Override
  public void triggerPaused(TriggerKey triggerKey) {
    // 触发器暂停
    logger.info("triggerPaused: {}", triggerKey);
  }

  @Override
  public void triggersPaused(String triggerGroup) {
    // 整个触发器组的触发器暂停
    logger.info("triggersPaused: {}", triggerGroup);
  }

  @Override
  public void triggerResumed(TriggerKey triggerKey) {
    // 恢复触发器
    logger.info("triggerResumed: {}", triggerKey);
  }

  @Override
  public void triggersResumed(String triggerGroup) {
    // 恢复整个触发器组的触发器
    logger.info("triggersResumed: {}", triggerGroup);
  }

  @Override
  public void jobAdded(JobDetail jobDetail) {
    // job被添加
    logger.info("jobAdded: {}", jobDetail.getKey());
  }

  @Override
  public void jobDeleted(JobKey jobKey) {
    // job被删除
    logger.info("jobDeleted: {}", jobKey);
  }

  @Override
  public void jobPaused(JobKey jobKey) {
    // job被暂停
    logger.info("jobPaused: {}", jobKey);
  }

  @Override
  public void jobsPaused(String jobGroup) {
    // job组被暂停
    logger.info("jobsPaused: {}", jobGroup);
  }

  @Override
  public void jobResumed(JobKey jobKey) {
    // job被恢复
    logger.info("jobResumed: {}", jobKey);
  }

  @Override
  public void jobsResumed(String jobGroup) {
    // job组被恢复
    logger.info("jobsResumed: {}", jobGroup);
  }

  @Override
  public void schedulerError(String msg, SchedulerException cause) {
    // 调度错误
    logger.info("schedulerError, msg: {}", msg);
  }

  @Override
  public void schedulerInStandbyMode() {
    // 待机模式
    logger.info("schedulerInStandbyMode: {}", Integer.toHexString(hashCode()));
  }

  @Override
  public void schedulerStarted() {
    logger.info("schedulerStarted, {}", Integer.toHexString(hashCode()));
  }

  @Override
  public void schedulerStarting() {
    logger.info("schedulerStarting, {}", Integer.toHexString(hashCode()));
  }

  @Override
  public void schedulerShutdown() {
    logger.info("schedulerShutdown");
  }

  @Override
  public void schedulerShuttingdown() {
    logger.info("schedulerShuttingdown");
  }

  @Override
  public void schedulingDataCleared() {
    logger.info("schedulingDataCleared");
  }
}
