package com.benefitj.devtoolsquartz.quartz;

import com.benefitj.core.EventLoop;
import com.benefitj.core.ReflectUtils;
import com.benefitj.devtoolsquartz.service.QrtzJobTaskService;
import com.benefitj.spring.applicationevent.IApplicationReadyEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

@ConditionalOnBean(QrtzJobTaskService.class)
@Component
public class QuartzStartEvent implements IApplicationReadyEventListener {

  @Autowired
  private SchedulerFactoryBean schedulerFactoryBean;
  @Autowired
  private QrtzJobTaskService service;

  @Override
  public void onApplicationReadyEvent(ApplicationReadyEvent event) {
    Class<?> type = schedulerFactoryBean.getClass();
    Field field = ReflectUtils.getField(type, "startupDelay");
    if (field != null) {
      Integer value = ReflectUtils.getFieldValue(field, schedulerFactoryBean);
      // 调度任务
      EventLoop.multi().schedule(() ->
          service.scheduleJobTasks(service.getAll()), value + 3000, TimeUnit.MILLISECONDS);
    }

  }

}

