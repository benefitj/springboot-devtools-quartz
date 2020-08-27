package com.benefitj.devtoolsquartz.quartz.scheduler;

import com.benefitj.core.ReflectUtils;
import org.quartz.*;
import org.quartz.core.QuartzScheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class DefaultSchedulerFactory implements SchedulerFactory, InitializingBean {

  private static final String DEFAULT = "quartzScheduler";

  private final ApplicationContext ctx;
  /**
   * 调度器工厂
   */
  private final StdSchedulerFactory factory;
  /**
   * Schedulers
   */
  private final Map<String, QrtzScheduler> schedulerMap = new ConcurrentHashMap<>();
  private final SchedulerCreator creator = new SchedulerCreator();
  private final Object lock = new Object();
  /**
   * 调度器
   */
  private volatile Collection<Scheduler> schedulers = Collections.emptySet();

  public DefaultSchedulerFactory(ApplicationContext ctx, StdSchedulerFactory factory) {
    this.ctx = ctx;
    this.factory = factory;
  }

  @Override
  public QrtzScheduler getScheduler() {
    return get(DEFAULT);
  }

  @Override
  public QrtzScheduler getScheduler(String schedName) {
    return get(schedName);
  }

  @Override
  public Collection<Scheduler> getAllSchedulers() {
    synchronized (lock) {
      return schedulers;
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    getScheduler();
  }

  private QrtzScheduler get(String name) {
    QrtzScheduler s = schedulerMap.computeIfAbsent(name, creator);
    if (creator.isChange()) {
      synchronized (lock) {
        setSchedulers(Collections.unmodifiableCollection(schedulerMap.values()));
      }
    }
    return s;
  }


  protected ApplicationContext getCtx() {
    return ctx;
  }

  protected StdSchedulerFactory getFactory() {
    return factory;
  }


  private void setSchedulers(Collection<Scheduler> schedulers) {
    this.schedulers = schedulers;
  }

  public class SchedulerCreator implements Function<String, QrtzScheduler> {

    private final ThreadLocal<Boolean> changeState = new ThreadLocal<>();

    @Override
    public QrtzScheduler apply(String name) {
      try {
        synchronized (lock) {
          Scheduler s = DEFAULT.equals(name)
              ? factory.getScheduler()
              : factory.getScheduler(name);

          modifyListenerManager(s);

          final ListenerManager lm = s.getListenerManager();
          // 调度器监听
          Map<String, SchedulerListener> gslMap = getCtx().getBeansOfType(SchedulerListener.class);
          gslMap.forEach((key, l) -> lm.addSchedulerListener(l));
          // job监听
          Map<String, JobListener> gjlMap = getCtx().getBeansOfType(JobListener.class);
          gjlMap.forEach((key, l) -> lm.addJobListener(l));

          changeState.set(Boolean.TRUE);

          return s instanceof QrtzScheduler
              ? (QrtzScheduler) s
              : new QrtzSchedulerDelegate(s);
        }
      } catch (SchedulerException e) {
        throw new IllegalStateException(e);
      }
    }

    /**
     * 修改 ListenerManager 的对象
     *
     * @param scheduler
     */
    private void modifyListenerManager(Object scheduler) {
      if (scheduler instanceof QuartzScheduler) {
        final AtomicReference<Field> ref = new AtomicReference<>();
        ReflectUtils.foreachField(scheduler.getClass(),
            f -> f.getType().isAssignableFrom(ListenerManager.class), // 过滤不匹配的类型
            ref::set,
            f -> ref.get() != null); // 找到字段后就停止查找
        final Field f = ref.get();
        if (f != null) {
          final DefaultListenerManager dlm = new DefaultListenerManager();
          final ListenerManager lm = ReflectUtils.getFieldValue(f, scheduler);
          if (lm != null) {
            // 添加监听
            lm.getSchedulerListeners().forEach(dlm::addSchedulerListener);
            lm.getTriggerListeners().forEach(tl -> {
              dlm.addTriggerListener(tl);
              for (Matcher<TriggerKey> m : lm.getTriggerListenerMatchers(tl.getName())) {
                dlm.addTriggerListenerMatcher(tl.getName(), m);
              }
            });
            lm.getJobListeners().forEach(jl -> {
              dlm.addJobListener(jl);
              for (Matcher<JobKey> m : lm.getJobListenerMatchers(jl.getName())) {
                dlm.addJobListenerMatcher(jl.getName(), m);
              }
            });
          }
          // 通过反射修改
          ReflectUtils.setFieldValue(f, scheduler, dlm);
        }
      } else {
        if (scheduler instanceof Scheduler) {
          final AtomicReference<QuartzScheduler> ref = new AtomicReference<>();
          ReflectUtils.foreachField(scheduler.getClass(),
              f -> f.getType().isAssignableFrom(QuartzScheduler.class),
              f -> ref.set(ReflectUtils.getFieldValue(f, scheduler)),
              f -> ref.get() != null);
          modifyListenerManager(ref.get());
        }
      }
    }

    /**
     * 判断是否被修改了
     */
    public boolean isChange() {
      final Boolean state = changeState.get();
      if (state != null) {
        changeState.remove();
      }
      return Boolean.TRUE.equals(state);
    }

  }


}
