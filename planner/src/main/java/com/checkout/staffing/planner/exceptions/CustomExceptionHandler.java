/* (C) 2025 */
package com.checkout.staffing.planner.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(CustomErrorResponse.class)
  public ResponseEntity<Object> handleCustomErrorResponse(CustomErrorResponse customErrorResponse) {
    return buildErrorResponse(customErrorResponse);
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleException(RuntimeException exception) {
    return buildErrorResponse(new CustomErrorResponse(ErrorCodes.INTERNAL_SERVER_ERROR));
  }

  private ResponseEntity<Object> buildErrorResponse(CustomErrorResponse customErrorResponse) {
    HttpStatus status =
        HttpStatus.valueOf(Integer.parseInt(customErrorResponse.getCode().split("-")[0]));
    return new ResponseEntity<>(
        new ErrorResponse(
            customErrorResponse.getCode(),
            customErrorResponse.getMessage(),
            customErrorResponse.getDescription()),
        status);
  }
}
