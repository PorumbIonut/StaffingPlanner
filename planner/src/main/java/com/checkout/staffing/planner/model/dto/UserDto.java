/* (C) 2025 */
package com.checkout.staffing.planner.model.dto;

import com.checkout.staffing.planner.model.entity.User;
import com.checkout.staffing.planner.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private Long id;

  private String firstName;

  private String lastName;

  private String email;

  private UserRole role;

  private String phoneNumber;

  public UserDto(User user) {
    this.id = user.getId();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.email = user.getEmail();
    this.role = user.getRole();
    this.phoneNumber = user.getPhoneNumber();
  }
}
