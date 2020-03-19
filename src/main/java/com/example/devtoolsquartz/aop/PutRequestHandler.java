package com.example.devtoolsquartz.aop;

import com.alibaba.fastjson.JSON;
import com.hsrg.core.local.LocalCache;
import com.hsrg.core.local.LocalCacheFactory;
import com.hsrg.spring.BeanHelper;
import com.hsrg.spring.aop.AdviceType;
import com.hsrg.spring.aop.web.WebRequestAspect;
import com.hsrg.spring.aop.web.WebRequestJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PUT请求的处理，只处理 application/json
 */
@Order(10)
@Component
public class PutRequestHandler implements WebRequestAspect.WebRequestAopHandler {

  private static final Logger logger = LoggerFactory.getLogger(PutRequestHandler.class);

  public static final int MAX_CONTENT_LENGTH = (1024 << 10) * 10;


  public static final String CONTENT_TYPE = "content-type";
  public static final String APPLICATION_JSON = "application/json";

  private final LocalCache<PutRequestMethod> putMethodCache = LocalCacheFactory.newCache();
  private final Map<Method, PutRequestMethod> putMethodMap = new ConcurrentHashMap<>();

  @Override
  public boolean support(WebRequestJoinPoint joinPoint) {
    if (joinPoint.getAdviceType() != AdviceType.BEFORE) {
      return false;
    }

    final HttpServletRequest request = joinPoint.getRequest();
    if (HttpMethod.resolve(request.getMethod()) != HttpMethod.PUT) {
      return false;
    }

    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    PutRequestMethod prm = putMethodMap.get(method);
    if (prm == null) {
      prm = new PutRequestMethod(method);
      // 是否匹配
      prm.setMatch(request.getContentType().contains(APPLICATION_JSON));
      final Map<Integer, Parameter> map = new LinkedHashMap<>();
      final Parameter[] parameters = method.getParameters();
      for (int i = 0; i < parameters.length; i++) {
        map.put(i, parameters[i]);
      }
      prm.setParameters(Collections.unmodifiableMap(map));
      putMethodMap.put(method, prm);
    }
    putMethodCache.set(prm);
    return prm.isMatch();
  }

  @Override
  public void doBefore(WebRequestJoinPoint joinPoint) {
    final Object[] args = joinPoint.getArgs();
    if (args != null && args.length > 0) {
      final PutRequestMethod prm = putMethodCache.getAndRemove();
      HttpServletRequest request = joinPoint.getRequest();

      long contentLengthLong = request.getContentLengthLong();
      if (contentLengthLong <= MAX_CONTENT_LENGTH) {
        // 对于基本数据类型及其包装类型，不做处理
        // ServletRequest/ServletResponse/MultipartFile/InputStream/OutputStream...都不做处理
        try {
          final ServletInputStream in = request.getInputStream();
          byte[] buff = new byte[(int) contentLengthLong];
          if (in.read(buff) > 0) {
            prm.getParameters().forEach((index, parameter) -> {
              if (match(parameter)) {
                BeanHelper.copy(JSON.parseObject(buff, args[index].getClass()), args[index]);
              }
            });
          }
        } catch (IOException e) {
          throw new IllegalStateException(e);
        }
      } else {
        // 数据太大，处理不了
        logger.warn("数据太大，无法处理, contentLength = {}", contentLengthLong);
      }
    }
  }

  private boolean match(final Class<?> parameterType) {
    if (parameterType.isAssignableFrom(CharSequence.class)) {
      return false;
    }
    if (parameterType.isAssignableFrom(Number.class)) {
      return false;
    }
    if (parameterType.isAssignableFrom(Boolean.class)) {
      return false;
    }
    if (parameterType.isAssignableFrom(Character.class)) {
      return false;
    }
    if (parameterType.isAssignableFrom(ServletRequest.class)) {
      return false;
    }
    if (parameterType.isAssignableFrom(ServletResponse.class)) {
      return false;
    }
    if (parameterType.isAssignableFrom(InputStream.class)) {
      return false;
    }
    if (parameterType.isAssignableFrom(OutputStream.class)) {
      return false;
    }
    return !parameterType.isAssignableFrom(MultipartFile.class);
  }

  private boolean match(Parameter parameter) {
    return match(parameter.getType());
  }


  public static class PutRequestMethod {
    /**
     * Method
     */
    private final Method method;
    /**
     * 参数
     */
    private Map<Integer, Parameter> parameters;
    /**
     * 是否匹配
     */
    private volatile boolean match = false;

    public PutRequestMethod(Method method) {
      this.method = method;
    }

    public Method getMethod() {
      return method;
    }

    public Map<Integer, Parameter> getParameters() {
      return parameters;
    }

    public void setParameters(Map<Integer, Parameter> parameters) {
      this.parameters = parameters;
    }

    public boolean isMatch() {
      return match;
    }

    public void setMatch(boolean match) {
      this.match = match;
    }
  }

}
