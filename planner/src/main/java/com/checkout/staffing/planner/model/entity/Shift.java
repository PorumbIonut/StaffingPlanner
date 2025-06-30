/* (C) 2025 */
package com.checkout.staffing.planner.model.entity;

import com.checkout.staffing.planner.model.enums.ShiftType;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shift")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Shift {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne private User employee;

  private LocalDate date;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private ShiftType shiftType;

  public Shift(User employee, LocalDate date, ShiftType shiftType) {
    this.employee = employee;
    this.date = date;
    this.shiftType = shiftType;
  }
}
