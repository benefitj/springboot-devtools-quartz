package com.example.devtoolsquartz.quartz.task;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.devtoolsquartz.quartz.job.JobType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class JobTask<T extends JobTask<T>> implements Cloneable {

  public static final String KEY_ID = "id";
  public static final String KEY_JOB_DATA = "jobData";

  /**
   * 任务id
   */
  @Id
  @Column(name = "id", columnDefinition = "varchar(32) comment '调度任务的ID'", length = 32)
  private String id;
  /**
   * group名称
   */
  @Column(name = "job_group", columnDefinition = "varchar(50) comment '任务分组'", length = 50)
  private String jobGroup;
  /**
   * Job名称
   */
  @Column(name = "job_name", columnDefinition = "varchar(50) comment '任务名称'", length = 50)
  private String jobName;
  /**
   * 任务别名
   */
  @Column(name = "job_alias", columnDefinition = "varchar(50) comment '任务别名'", length = 50)
  private String jobAlias;
  /**
   * 描述
   */
  @Column(name = "description", columnDefinition = "varchar(1024) comment '任务描述'", length = 1024)
  private String description;
  /**
   * 是否异步
   */
  @Column(name = "async", columnDefinition = "tinyint(1) comment '是否异步执行程序'", length = 1)
  private Boolean async = Boolean.FALSE;
  /**
   * 是否恢复
   */
  @Column(name = "recovery", columnDefinition = "tinyint(1) comment '是否恢复'", length = 1)
  private Boolean recovery = Boolean.FALSE;
  /**
   * 执行后是否持久化数据，默认不持久化
   */
  @Column(name = "persistent", columnDefinition = "tinyint(1) comment '执行后是否持久化数据，默认不持久化'", length = 1)
  private Boolean persistent = Boolean.FALSE;
  /**
   * 是否不允许并发执行，默认并发执行
   */
  @Column(name = "disallow_concurrent", columnDefinition = "tinyint(1) comment '是否不允许并发执行，默认并发执行'", length = 1)
  private Boolean disallowConcurrent = Boolean.FALSE;
  /**
   * 创建时间
   */
  @JSONField(serialize = false)
  @JsonIgnore
  @Column(name = "create_time", columnDefinition = "datetime comment '创建时间'")
  private Date createTime;
  /**
   * 更新时间
   */
  @JSONField(serialize = false)
  @JsonIgnore
  @Column(name = "update_time", columnDefinition = "datetime comment '更新时间'")
  private Date updateTime;
  /**
   * Job的执行类型，参考: {@link JobType }
   */
  @Column(name = "job_type", columnDefinition = "varchar(50) comment 'Job的执行类型'", length = 50)
  private String jobType;
  /**
   * Job的调用类和方法
   */
  @Column(name = "job_class", columnDefinition = "varchar(50) comment 'Job的实现类'", length = 50)
  private String jobClass;
  /**
   * Job的调用类和方法
   */
  @Column(name = "job_method", columnDefinition = "varchar(50) comment 'Job的实现类'", length = 50)
  private String jobMethod;
  /**
   * job携带的数据
   */
  @Column(name = "job_data", columnDefinition = "varchar(1024) comment 'job携带的数据'", length = 1024)
  private String jobData;
  /**
   * 可用状态
   */
  @Column(name = "active", columnDefinition = "tinyint(1) comment '可用状态'", length = 1)
  private Boolean active = Boolean.TRUE;

  public JobTask() {
  }

  @JSONField(serialize = false)
  @JsonIgnore
  protected T self() {
    return (T) this;
  }

  public String getId() {
    return id;
  }

  public T setId(String id) {
    this.id = id;
    return self();
  }

  public String getJobName() {
    return jobName;
  }

  public T setJobName(String jobName) {
    this.jobName = jobName;
    return self();
  }

  public String getJobGroup() {
    return jobGroup;
  }

  public T setJobGroup(String jobGroup) {
    this.jobGroup = jobGroup;
    return self();
  }

  public String getJobAlias() {
    return jobAlias;
  }

  public T setJobAlias(String jobAlias) {
    this.jobAlias = jobAlias;
    return self();
  }

  public String getDescription() {
    return description;
  }

  public T setDescription(String description) {
    this.description = description;
    return self();
  }

  public Boolean getAsync() {
    return async;
  }

  public T setAsync(Boolean async) {
    this.async = async;
    return self();
  }

  public Boolean getRecovery() {
    return recovery;
  }

  public T setRecovery(Boolean recovery) {
    this.recovery = (recovery != null) ? recovery : Boolean.FALSE;
    return self();
  }

  public Boolean getPersistent() {
    return persistent;
  }

  public T setPersistent(Boolean persistAfter) {
    this.persistent = (persistAfter != null) ? persistAfter : Boolean.FALSE;
    return self();
  }

  public Boolean getDisallowConcurrent() {
    return disallowConcurrent;
  }

  public T setDisallowConcurrent(Boolean concurrent) {
    this.disallowConcurrent = (concurrent != null) ? concurrent : Boolean.TRUE;
    return self();
  }

  public Date getCreateTime() {
    return createTime;
  }

  public T setCreateTime(Date createTime) {
    this.createTime = createTime;
    return self();
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public T setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
    return self();
  }


  public String getJobType() {
    return jobType;
  }

  public void setJobType(String jobType) {
    this.jobType = jobType;
  }

  public String getJobClass() {
    return jobClass;
  }

  public T setJobClass(String jobClass) {
    this.jobClass = jobClass;
    return self();
  }

  public String getJobMethod() {
    return jobMethod;
  }

  public T setJobMethod(String jobMethod) {
    this.jobMethod = jobMethod;
    return self();
  }

  public String getJobData() {
    return jobData;
  }

  public T setJobData(String jobData) {
    this.jobData = jobData;
    return self();
  }

  public Boolean getActive() {
    return active;
  }

  public T setActive(Boolean active) {
    this.active = active;
    return self();
  }

  @Override
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException ex) {
      throw new IncompatibleClassChangeError("Not Cloneable.");
    }
  }

}
