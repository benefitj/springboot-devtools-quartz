package com.benefitj.devtoolsquartz.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

/**
 * 数据库配置
 */
@EntityScan("com.benefitj.devtoolsquartz.quartz.task")
@MapperScan({"com.benefitj.devtoolsquartz.dao"})
@EnableTransactionManagement
@Configuration
public class DatabaseConfig {

  /**
   * 事务管理
   *
   * @param dataSource DataSource对象
   * @return
   */
  @Bean
  public DataSourceTransactionManager transactionManager(DataSource dataSource) {
    DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource);
    manager.setDefaultTimeout(6000);
    manager.setRollbackOnCommitFailure(true);
    return manager;
  }

  // druid 配置
  @Bean
  public ServletRegistrationBean druidServlet() {
    ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
    // 登录查看信息的账号密码.
    bean.addInitParameter("loginUsername", "admin");
    bean.addInitParameter("loginPassword", "123456");
    return bean;
  }

  @Bean
  public FilterRegistrationBean filterRegistrationBean() {
    FilterRegistrationBean bean = new FilterRegistrationBean();
    bean.setFilter(new WebStatFilter());
    bean.addUrlPatterns("/*");
    bean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
    return bean;
  }

}
