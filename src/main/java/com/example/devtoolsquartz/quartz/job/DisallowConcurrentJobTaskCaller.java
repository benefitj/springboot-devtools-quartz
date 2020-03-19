package com.example.devtoolsquartz.quartz.job;

import org.quartz.DisallowConcurrentExecution;

/**
 * 不允许并发执行
 */
@DisallowConcurrentExecution
public class DisallowConcurrentJobTaskCaller extends JobTaskCaller {
}
