package com.example.devtoolsquartz.quartz;

import com.hsrg.core.ReflectUtils;
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
  private final Map<String, Scheduler> schedulerMap = new ConcurrentHashMap<>();
  /**
   * 调度器
   */
  private final AtomicReference<Collection<Scheduler>> schedulersRef = new AtomicReference<>(Collections.emptySet());

  private final SchedulerCreator creator = new SchedulerCreator();

  public DefaultSchedulerFactory(ApplicationContext ctx, StdSchedulerFactory factory) {
    this.ctx = ctx;
    this.factory = factory;
  }

  @Override
  public Scheduler getScheduler() {
    return get(DEFAULT);
  }

  @Override
  public Scheduler getScheduler(String schedName) {
    return get(schedName);
  }

  @Override
  public Collection<Scheduler> getAllSchedulers() {
    return schedulersRef.get();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    getScheduler();
  }

  private Scheduler get(String name) {
    return schedulerMap.computeIfAbsent(name, creator);
  }


  protected ApplicationContext getCtx() {
    return ctx;
  }

  protected StdSchedulerFactory getFactory() {
    return factory;
  }

  public class SchedulerCreator implements Function<String, Scheduler> {

    @Override
    public Scheduler apply(String name) {
      try {
        Scheduler s = DEFAULT.equals(name)
            ? getFactory().getScheduler()
            : getFactory().getScheduler(name);

        modifyListenerManager(s);

        final ListenerManager lm = s.getListenerManager();
        // 调度器监听
        Map<String, GlobalSchedulerListener> gslMap = getCtx().getBeansOfType(GlobalSchedulerListener.class);
        gslMap.forEach((key, l) -> lm.addSchedulerListener(l));
        // job监听
        Map<String, JobListener> gjlMap = getCtx().getBeansOfType(JobListener.class);
        gjlMap.forEach((key, l) -> lm.addJobListener(l));

        schedulersRef.set(Collections.unmodifiableCollection(schedulerMap.values()));

        return s;
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
            f -> ref.get() != null, // 找到字段后就停止查找
            f -> f.getType().isAssignableFrom(ListenerManager.class), // 过滤不匹配的类型
            ref::set);
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
              f -> ref.get() != null,
              f -> f.getType().isAssignableFrom(QuartzScheduler.class),
              f -> ref.set(ReflectUtils.getFieldValue(f, scheduler)));
          modifyListenerManager(ref.get());
        }
      }
    }

  }


}
