/* (C) 2025 */
package com.checkout.staffing.planner.exceptions;

import lombok.Getter;

@Getter
public class CustomErrorResponse extends RuntimeException {

  private String code;

  private String message;

  private String description;

  public CustomErrorResponse(ErrorCodes errorCode) {
    super();
    this.code = errorCode.getValue();
    this.message = errorCode.getMessage();
    this.description = "";
  }

  public CustomErrorResponse(ErrorCodes errorCode, String message, String description) {
    super();
    this.code = errorCode.getValue();
    this.message = errorCode.getMessage();
    this.description = description;
  }
}
