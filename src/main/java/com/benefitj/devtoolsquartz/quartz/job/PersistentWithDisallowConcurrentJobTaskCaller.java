package com.benefitj.devtoolsquartz.quartz.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;

/**
 * 执行后持久化数据，并且不允许并发执行
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PersistentWithDisallowConcurrentJobTaskCaller extends JobTaskCaller {
}
