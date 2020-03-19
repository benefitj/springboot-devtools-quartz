package com.example.devtoolsquartz.vo;

/**
 * Http的结果
 *
 * @param <T>
 */
public class HttpResult<T> {

  /**
   * 成功
   *
   * @param <T> 类型
   * @return 返回Http结果
   */
  public static <T> HttpResult<T> success() {
    return success(null);
  }

  /**
   * 成功
   *
   * @param data 数据
   * @param <T>  类型
   * @return 返回Http结果
   */
  public static <T> HttpResult<T> success(T data) {
    return create(CommonStatus.OK, data);
  }

  /**
   * 成功
   *
   * @param code 结果码
   * @param msg  消息
   * @param <T>  类型
   * @return 返回Http结果
   */
  public static <T> HttpResult<T> success(int code, String msg) {
    return create(code, msg, null);
  }

  /**
   * 失败
   *
   * @param <T> 类型
   * @return 返回Http结果
   */
  public static <T> HttpResult<T> failure() {
    return failure(null);
  }

  /**
   * 失败
   *
   * @param data 数据
   * @param <T>  类型
   * @return 返回Http结果
   */
  public static <T> HttpResult<T> failure(T data) {
    return create(CommonStatus.BAD_REQUEST, data);
  }

  /**
   * 失败
   *
   * @param code 结果码
   * @param msg  消息
   * @param <T>  类型
   * @return 返回Http结果
   */
  public static <T> HttpResult<T> failure(int code, String msg) {
    return create(code, msg, null);
  }

  /**
   * 创建HttpResult
   *
   * @param status 状态
   * @param <T>    类型
   * @return 返回结果
   */
  public static <T> HttpResult<T> create(CommonStatus status) {
    return create(status, null);
  }

  /**
   * 创建HttpResult
   *
   * @param status 状态
   * @param data   数据
   * @param <T>    类型
   * @return 返回结果
   */
  public static <T> HttpResult<T> create(CommonStatus status, T data) {
    return create(status.getCode(), status.getResponse(), data);
  }

  /**
   * 创建HttpResult
   *
   * @param code 状态码
   * @param msg  消息
   * @param <T>  类型
   * @return 返回结果
   */
  public static <T> HttpResult<T> create(int code, String msg) {
    return create(code, msg, null);
  }

  /**
   * 创建HttpResult
   *
   * @param code 状态码
   * @param msg  消息
   * @param data 数据
   * @param <T>  类型
   * @return 返回结果
   */
  public static <T> HttpResult<T> create(int code, String msg, T data) {
    return new Builder<T>().setCode(code).setMsg(msg).setData(data).build();
  }

  /**
   * 状态码
   */
  private int code;
  /**
   * 信息
   */
  private String msg;
  /**
   * 数据
   */
  private T data;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return super.toString();
  }

  public static final class Builder<T> {
    private int code;
    private String msg;
    private T data;

    public Builder() {
    }

    public Builder<T> setCode(int code) {
      this.code = code;
      return this;
    }

    public Builder<T> setMsg(String msg) {
      this.msg = msg;
      return this;
    }

    public Builder<T> setData(T data) {
      this.data = data;
      return this;
    }

    public HttpResult<T> build() {
      HttpResult<T> result = new HttpResult<>();
      result.setCode(code);
      result.setMsg(msg);
      result.setData(data);
      return result;
    }
  }
}
