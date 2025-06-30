/* (C) 2025 */
package com.checkout.staffing.planner.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

  private String errorCode;

  private String errorMessage;

  private String errorDescription;
}
