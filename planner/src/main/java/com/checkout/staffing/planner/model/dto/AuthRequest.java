/* (C) 2025 */
package com.checkout.staffing.planner.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

  private String email;

  private String password;
}
