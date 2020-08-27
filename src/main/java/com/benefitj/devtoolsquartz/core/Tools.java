package com.benefitj.devtoolsquartz.core;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Tools {

  /**
   * 检查为 null
   *
   * @param t    被检查的对象
   * @param func 处理的函数
   * @return 返回合法的值
   */
  public static <T> T checkNull(T t, Supplier<T> func) {
    return t == null ? func.get() : t;
  }

  /**
   * 检查为 null
   *
   * @param t 被检查的对象
   */
  public static <T> void checkNull(T t, Runnable r) {
    if (t == null) {
      r.run();
    }
  }

  /**
   * 检查为 null
   *
   * @param t 被检查的对象
   * @param r 处理的函数
   * @return 返回合法的值
   */
  public static <T> void checkNotNull(T t, Runnable r) {
    if (t != null) {
      r.run();
    }
  }

  /**
   * 检查不为 null
   *
   * @param t    被检查的对象
   * @param func 处理的函数
   * @return 返回合法的值
   */
  public static <T, R> R checkNotNull(T t, Function<T, R> func) {
    return t != null ? func.apply(t) : null;
  }

  /**
   * 检查不为 null
   *
   * @param t            被检查的对象
   * @param defaultValue 默认值
   * @return 返回合法的值
   */
  public static <T> T checkNotNull(T t, T defaultValue) {
    return t != null ? t : defaultValue;
  }

  /**
   * 检查不为空
   *
   * @param str          字符串
   * @param defaultValue 默认值
   * @return 返回合法的值
   */
  public static String checkNotEmpty(String str, String defaultValue) {
    return StringUtils.isNotEmpty(str) ? str : defaultValue;
  }

  /**
   * 检查不为空或空字符串
   *
   * @param str          字符串
   * @param defaultValue 默认值
   * @return 返回合法的值
   */
  public static String checkNotBlank(String str, String defaultValue) {
    return StringUtils.isNotBlank(str) ? str : defaultValue;
  }

  /**
   * 检查不为空或空字符串
   *
   * @param str      字符串
   * @param consumer 处理函数
   */
  public static void checkNotBlank(String str, Consumer<String> consumer) {
    if (StringUtils.isNotBlank(str)) {
      consumer.accept(str);
    }
  }

  /**
   * 检查为空或空字符串
   *
   * @param str  字符串
   * @param func 处理函数
   * @return 返回合法的值
   */
  public static void checkBlank(String str, Runnable func) {
    if (StringUtils.isBlank(str)) {
      func.run();
    }
  }

  public static void requireNotBlank(String str, String errorMsg) {
    if (StringUtils.isBlank(str)) {
      throw new IllegalStateException(errorMsg);
    }
  }

  public static void requireNotNull(Object o, String errorMsg) {
    if (o == null) {
      throw new IllegalStateException(errorMsg);
    }
  }
}
