/* (C) 2025 */
package com.checkout.staffing.planner.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCodes {
  BAD_REQUEST_USER_NOT_FOUND("404-1", "Bad request - User not found"),
  BAD_REQUEST_USER_EXISTS("400-1", "Bad request - User with this email already exists"),
  INTERNAL_SERVER_ERROR("500-1", "There was an error processing your request. Please try again"),
  UNAUTHORIZED("401-1", "Unauthorized - Unauthorized to access this resource"),
  UNAUTHORIZED_BAD_CREDENTIALS("401-2", "Unauthorized - Bad credentials!"),
  UNAUTHORIZED_TOKEN_EXPIRED("401-3", "Unauthorized - Access token expired!");

  private final String value;

  private final String message;
}
