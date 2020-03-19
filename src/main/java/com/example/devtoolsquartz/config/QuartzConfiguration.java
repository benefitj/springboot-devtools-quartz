package com.example.devtoolsquartz.config;

import com.example.devtoolsquartz.quartz.DefaultSchedulerFactory;
import com.example.devtoolsquartz.quartz.GlobalJobListener;
import com.example.devtoolsquartz.quartz.GlobalSchedulerListener;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.util.Map;
import java.util.Properties;

@Lazy
@PropertySource({"classpath:/quartz.properties"})
@ConditionalOnProperty(name = "quartz.enabled")
@Configuration
public class QuartzConfiguration {

  @Bean
  public GlobalJobListener globalJobListener() {
    return new GlobalJobListener();
  }

  @Bean
  public GlobalSchedulerListener globalSchedulerListener() {
    return new GlobalSchedulerListener();
  }

  @Bean
  public DefaultSchedulerFactory schedulerFactory(QuartzProperties quartzProperties,
                                                  ApplicationContext context) {
    try {
      String prefix = "spring.quartz.properties.";
      final Properties properties = new Properties();
      quartzProperties.getProperties().forEach((key, value) ->
          properties.put(key.replaceFirst(prefix, ""), value));
      StdSchedulerFactory factory = new StdSchedulerFactory(properties);
      return new DefaultSchedulerFactory(context, factory);
    } catch (SchedulerException e) {
      throw new BeanInstantiationException(DefaultSchedulerFactory.class, e.getMessage());
    }
  }

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean(QuartzProperties properties,
                                                   ObjectProvider<SchedulerFactoryBeanCustomizer> customizers,
                                                   ObjectProvider<JobDetail> jobDetails,
                                                   Map<String, Calendar> calendars,
                                                   ObjectProvider<Trigger> triggers,
                                                   ApplicationContext applicationContext,
                                                   SchedulerFactory schedulerFactory) {
    SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
    schedulerFactoryBean.setSchedulerFactory(schedulerFactory);
    SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
    jobFactory.setApplicationContext(applicationContext);
    schedulerFactoryBean.setJobFactory(jobFactory);
    if (properties.getSchedulerName() != null) {
      schedulerFactoryBean.setSchedulerName(properties.getSchedulerName());
    }
    schedulerFactoryBean.setAutoStartup(properties.isAutoStartup());
    schedulerFactoryBean.setStartupDelay((int) properties.getStartupDelay().getSeconds());
    schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(properties.isWaitForJobsToCompleteOnShutdown());
    schedulerFactoryBean.setOverwriteExistingJobs(properties.isOverwriteExistingJobs());
    if (!properties.getProperties().isEmpty()) {
      schedulerFactoryBean.setQuartzProperties(asProperties(properties.getProperties()));
    }
    schedulerFactoryBean.setJobDetails(jobDetails.orderedStream().toArray(JobDetail[]::new));
    schedulerFactoryBean.setCalendars(calendars);
    schedulerFactoryBean.setTriggers(triggers.orderedStream().toArray(Trigger[]::new));
    customizers.orderedStream().forEach((customizer) -> customizer.customize(schedulerFactoryBean));
    return schedulerFactoryBean;
  }

  private Properties asProperties(Map<String, String> source) {
    Properties properties = new Properties();
    properties.putAll(source);
    return properties;
  }


}
