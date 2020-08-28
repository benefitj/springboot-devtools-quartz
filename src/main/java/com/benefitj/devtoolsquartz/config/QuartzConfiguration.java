package com.benefitj.devtoolsquartz.config;

import com.benefitj.devtoolsquartz.quartz.scheduler.DefaultSchedulerFactory;
import com.benefitj.devtoolsquartz.quartz.scheduler.LoggingSchedulerListener;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.util.Map;
import java.util.Properties;

@Lazy
@EnableConfigurationProperties
@PropertySource("classpath:/quartz-spring.properties")
@ConditionalOnProperty(name = "quartz.enabled")
@Configuration
public class QuartzConfiguration {

  private static final String PREFIX_SPRING = "spring.quartz.properties.";


  @ConditionalOnMissingBean(DefaultSchedulerFactory.class)
  @Bean
  public DefaultSchedulerFactory schedulerFactory(QuartzProperties quartzProperties,
                                                  ApplicationContext context) {
    try {
      final Properties properties = new Properties();
      final Map<String, String> map = quartzProperties.getProperties();
      map.forEach((key, value) -> properties.put(
          key.replaceFirst(PREFIX_SPRING, ""), value));
      StdSchedulerFactory factory = new StdSchedulerFactory(properties);
      return new DefaultSchedulerFactory(context, factory);
    } catch (SchedulerException e) {
      throw new BeanInstantiationException(DefaultSchedulerFactory.class, e.getMessage());
    }
  }

  @ConditionalOnMissingBean(SchedulerFactoryBean.class)
  @Primary
  @Bean
  public SchedulerFactoryBean schedulerFactoryBean(QuartzProperties properties,
                                                   ObjectProvider<SchedulerFactoryBeanCustomizer> customizers,
                                                   @Autowired(required = false) ObjectProvider<JobDetail> jobDetails,
                                                   @Autowired(required = false) Map<String, Calendar> calendars,
                                                   ObjectProvider<Trigger> triggers,
                                                   ApplicationContext context,
                                                   SchedulerFactory schedulerFactory) {
    SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
    schedulerFactoryBean.setSchedulerFactory(schedulerFactory);
    SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
    jobFactory.setApplicationContext(context);
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

    schedulerFactoryBean.setSchedulerListeners(
        context.getBeansOfType(SchedulerListener.class)
            .values()
            .toArray(new SchedulerListener[0]));

    schedulerFactoryBean.setGlobalTriggerListeners(
        context.getBeansOfType(TriggerListener.class)
            .values()
            .toArray(new TriggerListener[0]));

    schedulerFactoryBean.setGlobalJobListeners(
        context.getBeansOfType(JobListener.class)
            .values()
            .toArray(new JobListener[0]));

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

  @Bean
  public LoggingSchedulerListener loggingGlobalListener() {
    return new LoggingSchedulerListener();
  }

}
