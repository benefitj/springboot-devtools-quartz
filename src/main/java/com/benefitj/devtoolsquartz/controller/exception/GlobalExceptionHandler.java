package com.benefitj.devtoolsquartz.controller.exception;

import com.benefitj.devtoolsquartz.vo.HttpResult;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * 客户端被中断的异常，比如原本弹出的是浏览器的下载，然后被改为了迅雷下载
   */
  @ExceptionHandler(value = ClientAbortException.class)
  public HttpResult clientAbortExceptionHandler(HttpServletRequest req, ClientAbortException e) {
    log.error("[" + req.getRequestURI() + "]请求出错: ", e);
    if (StringUtils.isBlank(e.getMessage())) {
      return HttpResult.failure(500, "服务器错误");
    }
    return HttpResult.failure(500, e.getMessage());
  }

  @ExceptionHandler(value = {IllegalStateException.class, IllegalArgumentException.class})
  public HttpResult simpleExceptionHandler(HttpServletRequest req, Throwable e) {
    log.error("[{}]请求出错: {}", req.getRequestURI(), e.getMessage());
    if (StringUtils.isBlank(e.getMessage())) {
      e.printStackTrace();
      return HttpResult.failure(500, "服务器错误");
    }
    return HttpResult.failure(400, e.getMessage());
  }

  @ExceptionHandler(value = Throwable.class)
  public HttpResult defaultHandler(HttpServletRequest req, Throwable e) {
    log.error("[{}]请求出错: {}", req.getRequestURI(), e.getMessage(), e);
    if (StringUtils.isBlank(e.getMessage())) {
      e.printStackTrace();
      return HttpResult.failure(500, "服务器错误");
    }
    return HttpResult.failure(400, e.getMessage());
  }

}
