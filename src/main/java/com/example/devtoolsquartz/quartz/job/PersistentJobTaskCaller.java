package com.example.devtoolsquartz.quartz.job;

import org.quartz.PersistJobDataAfterExecution;

/**
 * 执行后持久化数据
 */
@PersistJobDataAfterExecution
public class PersistentJobTaskCaller extends JobTaskCaller {
}
