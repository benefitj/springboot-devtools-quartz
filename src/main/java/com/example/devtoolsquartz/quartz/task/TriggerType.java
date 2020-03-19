package com.example.devtoolsquartz.quartz.task;

import org.quartz.*;

/**
 * 触发器类型
 */
public enum TriggerType {

  SIMPLE, CRON;

  /**
   * 失效策略
   */
  public enum SimplePolicy {
    /**
     * 智能, {@link Trigger#MISFIRE_INSTRUCTION_SMART_POLICY}
     */
    SMART_POLICY(0),
    /**
     * 忽略策略, {@link Trigger#MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY}
     */
    IGNORE_MISFIRE_POLICY(-1),
    /**
     * {@link SimpleTrigger#MISFIRE_INSTRUCTION_FIRE_NOW}
     */
    FIRE_NOW(1),
    /**
     * {@link SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT}
     */
    NOW_WITH_EXISTING_REPEAT_COUNT(2),
    /**
     * {@link SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT}
     */
    NOW_WITH_REMAINING_REPEAT_COUNT(3),
    /**
     * {@link SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT}
     */
    NEXT_WITH_REMAINING_COUNT(4),
    /**
     * {@link SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT}
     */
    NEXT_WITH_EXISTING_COUNT(5);

    /**
     * 失效策略
     */
    private final int policy;


    SimplePolicy(int policy) {
      this.policy = policy;
    }

    public int getPolicy() {
      return policy;
    }

    /**
     * 选择调度策略
     */
    public static SimpleScheduleBuilder schedulePolicy(SimpleScheduleBuilder ssb, Integer policy) {
      return schedulePolicy(ssb, ofSimple(policy));
    }

    /**
     * 选择调度策略
     */
    public static SimpleScheduleBuilder schedulePolicy(SimpleScheduleBuilder ssb, SimplePolicy policy) {
      switch (policy) {
        case IGNORE_MISFIRE_POLICY:
          ssb.withMisfireHandlingInstructionIgnoreMisfires();
          break;
        case FIRE_NOW:
          ssb.withMisfireHandlingInstructionFireNow();
          break;
        case NOW_WITH_EXISTING_REPEAT_COUNT:
          ssb.withMisfireHandlingInstructionNowWithExistingCount();
          break;
        case NOW_WITH_REMAINING_REPEAT_COUNT:
          ssb.withMisfireHandlingInstructionNowWithRemainingCount();
          break;
        case NEXT_WITH_REMAINING_COUNT:
          ssb.withMisfireHandlingInstructionNextWithRemainingCount();
          break;
        case NEXT_WITH_EXISTING_COUNT:
          ssb.withMisfireHandlingInstructionNextWithExistingCount();
          break;
        case SMART_POLICY:
        default:
          break;
      }
      return ssb;
    }

  }

  /**
   * 失效策略
   */
  public enum CronPolicy {
    /**
     * 智能, {@link Trigger#MISFIRE_INSTRUCTION_SMART_POLICY}
     */
    SMART_POLICY(0),
    /**
     * 忽略策略, {@link Trigger#MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY}
     */
    IGNORE_MISFIRE_POLICY(-1),
    /**
     * {@link CronTrigger#MISFIRE_INSTRUCTION_FIRE_ONCE_NOW}
     */
    FIRE_ONCE_NOW(1),

    /**
     * {@link CronTrigger#MISFIRE_INSTRUCTION_DO_NOTHING}
     */
    DO_NOTHING(2);

    /**
     * 失效策略
     */
    private final int policy;

    CronPolicy(int policy) {
      this.policy = policy;
    }

    public int getPolicy() {
      return policy;
    }

    /**
     * 选择调度策略
     */
    public static CronScheduleBuilder schedulePolicy(CronScheduleBuilder csb, Integer policy) {
      return schedulePolicy(csb, ofCron(policy));
    }

    /**
     * 选择调度策略
     */
    public static CronScheduleBuilder schedulePolicy(CronScheduleBuilder csb, CronPolicy policy) {
      switch (policy) {
        case IGNORE_MISFIRE_POLICY:
          csb.withMisfireHandlingInstructionIgnoreMisfires();
          break;
        case FIRE_ONCE_NOW:
          csb.withMisfireHandlingInstructionFireAndProceed();
          break;
        case DO_NOTHING:
          csb.withMisfireHandlingInstructionDoNothing();
          break;
        case SMART_POLICY:
        default:
          csb.withMisfireHandlingInstructionDoNothing();
          break;
      }
      return csb;
    }

  }

  /**
   * 获取触发器类型
   */
  public static TriggerType of(String name) {
    for (TriggerType type : values()) {
      if (type.name().equalsIgnoreCase(name)) {
        return type;
      }
    }
    return null;
  }

  /**
   * 验证触发器名称
   */
  public static boolean validateName(String name) {
    return of(name) != null;
  }


  public static SimplePolicy ofSimple(Integer value) {
    if (value != null) {
      for (SimplePolicy s : SimplePolicy.values()) {
        if (s.policy == value) {
          return s;
        }
      }
    }
    return SimplePolicy.SMART_POLICY;
  }

  public static CronPolicy ofCron(Integer value) {
    if (value != null) {
      for (CronPolicy c : CronPolicy.values()) {
        if (c.policy == value) {
          return c;
        }
      }
    }
    return CronPolicy.SMART_POLICY;
  }

}
