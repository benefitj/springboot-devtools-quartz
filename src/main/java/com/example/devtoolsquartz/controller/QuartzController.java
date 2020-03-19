package com.example.devtoolsquartz.controller;


import com.example.devtoolsquartz.vo.CommonStatus;
import com.example.devtoolsquartz.vo.HttpResult;
import com.example.devtoolsquartz.quartz.task.QrtzJobTask;
import com.example.devtoolsquartz.service.QrtzJobTaskService;
import com.hsrg.spring.aop.web.AopWebPointCut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Quartz的调度任务
 */
@AopWebPointCut
@Api(tags = {"quartz"}, description = "Quartz的调度任务")
@RestController
@RequestMapping("/quartz")
public class QuartzController {

  @Autowired
  private QrtzJobTaskService qrtzService;

  @ApiOperation("获取Cron调度任务")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "Cron调度任务的ID", required = true, dataType = "String"),
  })
  @GetMapping
  public HttpResult<?> get(String id) {
    QrtzJobTask task = qrtzService.get(id);
    return HttpResult.create(CommonStatus.OK, task);
  }

  @ApiOperation("添加任务调度")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "task", dataTypeClass = QrtzJobTask.class),
  })
  @PostMapping
  public HttpResult<?> create(QrtzJobTask task) {
    task = qrtzService.create(task);
    return HttpResult.create(CommonStatus.CREATED, task);
  }

  @ApiOperation("更新任务调度")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "task", value = "任务调度数据", dataTypeClass = QrtzJobTask.class),
  })
  @PutMapping
  public HttpResult<?> update(QrtzJobTask task) {
    if (StringUtils.isBlank(task.getId())) {
      return HttpResult.failure("任务调度任务的ID不能为空");
    }
    task = qrtzService.update(task);
    return HttpResult.create(CommonStatus.CREATED, task);
  }

  @ApiOperation("删除任务调度")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "任务调度ID", dataType = "String"),
      @ApiImplicitParam(name = "force", value = "是否强制", dataType = "Boolean"),
  })
  @DeleteMapping
  public HttpResult<?> delete(String id, Boolean force) {
    int count = qrtzService.delete(id, Boolean.TRUE.equals(force));
    return HttpResult.create(CommonStatus.NO_CONTENT, count);
  }

  @ApiOperation("改变任务调度的状态")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "任务调度ID", dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "status", value = "状态", dataType = "Boolean", paramType = "form"),
  })
  @PatchMapping("/active")
  public HttpResult<?> changeActive(String id, Boolean status) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.failure("调度任务的ID不能为空");
    }
    Boolean result = qrtzService.changeActive(id, status);
    return HttpResult.create(CommonStatus.OK, result);
  }

  @ApiOperation("获取机构的任务调度列表")
  @ApiImplicitParams({})
  @GetMapping("/list")
  public HttpResult<?> getAll() {
    List<QrtzJobTask> list = qrtzService.getAll();
    return HttpResult.success(list);
  }


}
