package com.example.devtoolsquartz.aop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 打印请求日志
 */
@Order(100)
@Component
@Aspect
public class RequestLogPrinterAop {

  private static final Logger log = LoggerFactory.getLogger(RequestLogPrinterAop.class);


  //   execution：用于匹配方法执行的连接点；
  //   within：用于匹配指定类型内的方法执行；
  //   this：用于匹配当前AOP代理对象类型的执行方法；注意是AOP代理对象的类型匹配，这样就可能包括引入接口也类型匹配；
  //   target：用于匹配当前目标对象类型的执行方法；注意是目标对象的类型匹配，这样就不包括引入接口也类型匹配；
  //   args：用于匹配当前执行的方法传入的参数为指定类型的执行方法；
  //   @within：用于匹配所以持有指定注解类型内的方法；
  //   @target：用于匹配当前目标对象类型的执行方法，其中目标对象持有指定的注解；
  //   @args：用于匹配当前执行的方法传入的参数持有指定注解的执行；
  //   @annotation：用于匹配当前执行方法持有指定注解的方法；
  //   bean：Spring AOP扩展的，AspectJ没有对于指示符，用于匹配特定名称的Bean对象的执行方法；
  //   reference pointcut：表示引用其他命名切入点，只有@ApectJ风格支持，Schema风格不支持。


  /**
   * 切入点表达式的语法格式: execution([权限修饰符] [返回值类型] [简单类名/全类名] [方法名]([参数列表]))
   */
  @Pointcut(
      "!execution(@com.hsrg.spring.aop.AopIgnore * *(..))" // 没有被AopIgnore注解注释
          + " && ("
          + " (@annotation(org.springframework.web.bind.annotation.RequestMapping)"
          + " || @annotation(org.springframework.stereotype.Controller)"
          + " || @annotation(org.springframework.web.bind.annotation.RestController)"
          + " || @annotation(org.springframework.web.bind.annotation.Mapping)"
          + " || @annotation(org.springframework.web.bind.annotation.GetMapping)"
          + " || @annotation(org.springframework.web.bind.annotation.PostMapping)"
          + " || @annotation(org.springframework.web.bind.annotation.PutMapping)"
          + " || @annotation(org.springframework.web.bind.annotation.DeleteMapping)"
          + " || @annotation(org.springframework.web.bind.annotation.PatchMapping)"
          + ")" // 需要被springMVC注解注释
          //    某个controller包下 && public修饰的方法
          + " && (within(com..controller..*) && execution(public * *(..))))"
          + ")"
  )
  public void pointcut() {
    // ~
  }

  @Before("pointcut()")
  public void doBefore(JoinPoint joinPoint) {
    ServletRequestAttributes attrs = getRequestAttributes();
    if (attrs != null) {
      ProceedingJoinPoint point = (ProceedingJoinPoint) joinPoint;
      MethodSignature signature = (MethodSignature) point.getSignature();
      Method method = signature.getMethod();
      //method = AopUtils.getMostSpecificMethod(signature.getMethod(), signature.getDeclaringType());
      final Map<String, Object> argsMap = new LinkedHashMap<>();
      Object[] args = point.getArgs();
      Parameter[] parameters = method.getParameters();
      for (int i = 0; i < parameters.length; i++) {
        Object arg = args[i];
        if (arg instanceof ServletRequest) {
          argsMap.put(parameters[i].getName(), "[ServletRequest]");
        } else if (arg instanceof ServletResponse) {
          argsMap.put(parameters[i].getName(), "[ServletResponse]");
        } else if (arg instanceof MultipartFile) {
          argsMap.put(parameters[i].getName(), "[MultipartFile]");
        } else if (arg instanceof InputStream) {
          argsMap.put(parameters[i].getName(), "[InputStream]");
        } else if (arg instanceof OutputStream) {
          argsMap.put(parameters[i].getName(), "[OutputStream]");
        } else {
          argsMap.put(parameters[i].getName(), arg);
        }
      }
      HttpServletRequest request = attrs.getRequest();
      log.info("uri: {}, method: {}, params: {}",
          request.getRequestURI(), request.getMethod(), JSON.toJSONString(argsMap));
    }
  }

  @Nullable
  protected ServletRequestAttributes getRequestAttributes() {
    return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
  }

}
