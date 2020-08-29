package com.benefitj.devtoolsquartz.dao;

import com.benefitj.core.DateFmtter;
import com.benefitj.devtoolsquartz.core.BaseMapper;
import com.benefitj.devtoolsquartz.core.Checker;
import com.benefitj.devtoolsquartz.model.QrtzJobTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.util.Sqls;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

/**
 * Job task mapper
 */
@Mapper
public interface QrtzJobTaskMapper extends BaseMapper<QrtzJobTask> {

  /**
   * 统计 job name 出现的次数
   *
   * @param jobName job name
   * @return 返回出现的次数
   */
  default int countJobName(@Param("jobName") String jobName) {
    return selectCountByExample(example(Sqls.custom()
        .andEqualTo("jobName", jobName)));
  }

  /**
   * 统计 trigger name 出现的次数
   *
   * @param triggerName trigger name
   * @return 返回出现的次数
   */
  default int countTriggerName(@Param("triggerName") String triggerName) {
    return selectCountByExample(example(Sqls.custom()
        .andEqualTo("triggerName", triggerName)));
  }

  /**
   * 查询调度任务的分页
   *
   * @param condition  条件
   * @param startTime  开始时间
   * @param endTime    结束时间
   * @param multiLevel 是否为多层级机构的调度(不支持)
   * @return 返回查询的列表
   */
  @Transient
  default List<QrtzJobTask> selectList(QrtzJobTask condition, Date startTime, Date endTime, boolean multiLevel) {
    final Sqls sqls = Sqls.custom();
    Checker.checkNotNull(startTime, () -> sqls.andGreaterThanOrEqualTo("create_time", fmt(startTime)));
    Checker.checkNotNull(endTime, () -> sqls.andLessThanOrEqualTo("create_time", fmt(endTime)));
    Checker.checkNotBlank(condition.getTriggerType(), s -> sqls.andLike("triggerType", s));
    Checker.checkNotBlank(condition.getJobGroup(), s -> sqls.andLike("jobGroup", s));
    Checker.checkNotBlank(condition.getJobName(), s -> sqls.andLike("jobName", s));
    Checker.checkNotBlank(condition.getTriggerGroup(), s -> sqls.andLike("triggerGroup", s));
    Checker.checkNotBlank(condition.getTriggerName(), s -> sqls.andLike("triggerName", s));
    return selectByExample(example(sqls));
  }


  static String fmt(Object time) {
    return DateFmtter.fmt(time, DateFmtter._yMd);
  }

}
