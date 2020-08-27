package com.benefitj.devtoolsquartz.quartz.scheduler;

import com.benefitj.core.functions.IConsumer;
import com.benefitj.core.functions.IFunction;
import org.quartz.Calendar;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobFactory;

import java.util.*;

public class QrtzSchedulerDelegate implements QrtzScheduler {

  private final Scheduler scheduler;

  public QrtzSchedulerDelegate(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  public Scheduler getScheduler() {
    return scheduler;
  }

  public <T> T myScheduler(IFunction<Scheduler, T> func) {
    try {
      return func.apply(getScheduler());
    } catch (Exception e) {
      throw new QrtzSchedulerException(e);
    }
  }

  public void myScheduler(IConsumer<Scheduler> consumer) {
    try {
      consumer.accept(getScheduler());
    } catch (Exception e) {
      throw new QrtzSchedulerException(e);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    QrtzSchedulerDelegate that = (QrtzSchedulerDelegate) o;
    return Objects.equals(scheduler, that.scheduler);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scheduler);
  }

  /**
   * Returns the name of the <code>Scheduler</code>.
   */
  @Override
  public String getSchedulerName() {
    return myScheduler(Scheduler::getSchedulerName);
  }

  /**
   * Returns the instance Id of the <code>Scheduler</code>.
   */
  @Override
  public String getSchedulerInstanceId() throws QrtzSchedulerException {
    return myScheduler(Scheduler::getSchedulerInstanceId);
  }

  /**
   * Returns the <code>SchedulerContext</code> of the <code>Scheduler</code>.
   */
  @Override
  public SchedulerContext getContext() throws QrtzSchedulerException {
    return myScheduler(Scheduler::getContext);
  }

  /**
   * Starts the <code>Scheduler</code>'s threads that fire <code>{@link Trigger}s</code>.
   * When a scheduler is first created it is in "stand-by" mode, and will not
   * fire triggers.  The scheduler can also be put into stand-by mode by
   * calling the <code>standby()</code> method.
   *
   * <p>
   * The misfire/recovery process will be started, if it is the initial call
   * to this method on this scheduler instance.
   * </p>
   *
   * @throws QrtzSchedulerException if <code>shutdown()</code> has been called, or there is an
   *                                error within the <code>Scheduler</code>.
   * @see #startDelayed(int)
   * @see #standby()
   * @see #shutdown()
   */
  @Override
  public void start() throws QrtzSchedulerException {
    myScheduler(Scheduler::start);
  }

  /**
   * Calls {#start()} after the indicated number of seconds.
   * (This call does not block). This can be useful within applications that
   * have initializers that create the scheduler immediately, before the
   * resources needed by the executing jobs have been fully initialized.
   *
   * @param seconds
   * @throws QrtzSchedulerException if <code>shutdown()</code> has been called, or there is an
   *                                error within the <code>Scheduler</code>.
   * @see #start()
   * @see #standby()
   * @see #shutdown()
   */
  @Override
  public void startDelayed(int seconds) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) (s) -> s.startDelayed(seconds));
  }

  /**
   * Whether the scheduler has been started.
   *
   * <p>
   * Note: This only reflects whether <code>{@link #start()}</code> has ever
   * been called on this Scheduler, so it will return <code>true</code> even
   * if the <code>Scheduler</code> is currently in standby mode or has been
   * since shutdown.
   * </p>
   *
   * @see #start()
   * @see #isShutdown()
   * @see #isInStandbyMode()
   */
  @Override
  public boolean isStarted() throws QrtzSchedulerException {
    return myScheduler(Scheduler::isStarted);
  }

  /**
   * Temporarily halts the <code>Scheduler</code>'s firing of <code>{@link Trigger}s</code>.
   *
   * <p>
   * When <code>start()</code> is called (to bring the scheduler out of
   * stand-by mode), trigger misfire instructions will NOT be applied
   * during the execution of the <code>start()</code> method - any misfires
   * will be detected immediately afterward (by the <code>JobStore</code>'s
   * normal process).
   * </p>
   *
   * <p>
   * The scheduler is not destroyed, and can be re-started at any time.
   * </p>
   *
   * @see #start()
   * @see #pauseAll()
   */
  @Override
  public void standby() throws QrtzSchedulerException {
    myScheduler(Scheduler::standby);
  }

  /**
   * Reports whether the <code>Scheduler</code> is in stand-by mode.
   *
   * @see #standby()
   * @see #start()
   */
  @Override
  public boolean isInStandbyMode() throws QrtzSchedulerException {
    return myScheduler(Scheduler::isInStandbyMode);
  }

  /**
   * Halts the <code>Scheduler</code>'s firing of <code>{@link Trigger}s</code>,
   * and cleans up all resources associated with the Scheduler. Equivalent to
   * <code>shutdown(false)</code>.
   *
   * <p>
   * The scheduler cannot be re-started.
   * </p>
   *
   * @see #shutdown(boolean)
   */
  @Override
  public void shutdown() throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) Scheduler::shutdown);
  }

  /**
   * Halts the <code>Scheduler</code>'s firing of <code>{@link Trigger}s</code>,
   * and cleans up all resources associated with the Scheduler.
   *
   * <p>
   * The scheduler cannot be re-started.
   * </p>
   *
   * @param waitForJobsToComplete if <code>true</code> the scheduler will not allow this method
   *                              to return until all currently executing jobs have completed.
   * @see #shutdown
   */
  @Override
  public void shutdown(boolean waitForJobsToComplete) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.shutdown(waitForJobsToComplete));
  }

  /**
   * Reports whether the <code>Scheduler</code> has been shutdown.
   */
  @Override
  public boolean isShutdown() throws QrtzSchedulerException {
    return myScheduler(Scheduler::isShutdown);
  }

  /**
   * Get a <code>SchedulerMetaData</code> object describing the settings
   * and capabilities of the scheduler instance.
   *
   * <p>
   * Note that the data returned is an 'instantaneous' snap-shot, and that as
   * soon as it's returned, the meta data values may be different.
   * </p>
   */
  @Override
  public SchedulerMetaData getMetaData() throws QrtzSchedulerException {
    return myScheduler(Scheduler::getMetaData);
  }

  /**
   * Return a list of <code>JobExecutionContext</code> objects that
   * represent all currently executing Jobs in this Scheduler instance.
   *
   * <p>
   * This method is not cluster aware.  That is, it will only return Jobs
   * currently executing in this Scheduler instance, not across the entire
   * cluster.
   * </p>
   *
   * <p>
   * Note that the list returned is an 'instantaneous' snap-shot, and that as
   * soon as it's returned, the true list of executing jobs may be different.
   * Also please read the doc associated with <code>JobExecutionContext</code>-
   * especially if you're using RMI.
   * </p>
   *
   * @see JobExecutionContext
   */
  @Override
  public List<JobExecutionContext> getCurrentlyExecutingJobs() throws QrtzSchedulerException {
    return myScheduler(Scheduler::getCurrentlyExecutingJobs);
  }

  /**
   * Set the <code>JobFactory</code> that will be responsible for producing
   * instances of <code>Job</code> classes.
   *
   * <p>
   * JobFactories may be of use to those wishing to have their application
   * produce <code>Job</code> instances via some special mechanism, such as to
   * give the opportunity for dependency injection.
   * </p>
   *
   * @param factory
   * @see JobFactory
   */
  @Override
  public void setJobFactory(JobFactory factory) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.setJobFactory(factory));
  }

  /**
   * Get a reference to the scheduler's <code>ListenerManager</code>,
   * through which listeners may be registered.
   *
   * @return the scheduler's <code>ListenerManager</code>
   * @throws QrtzSchedulerException if the scheduler is not local
   * @see ListenerManager
   * @see JobListener
   * @see TriggerListener
   * @see SchedulerListener
   */
  @Override
  public ListenerManager getListenerManager() throws QrtzSchedulerException {
    return myScheduler(Scheduler::getListenerManager);
  }

  /**
   * Add the given <code>{@link JobDetail}</code> to the
   * Scheduler, and associate the given <code>{@link Trigger}</code> with
   * it.
   *
   * <p>
   * If the given Trigger does not reference any <code>Job</code>, then it
   * will be set to reference the Job passed with it into this method.
   * </p>
   *
   * @param jobDetail
   * @param trigger
   * @throws QrtzSchedulerException if the Job or Trigger cannot be added to the Scheduler, or
   *                                there is an internal Scheduler error.
   */
  @Override
  public Date scheduleJob(JobDetail jobDetail, Trigger trigger) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Date>) s -> s.scheduleJob(jobDetail, trigger));
  }

  /**
   * Schedule the given <code>{@link Trigger}</code> with the
   * <code>Job</code> identified by the <code>Trigger</code>'s settings.
   *
   * @param trigger
   * @throws QrtzSchedulerException if the indicated Job does not exist, or the Trigger cannot be
   *                                added to the Scheduler, or there is an internal Scheduler
   *                                error.
   */
  @Override
  public Date scheduleJob(Trigger trigger) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Date>) s -> s.scheduleJob(trigger));
  }

  /**
   * Schedule all of the given jobs with the related set of triggers.
   *
   * <p>If any of the given jobs or triggers already exist (or more
   * specifically, if the keys are not unique) and the replace
   * parameter is not set to true then an exception will be thrown.</p>
   *
   * @param triggersAndJobs
   * @param replace
   * @throws ObjectAlreadyExistsException if the job/trigger keys
   *                                      are not unique and the replace flag is not set to true.
   */
  @Override
  public void scheduleJobs(Map<JobDetail, Set<? extends Trigger>> triggersAndJobs, boolean replace) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.scheduleJobs(triggersAndJobs, replace));
  }

  /**
   * Schedule the given job with the related set of triggers.
   *
   * <p>If any of the given job or triggers already exist (or more
   * specifically, if the keys are not unique) and the replace
   * parameter is not set to true then an exception will be thrown.</p>
   *
   * @param jobDetail
   * @param triggersForJob
   * @param replace
   * @throws ObjectAlreadyExistsException if the job/trigger keys
   *                                      are not unique and the replace flag is not set to true.
   */
  @Override
  public void scheduleJob(JobDetail jobDetail, Set<? extends Trigger> triggersForJob, boolean replace) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.scheduleJob(jobDetail, triggersForJob, replace));
  }

  /**
   * Remove the indicated <code>{@link Trigger}</code> from the scheduler.
   *
   * <p>If the related job does not have any other triggers, and the job is
   * not durable, then the job will also be deleted.</p>
   *
   * @param triggerKey
   */
  @Override
  public boolean unscheduleJob(TriggerKey triggerKey) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Boolean>) s -> s.unscheduleJob(triggerKey));
  }

  /**
   * Remove all of the indicated <code>{@link Trigger}</code>s from the scheduler.
   *
   * <p>If the related job does not have any other triggers, and the job is
   * not durable, then the job will also be deleted.</p>
   *
   * <p>Note that while this bulk operation is likely more efficient than
   * invoking <code>unscheduleJob(TriggerKey triggerKey)</code> several
   * times, it may have the adverse affect of holding data locks for a
   * single long duration of time (rather than lots of small durations
   * of time).</p>
   *
   * @param triggerKeys
   */
  @Override
  public boolean unscheduleJobs(List<TriggerKey> triggerKeys) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Boolean>) s -> s.unscheduleJobs(triggerKeys));
  }

  /**
   * Remove (delete) the <code>{@link Trigger}</code> with the
   * given key, and store the new given one - which must be associated
   * with the same job (the new trigger must have the job name & group specified)
   * - however, the new trigger need not have the same name as the old trigger.
   *
   * @param triggerKey identity of the trigger to replace
   * @param newTrigger The new <code>Trigger</code> to be stored.
   * @return <code>null</code> if a <code>Trigger</code> with the given
   * name & group was not found and removed from the store (and the
   * new trigger is therefore not stored), otherwise
   * the first fire time of the newly scheduled trigger is returned.
   */
  @Override
  public Date rescheduleJob(TriggerKey triggerKey, Trigger newTrigger) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Date>) s -> s.rescheduleJob(triggerKey, newTrigger));
  }

  /**
   * Add the given <code>Job</code> to the Scheduler - with no associated
   * <code>Trigger</code>. The <code>Job</code> will be 'dormant' until
   * it is scheduled with a <code>Trigger</code>, or <code>Scheduler.triggerJob()</code>
   * is called for it.
   *
   * <p>
   * The <code>Job</code> must by definition be 'durable', if it is not,
   * QrtzSchedulerException will be thrown.
   * </p>
   *
   * @param jobDetail
   * @param replace
   * @throws QrtzSchedulerException if there is an internal Scheduler error, or if the Job is not
   *                                durable, or a Job with the same name already exists, and
   *                                <code>replace</code> is <code>false</code>.
   * @see #addJob(JobDetail, boolean, boolean)
   */
  @Override
  public void addJob(JobDetail jobDetail, boolean replace) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.addJob(jobDetail, replace));
  }

  /**
   * Add the given <code>Job</code> to the Scheduler - with no associated
   * <code>Trigger</code>. The <code>Job</code> will be 'dormant' until
   * it is scheduled with a <code>Trigger</code>, or <code>Scheduler.triggerJob()</code>
   * is called for it.
   *
   * <p>
   * With the <code>storeNonDurableWhileAwaitingScheduling</code> parameter
   * set to <code>true</code>, a non-durable job can be stored.  Once it is
   * scheduled, it will resume normal non-durable behavior (i.e. be deleted
   * once there are no remaining associated triggers).
   * </p>
   *
   * @param jobDetail
   * @param replace
   * @param storeNonDurableWhileAwaitingScheduling
   * @throws QrtzSchedulerException if there is an internal Scheduler error, or if the Job is not
   *                                durable, or a Job with the same name already exists, and
   *                                <code>replace</code> is <code>false</code>.
   */
  @Override
  public void addJob(JobDetail jobDetail, boolean replace, boolean storeNonDurableWhileAwaitingScheduling) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.addJob(jobDetail, replace, storeNonDurableWhileAwaitingScheduling));
  }

  /**
   * Delete the identified <code>Job</code> from the Scheduler - and any
   * associated <code>Trigger</code>s.
   *
   * @param jobKey
   * @return true if the Job was found and deleted.
   * @throws QrtzSchedulerException if there is an internal Scheduler error.
   */
  @Override
  public boolean deleteJob(JobKey jobKey) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Boolean>) s -> s.deleteJob(jobKey));
  }

  /**
   * Delete the identified <code>Job</code>s from the Scheduler - and any
   * associated <code>Trigger</code>s.
   *
   * <p>Note that while this bulk operation is likely more efficient than
   * invoking <code>deleteJob(JobKey jobKey)</code> several
   * times, it may have the adverse affect of holding data locks for a
   * single long duration of time (rather than lots of small durations
   * of time).</p>
   *
   * @param jobKeys
   * @return true if all of the Jobs were found and deleted, false if
   * one or more were not deleted.
   * @throws QrtzSchedulerException if there is an internal Scheduler error.
   */
  @Override
  public boolean deleteJobs(List<JobKey> jobKeys) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Boolean>) s -> s.deleteJobs(jobKeys));
  }

  /**
   * Trigger the identified <code>{@link JobDetail}</code>
   * (execute it now).
   *
   * @param jobKey
   */
  @Override
  public void triggerJob(JobKey jobKey) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.triggerJob(jobKey));
  }

  /**
   * Trigger the identified <code>{@link JobDetail}</code>
   * (execute it now).
   *
   * @param jobKey
   * @param data   the (possibly <code>null</code>) JobDataMap to be
   */
  @Override
  public void triggerJob(JobKey jobKey, JobDataMap data) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.triggerJob(jobKey, data));
  }

  /**
   * Pause the <code>{@link JobDetail}</code> with the given
   * key - by pausing all of its current <code>Trigger</code>s.
   *
   * @param jobKey
   * @see #resumeJob(JobKey)
   */
  @Override
  public void pauseJob(JobKey jobKey) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.pauseJob(jobKey));
  }

  /**
   * Pause all of the <code>{@link JobDetail}s</code> in the
   * matching groups - by pausing all of their <code>Trigger</code>s.
   *
   * <p>
   * The Scheduler will "remember" the groups paused, and impose the
   * pause on any new jobs that are added to any of those groups
   * until it is resumed.
   * </p>
   *
   * <p>NOTE: There is a limitation that only exactly matched groups
   * can be remembered as paused.  For example, if there are pre-existing
   * job in groups "aaa" and "bbb" and a matcher is given to pause
   * groups that start with "a" then the group "aaa" will be remembered
   * as paused and any subsequently added jobs in group "aaa" will be paused,
   * however if a job is added to group "axx" it will not be paused,
   * as "axx" wasn't known at the time the "group starts with a" matcher
   * was applied.  HOWEVER, if there are pre-existing groups "aaa" and
   * "bbb" and a matcher is given to pause the group "axx" (with a
   * group equals matcher) then no jobs will be paused, but it will be
   * remembered that group "axx" is paused and later when a job is added
   * in that group, it will become paused.</p>
   *
   * @param matcher The matcher to evaluate against know groups
   * @throws QrtzSchedulerException On error
   * @see #resumeJobs(GroupMatcher)
   */
  @Override
  public void pauseJobs(GroupMatcher<JobKey> matcher) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.pauseJobs(matcher));
  }

  /**
   * Pause the <code>{@link Trigger}</code> with the given key.
   *
   * @param triggerKey
   * @see #resumeTrigger(TriggerKey)
   */
  @Override
  public void pauseTrigger(TriggerKey triggerKey) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.pauseTrigger(triggerKey));
  }

  /**
   * Pause all of the <code>{@link Trigger}s</code> in the groups matching.
   *
   * <p>
   * The Scheduler will "remember" all the groups paused, and impose the
   * pause on any new triggers that are added to any of those groups
   * until it is resumed.
   * </p>
   *
   * <p>NOTE: There is a limitation that only exactly matched groups
   * can be remembered as paused.  For example, if there are pre-existing
   * triggers in groups "aaa" and "bbb" and a matcher is given to pause
   * groups that start with "a" then the group "aaa" will be remembered as
   * paused and any subsequently added triggers in that group be paused,
   * however if a trigger is added to group "axx" it will not be paused,
   * as "axx" wasn't known at the time the "group starts with a" matcher
   * was applied.  HOWEVER, if there are pre-existing groups "aaa" and
   * "bbb" and a matcher is given to pause the group "axx" (with a
   * group equals matcher) then no triggers will be paused, but it will be
   * remembered that group "axx" is paused and later when a trigger is added
   * in that group, it will become paused.</p>
   *
   * @param matcher The matcher to evaluate against know groups
   * @throws QrtzSchedulerException
   * @see #resumeTriggers(GroupMatcher)
   */
  @Override
  public void pauseTriggers(GroupMatcher<TriggerKey> matcher) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.pauseTriggers(matcher));
  }

  /**
   * Resume (un-pause) the <code>{@link JobDetail}</code> with
   * the given key.
   *
   * <p>
   * If any of the <code>Job</code>'s<code>Trigger</code> s missed one
   * or more fire-times, then the <code>Trigger</code>'s misfire
   * instruction will be applied.
   * </p>
   *
   * @param jobKey
   * @see #pauseJob(JobKey)
   */
  @Override
  public void resumeJob(JobKey jobKey) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.resumeJob(jobKey));
  }

  /**
   * Resume (un-pause) all of the <code>{@link JobDetail}s</code>
   * in matching groups.
   *
   * <p>
   * If any of the <code>Job</code> s had <code>Trigger</code> s that
   * missed one or more fire-times, then the <code>Trigger</code>'s
   * misfire instruction will be applied.
   * </p>
   *
   * @param matcher The matcher to evaluate against known paused groups
   * @throws QrtzSchedulerException On error
   * @see #pauseJobs(GroupMatcher)
   */
  @Override
  public void resumeJobs(GroupMatcher<JobKey> matcher) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.resumeJobs(matcher));
  }

  /**
   * Resume (un-pause) the <code>{@link Trigger}</code> with the given
   * key.
   *
   * <p>
   * If the <code>Trigger</code> missed one or more fire-times, then the
   * <code>Trigger</code>'s misfire instruction will be applied.
   * </p>
   *
   * @param triggerKey
   * @see #pauseTrigger(TriggerKey)
   */
  @Override
  public void resumeTrigger(TriggerKey triggerKey) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.resumeTrigger(triggerKey));
  }

  /**
   * Resume (un-pause) all of the <code>{@link Trigger}s</code> in matching groups.
   *
   * <p>
   * If any <code>Trigger</code> missed one or more fire-times, then the
   * <code>Trigger</code>'s misfire instruction will be applied.
   * </p>
   *
   * @param matcher The matcher to evaluate against know paused groups
   * @throws QrtzSchedulerException On error
   * @see #pauseTriggers(GroupMatcher)
   */
  @Override
  public void resumeTriggers(GroupMatcher<TriggerKey> matcher) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.resumeTriggers(matcher));
  }

  /**
   * Pause all triggers - similar to calling <code>pauseTriggerGroup(group)</code>
   * on every group, however, after using this method <code>resumeAll()</code>
   * must be called to clear the scheduler's state of 'remembering' that all
   * new triggers will be paused as they are added.
   *
   * <p>
   * When <code>resumeAll()</code> is called (to un-pause), trigger misfire
   * instructions WILL be applied.
   * </p>
   *
   * @see #resumeAll()
   * @see #pauseTriggers(GroupMatcher)
   * @see #standby()
   */
  @Override
  public void pauseAll() throws QrtzSchedulerException {
    myScheduler(Scheduler::pauseAll);
  }

  /**
   * Resume (un-pause) all triggers - similar to calling
   * <code>resumeTriggerGroup(group)</code> on every group.
   *
   * <p>
   * If any <code>Trigger</code> missed one or more fire-times, then the
   * <code>Trigger</code>'s misfire instruction will be applied.
   * </p>
   *
   * @see #pauseAll()
   */
  @Override
  public void resumeAll() throws QrtzSchedulerException {
    myScheduler(Scheduler::resumeAll);
  }

  /**
   * Get the names of all known <code>{@link JobDetail}</code>
   * groups.
   */
  @Override
  public List<String> getJobGroupNames() throws QrtzSchedulerException {
    return myScheduler(Scheduler::getJobGroupNames);
  }

  /**
   * Get the keys of all the <code>{@link JobDetail}s</code>
   * in the matching groups.
   *
   * @param matcher Matcher to evaluate against known groups
   * @return Set of all keys matching
   * @throws QrtzSchedulerException On error
   */
  @Override
  public Set<JobKey> getJobKeys(GroupMatcher<JobKey> matcher) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Set<JobKey>>) s -> s.getJobKeys(matcher));
  }

  /**
   * Get all <code>{@link Trigger}</code> s that are associated with the
   * identified <code>{@link JobDetail}</code>.
   *
   * <p>The returned Trigger objects will be snap-shots of the actual stored
   * triggers.  If you wish to modify a trigger, you must re-store the
   * trigger afterward (e.g. see {@link #rescheduleJob(TriggerKey, Trigger)}).
   * </p>
   *
   * @param jobKey
   */
  @Override
  public List<? extends Trigger> getTriggersOfJob(JobKey jobKey) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, List<? extends Trigger>>) s -> s.getTriggersOfJob(jobKey));
  }

  /**
   * Get the names of all known <code>{@link Trigger}</code> groups.
   */
  @Override
  public List<String> getTriggerGroupNames() throws QrtzSchedulerException {
    return myScheduler(Scheduler::getTriggerGroupNames);
  }

  /**
   * Get the names of all the <code>{@link Trigger}s</code> in the given
   * group.
   *
   * @param matcher Matcher to evaluate against known groups
   * @return List of all keys matching
   * @throws QrtzSchedulerException On error
   */
  @Override
  public Set<TriggerKey> getTriggerKeys(GroupMatcher<TriggerKey> matcher) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Set<TriggerKey>>) s -> s.getTriggerKeys(matcher));
  }

  /**
   * Get the names of all <code>{@link Trigger}</code> groups that are paused.
   */
  @Override
  public Set<String> getPausedTriggerGroups() throws QrtzSchedulerException {
    return myScheduler(Scheduler::getPausedTriggerGroups);
  }

  /**
   * Get the <code>{@link JobDetail}</code> for the <code>Job</code>
   * instance with the given key.
   *
   * <p>The returned JobDetail object will be a snap-shot of the actual stored
   * JobDetail.  If you wish to modify the JobDetail, you must re-store the
   * JobDetail afterward (e.g. see {@link #addJob(JobDetail, boolean)}).
   * </p>
   *
   * @param jobKey
   */
  @Override
  public JobDetail getJobDetail(JobKey jobKey) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, JobDetail>) s -> s.getJobDetail(jobKey));
  }

  /**
   * Get the <code>{@link Trigger}</code> instance with the given key.
   *
   * <p>The returned Trigger object will be a snap-shot of the actual stored
   * trigger.  If you wish to modify the trigger, you must re-store the
   * trigger afterward (e.g. see {@link #rescheduleJob(TriggerKey, Trigger)}).
   * </p>
   *
   * @param triggerKey
   */
  @Override
  public Trigger getTrigger(TriggerKey triggerKey) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Trigger>) s -> s.getTrigger(triggerKey));
  }

  /**
   * Get the current state of the identified <code>{@link Trigger}</code>.
   *
   * @param triggerKey
   * @see Trigger.TriggerState
   */
  @Override
  public Trigger.TriggerState getTriggerState(TriggerKey triggerKey) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Trigger.TriggerState>) s -> s.getTriggerState(triggerKey));
  }

  /**
   * Reset the current state of the identified <code>{@link Trigger}</code>
   * from {@link TriggerState#ERROR} to {@link TriggerState#NORMAL} or
   * {@link TriggerState#PAUSED} as appropriate.
   *
   * <p>Only affects triggers that are in ERROR state - if identified trigger is not
   * in that state then the result is a no-op.</p>
   *
   * <p>The result will be the trigger returning to the normal, waiting to
   * be fired state, unless the trigger's group has been paused, in which
   * case it will go into the PAUSED state.</p>
   *
   * @param triggerKey
   * @see Trigger.TriggerState
   */
  @Override
  public void resetTriggerFromErrorState(TriggerKey triggerKey) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.resetTriggerFromErrorState(triggerKey));
  }

  /**
   * Add (register) the given <code>Calendar</code> to the Scheduler.
   *
   * @param calName
   * @param calendar
   * @param replace
   * @param updateTriggers whether or not to update existing triggers that
   *                       referenced the already existing calendar so that they are 'correct'
   *                       based on the new trigger.
   * @throws QrtzSchedulerException if there is an internal Scheduler error, or a Calendar with
   *                                the same name already exists, and <code>replace</code> is
   *                                <code>false</code>.
   */
  @Override
  public void addCalendar(String calName, Calendar calendar, boolean replace, boolean updateTriggers) throws QrtzSchedulerException {
    myScheduler((IConsumer<Scheduler>) s -> s.addCalendar(calName, calendar, replace, updateTriggers));
  }

  /**
   * Delete the identified <code>Calendar</code> from the Scheduler.
   *
   * <p>
   * If removal of the <code>Calendar</code> would result in
   * <code>Trigger</code>s pointing to non-existent calendars, then a
   * <code>QrtzSchedulerException</code> will be thrown.
   * </p>
   *
   * @param calName
   * @return true if the Calendar was found and deleted.
   * @throws QrtzSchedulerException if there is an internal Scheduler error, or one or more
   *                                triggers reference the calendar
   */
  @Override
  public boolean deleteCalendar(String calName) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Boolean>) s -> s.deleteCalendar(calName));
  }

  /**
   * Get the <code>{@link Calendar}</code> instance with the given name.
   *
   * @param calName
   */
  @Override
  public Calendar getCalendar(String calName) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Calendar>) s -> s.getCalendar(calName));
  }

  /**
   * Get the names of all registered <code>{@link Calendar}s</code>.
   */
  @Override
  public List<String> getCalendarNames() throws QrtzSchedulerException {
    return myScheduler(Scheduler::getCalendarNames);
  }

  /**
   * Request the interruption, within this Scheduler instance, of all
   * currently executing instances of the identified <code>Job</code>, which
   * must be an implementor of the <code>InterruptableJob</code> interface.
   *
   * <p>
   * If more than one instance of the identified job is currently executing,
   * the <code>InterruptableJob#interrupt()</code> method will be called on
   * each instance.  However, there is a limitation that in the case that
   * <code>interrupt()</code> on one instances throws an exception, all
   * remaining  instances (that have not yet been interrupted) will not have
   * their <code>interrupt()</code> method called.
   * </p>
   *
   * <p>
   * This method is not cluster aware.  That is, it will only interrupt
   * instances of the identified InterruptableJob currently executing in this
   * Scheduler instance, not across the entire cluster.
   * </p>
   *
   * @param jobKey
   * @return true if at least one instance of the identified job was found
   * and interrupted.
   * @throws UnableToInterruptJobException if the job does not implement
   *                                       <code>InterruptableJob</code>, or there is an exception while
   *                                       interrupting the job.
   * @see InterruptableJob#interrupt()
   * @see #getCurrentlyExecutingJobs()
   * @see #interrupt(String)
   */
  @Override
  public boolean interrupt(JobKey jobKey) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Boolean>) s -> s.interrupt(jobKey));
  }

  /**
   * Request the interruption, within this Scheduler instance, of the
   * identified executing <code>Job</code> instance, which
   * must be an implementor of the <code>InterruptableJob</code> interface.
   *
   * <p>
   * This method is not cluster aware.  That is, it will only interrupt
   * instances of the identified InterruptableJob currently executing in this
   * Scheduler instance, not across the entire cluster.
   * </p>
   *
   * @param fireInstanceId the unique identifier of the job instance to
   *                       be interrupted (see {@link JobExecutionContext#getFireInstanceId()}
   * @return true if the identified job instance was found and interrupted.
   * @throws UnableToInterruptJobException if the job does not implement
   *                                       <code>InterruptableJob</code>, or there is an exception while
   *                                       interrupting the job.
   * @see InterruptableJob#interrupt()
   * @see #getCurrentlyExecutingJobs()
   * @see JobExecutionContext#getFireInstanceId()
   * @see #interrupt(JobKey)
   */
  @Override
  public boolean interrupt(String fireInstanceId) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Boolean>) s -> s.interrupt(fireInstanceId));
  }

  /**
   * Determine whether a {@link Job} with the given identifier already
   * exists within the scheduler.
   *
   * @param jobKey the identifier to check for
   * @return true if a Job exists with the given identifier
   * @throws QrtzSchedulerException
   */
  @Override
  public boolean checkExists(JobKey jobKey) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Boolean>) s -> s.checkExists(jobKey));
  }

  /**
   * Determine whether a {@link Trigger} with the given identifier already
   * exists within the scheduler.
   *
   * @param triggerKey the identifier to check for
   * @return true if a Trigger exists with the given identifier
   * @throws QrtzSchedulerException
   */
  @Override
  public boolean checkExists(TriggerKey triggerKey) throws QrtzSchedulerException {
    return myScheduler((IFunction<Scheduler, Boolean>) s -> s.checkExists(triggerKey));
  }

  /**
   * Clears (deletes!) all scheduling data - all {@link Job}s, {@link Trigger}s
   * {@link Calendar}s.
   *
   * @throws QrtzSchedulerException
   */
  @Override
  public void clear() throws QrtzSchedulerException {
    myScheduler(Scheduler::clear);
  }
}
