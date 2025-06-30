/* (C) 2025 */
package com.checkout.staffing.planner.model.dto;

import com.checkout.staffing.planner.model.enums.ShiftType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateWishBookDto {

  private LocalDate date;

  private ShiftType type;
}
