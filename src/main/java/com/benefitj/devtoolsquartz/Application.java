package com.benefitj.devtoolsquartz;

import com.benefitj.spring.aop.EnableAutoAopWebHandler;
import com.benefitj.spring.aop.log.EnableRequestLoggingHandler;
import com.benefitj.spring.applicationevent.EnableAutoApplicationListener;
import com.benefitj.spring.ctx.EnableSpringCtxInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoApplicationListener
@EnableAutoAopWebHandler
@EnableRequestLoggingHandler
@EnableSpringCtxInit
@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
