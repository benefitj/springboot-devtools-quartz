package com.benefitj.devtoolsquartz.quartz.scheduler;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface QrtzScheduler extends Scheduler {

  /**
   * Returns the name of the <code>Scheduler</code>.
   */
  @Override
  String getSchedulerName();

  /**
   * Returns the instance Id of the <code>Scheduler</code>.
   */
  @Override
  String getSchedulerInstanceId() throws QrtzSchedulerException;

  /**
   * Returns the <code>SchedulerContext</code> of the <code>Scheduler</code>.
   */
  @Override
  SchedulerContext getContext() throws QrtzSchedulerException;

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
  void start() throws QrtzSchedulerException;

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
  void startDelayed(int seconds) throws QrtzSchedulerException;

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
  boolean isStarted() throws QrtzSchedulerException;

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
  void standby() throws QrtzSchedulerException;

  /**
   * Reports whether the <code>Scheduler</code> is in stand-by mode.
   *
   * @see #standby()
   * @see #start()
   */
  @Override
  boolean isInStandbyMode() throws QrtzSchedulerException;

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
  void shutdown() throws QrtzSchedulerException;

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
  void shutdown(boolean waitForJobsToComplete) throws QrtzSchedulerException;

  /**
   * Reports whether the <code>Scheduler</code> has been shutdown.
   */
  @Override
  boolean isShutdown() throws QrtzSchedulerException;

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
  SchedulerMetaData getMetaData() throws QrtzSchedulerException;

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
  List<JobExecutionContext> getCurrentlyExecutingJobs() throws QrtzSchedulerException;

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
  void setJobFactory(JobFactory factory) throws QrtzSchedulerException;

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
  ListenerManager getListenerManager() throws QrtzSchedulerException;

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
  Date scheduleJob(JobDetail jobDetail, Trigger trigger) throws QrtzSchedulerException;

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
  Date scheduleJob(Trigger trigger) throws QrtzSchedulerException;

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
  void scheduleJobs(Map<JobDetail, Set<? extends Trigger>> triggersAndJobs, boolean replace) throws QrtzSchedulerException;

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
  void scheduleJob(JobDetail jobDetail, Set<? extends Trigger> triggersForJob, boolean replace) throws QrtzSchedulerException;

  /**
   * Remove the indicated <code>{@link Trigger}</code> from the scheduler.
   *
   * <p>If the related job does not have any other triggers, and the job is
   * not durable, then the job will also be deleted.</p>
   *
   * @param triggerKey
   */
  @Override
  boolean unscheduleJob(TriggerKey triggerKey) throws QrtzSchedulerException;

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
  boolean unscheduleJobs(List<TriggerKey> triggerKeys) throws QrtzSchedulerException;

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
  Date rescheduleJob(TriggerKey triggerKey, Trigger newTrigger) throws QrtzSchedulerException;

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
  void addJob(JobDetail jobDetail, boolean replace) throws QrtzSchedulerException;

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
  void addJob(JobDetail jobDetail, boolean replace, boolean storeNonDurableWhileAwaitingScheduling) throws QrtzSchedulerException;

  /**
   * Delete the identified <code>Job</code> from the Scheduler - and any
   * associated <code>Trigger</code>s.
   *
   * @param jobKey
   * @return true if the Job was found and deleted.
   * @throws QrtzSchedulerException if there is an internal Scheduler error.
   */
  @Override
  boolean deleteJob(JobKey jobKey) throws QrtzSchedulerException;

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
  boolean deleteJobs(List<JobKey> jobKeys) throws QrtzSchedulerException;

  /**
   * Trigger the identified <code>{@link JobDetail}</code>
   * (execute it now).
   *
   * @param jobKey
   */
  @Override
  void triggerJob(JobKey jobKey) throws QrtzSchedulerException;

  /**
   * Trigger the identified <code>{@link JobDetail}</code>
   * (execute it now).
   *
   * @param jobKey
   * @param data   the (possibly <code>null</code>) JobDataMap to be
   */
  @Override
  void triggerJob(JobKey jobKey, JobDataMap data) throws QrtzSchedulerException;

  /**
   * Pause the <code>{@link JobDetail}</code> with the given
   * key - by pausing all of its current <code>Trigger</code>s.
   *
   * @param jobKey
   * @see #resumeJob(JobKey)
   */
  @Override
  void pauseJob(JobKey jobKey) throws QrtzSchedulerException;

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
  void pauseJobs(GroupMatcher<JobKey> matcher) throws QrtzSchedulerException;

  /**
   * Pause the <code>{@link Trigger}</code> with the given key.
   *
   * @param triggerKey
   * @see #resumeTrigger(TriggerKey)
   */
  @Override
  void pauseTrigger(TriggerKey triggerKey) throws QrtzSchedulerException;

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
  void pauseTriggers(GroupMatcher<TriggerKey> matcher) throws QrtzSchedulerException;

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
  void resumeJob(JobKey jobKey) throws QrtzSchedulerException;

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
  void resumeJobs(GroupMatcher<JobKey> matcher) throws QrtzSchedulerException;

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
  void resumeTrigger(TriggerKey triggerKey) throws QrtzSchedulerException;

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
  void resumeTriggers(GroupMatcher<TriggerKey> matcher) throws QrtzSchedulerException;

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
  void pauseAll() throws QrtzSchedulerException;

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
  void resumeAll() throws QrtzSchedulerException;

  /**
   * Get the names of all known <code>{@link JobDetail}</code>
   * groups.
   */
  @Override
  List<String> getJobGroupNames() throws QrtzSchedulerException;

  /**
   * Get the keys of all the <code>{@link JobDetail}s</code>
   * in the matching groups.
   *
   * @param matcher Matcher to evaluate against known groups
   * @return Set of all keys matching
   * @throws QrtzSchedulerException On error
   */
  @Override
  Set<JobKey> getJobKeys(GroupMatcher<JobKey> matcher) throws QrtzSchedulerException;

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
  List<? extends Trigger> getTriggersOfJob(JobKey jobKey) throws QrtzSchedulerException;

  /**
   * Get the names of all known <code>{@link Trigger}</code> groups.
   */
  @Override
  List<String> getTriggerGroupNames() throws QrtzSchedulerException;

  /**
   * Get the names of all the <code>{@link Trigger}s</code> in the given
   * group.
   *
   * @param matcher Matcher to evaluate against known groups
   * @return List of all keys matching
   * @throws QrtzSchedulerException On error
   */
  @Override
  Set<TriggerKey> getTriggerKeys(GroupMatcher<TriggerKey> matcher) throws QrtzSchedulerException;

  /**
   * Get the names of all <code>{@link Trigger}</code> groups that are paused.
   */
  @Override
  Set<String> getPausedTriggerGroups() throws QrtzSchedulerException;

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
  JobDetail getJobDetail(JobKey jobKey) throws QrtzSchedulerException;

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
  Trigger getTrigger(TriggerKey triggerKey) throws QrtzSchedulerException;

  /**
   * Get the current state of the identified <code>{@link Trigger}</code>.
   *
   * @param triggerKey
   * @see Trigger.TriggerState
   */
  @Override
  Trigger.TriggerState getTriggerState(TriggerKey triggerKey) throws QrtzSchedulerException;

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
  void resetTriggerFromErrorState(TriggerKey triggerKey) throws QrtzSchedulerException;

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
  void addCalendar(String calName, Calendar calendar, boolean replace, boolean updateTriggers) throws QrtzSchedulerException;

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
  boolean deleteCalendar(String calName) throws QrtzSchedulerException;

  /**
   * Get the <code>{@link Calendar}</code> instance with the given name.
   *
   * @param calName
   */
  @Override
  Calendar getCalendar(String calName) throws QrtzSchedulerException;

  /**
   * Get the names of all registered <code>{@link Calendar}s</code>.
   */
  @Override
  List<String> getCalendarNames() throws QrtzSchedulerException;

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
  boolean interrupt(JobKey jobKey) throws UnableToInterruptJobException;

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
  boolean interrupt(String fireInstanceId) throws UnableToInterruptJobException;

  /**
   * Determine whether a {@link Job} with the given identifier already
   * exists within the scheduler.
   *
   * @param jobKey the identifier to check for
   * @return true if a Job exists with the given identifier
   * @throws QrtzSchedulerException
   */
  @Override
  boolean checkExists(JobKey jobKey) throws QrtzSchedulerException;

  /**
   * Determine whether a {@link Trigger} with the given identifier already
   * exists within the scheduler.
   *
   * @param triggerKey the identifier to check for
   * @return true if a Trigger exists with the given identifier
   * @throws QrtzSchedulerException
   */
  @Override
  boolean checkExists(TriggerKey triggerKey) throws QrtzSchedulerException;

  /**
   * Clears (deletes!) all scheduling data - all {@link Job}s, {@link Trigger}s
   * {@link Calendar}s.
   *
   * @throws QrtzSchedulerException
   */
  @Override
  void clear() throws QrtzSchedulerException;
}
