package com.example.devtoolsquartz.core;

import java.util.Random;
import java.util.UUID;

/**
 * 生成随机字符串
 */
public class IdUtils {

  private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final char[] CHARS_ARRAY = CHARS.toCharArray();

  private static final String EMPTY = "";

  private static final ThreadLocal<Random> randomLocal = ThreadLocal.withInitial(Random::new);

  private static Random getRandom() {
    return randomLocal.get();
  }

  /**
   * 获取随机的字符串
   *
   * @param length 随机字符串长度
   * @return 返回随机字符串
   */
  public static String nextId(int length) {
    return nextId(null, null, length);
  }

  /**
   * 获取随机的字符串
   *
   * @param chars  字符
   * @param length 随机字符串长度
   * @return 返回随机字符串
   */
  public static String nextId(char[] chars, int length) {
    return nextId(chars, null, null, length);
  }

  /**
   * 获取随机的字符串
   *
   * @param prefix 前缀
   * @param suffix 后缀
   * @param length 随机字符串长度
   * @return 返回随机字符串
   */
  public static String nextId(String prefix, String suffix, int length) {
    return nextId(CHARS_ARRAY, prefix, suffix, length);
  }

  /**
   * 获取随机的字符串
   *
   * @param chars  字符
   * @param prefix 前缀
   * @param suffix 后缀
   * @param length 随机字符串长度
   * @return 返回随机字符串
   */
  public static String nextId(char[] chars, String prefix, String suffix, int length) {
    if (length > 0) {
      StringBuilder sb = new StringBuilder(length);
      sb.append(checkNotNull(prefix));
      for (int i = 0; i < length; i++) {
        sb.append(nextChar(chars));
      }
      sb.append(checkNotNull(suffix));
      return sb.toString();
    }
    return "";
  }

  /**
   * 随机获取下一个字符
   *
   * @return 返回随机字符
   */
  public static char defaultNextChar() {
    return nextChar(CHARS_ARRAY);
  }

  /**
   * 随机获取下一个字符
   *
   * @param chars 字符数组
   * @return 返回随机字符
   */
  public static char nextChar(char[] chars) {
    return chars[getRandom().nextInt(chars.length)];
  }

  /**
   * 获取UUID
   */
  public static String uuid() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  private static String checkNotNull(String str) {
    return str != null ? str : EMPTY;
  }

}
