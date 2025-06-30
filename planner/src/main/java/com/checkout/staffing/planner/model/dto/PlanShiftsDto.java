/* (C) 2025 */
package com.checkout.staffing.planner.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanShiftsDto extends CreateWishBookDto {

  private String employeeEmail;
}
