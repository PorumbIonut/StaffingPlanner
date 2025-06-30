/* (C) 2025 */
package com.checkout.staffing.planner.model.entity;

import com.checkout.staffing.planner.model.dto.CreateUserDto;
import com.checkout.staffing.planner.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private UserRole role;

  public User(CreateUserDto createUserDto) {
    this.firstName = createUserDto.getFirstName();
    this.lastName = createUserDto.getLastName();
    this.email = createUserDto.getEmail();
    this.phoneNumber = createUserDto.getPhoneNumber();
    this.role = createUserDto.getRole();
  }
}
