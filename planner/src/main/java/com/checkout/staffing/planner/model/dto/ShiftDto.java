/* (C) 2025 */
package com.checkout.staffing.planner.model.dto;

import com.checkout.staffing.planner.model.entity.Shift;
import com.checkout.staffing.planner.model.enums.ShiftType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftDto {

  private String employeeEmail;

  private LocalDate date;

  private ShiftType type;

  public ShiftDto(Shift shift) {
    this.employeeEmail = shift.getEmployee().getEmail();
    this.date = shift.getDate();
    this.type = shift.getShiftType();
  }
}
