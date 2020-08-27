package com.benefitj.devtoolsquartz.vo;

/**
 * 常用状态码
 */
public enum CommonStatus {

  /**
   * 200 OK 服务器返回用户请求的数据，该操作是幂等的
   */
  OK(200, "OK", "Success"),
  /**
   * 201 CREATED 新建或者修改数据成功
   */
  CREATED(201, "Created", "Success"),
  /**
   * 204 NO CONTENT 删除数据成功
   */
  NO_CONTENT(204, "No Content", "Success"),
  /**
   * 400 BAD REQUEST 用户发出的请求有问题，该操作是幂等的
   */
  BAD_REQUEST(400, "Bad Request", "Failure"),
  /**
   * 401 UNAUTHORIED 表示用户没有认证，无法进行操作
   */
  UNAUTHORIED(401, "Unauthoried", "Failure"),
  /**
   * 403 FORBIDDEN 用户访问是被禁止的
   */
  FORBIDDEN(403, "Forbidden", "Failure"),
  /**
   * 422 Unprocesable Entity 当创建一个对象时，发生一个验证错误
   */
  UNPROCESABLE_ENTITY(422, "Unprocesable Entity", "Failure"),
  /**
   * 500 INTERNAL SERVER ERROR 服务器内部错误，用户将无法判断发出的请求是否成功
   */
  INTERNAL_SERVER_ERROR(500, "Internal Server Error", "Internal Server Error"),
  /**
   * 503 Service Unavailable 服务不可用状态，多半是因为服务器问题，例如CPU占用率大，等等
   */
  SERVICE_UNAVAILABLE(503, "Service Unavailable", "Internal Server Error");


  private final int code;
  private final String reasonPhrase;
  private final String response;

  CommonStatus(int code, String reasonPhrase, String response) {
    this.code = code;
    this.reasonPhrase = reasonPhrase;
    this.response = response;
  }

  public int getCode() {
    return code;
  }

  public String getReasonPhrase() {
    return reasonPhrase;
  }

  public String getResponse() {
    return response;
  }

  /**
   * 判断是否相等
   */
  public boolean isEquals(Object o) {
    if (o == null) {
      return false;
    }
    if (o instanceof CommonStatus) {
      return equals(o);
    }
    if (o instanceof Integer) {
      return getCode() == (Integer) o;
    }
    if (o instanceof String) {
      return ((String) o).equalsIgnoreCase(name());
    }
    return false;
  }

  /**
   * 判断是否为成功的状态
   */
  public boolean isSuccessful() {
    return getCode() / 200 == 1;
  }

  /**
   * 判断是否为请求错误的状态
   */
  public boolean isUnsuccessful() {
    return getCode() / 400 == 1;
  }

  /**
   * 判断是否为服务器错误的状态
   */
  public boolean isServerError() {
    return getCode() / 500 == 1;
  }

  @Override
  public String toString() {
    return super.toString();
  }


}
