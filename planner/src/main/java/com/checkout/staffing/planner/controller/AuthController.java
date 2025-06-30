/* (C) 2025 */
package com.checkout.staffing.planner.controller;

import com.checkout.staffing.planner.model.dto.AuthRequest;
import com.checkout.staffing.planner.model.dto.CreateUserDto;
import com.checkout.staffing.planner.model.dto.JwtTokensDto;
import com.checkout.staffing.planner.model.dto.UserDto;
import com.checkout.staffing.planner.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping(value = "/register")
  public UserDto register(@RequestBody CreateUserDto createUserDto) {
    return authService.register(createUserDto);
  }

  @PostMapping(
      value = "/login",
      consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public ResponseEntity<JwtTokensDto> login(AuthRequest loginDto) {
    JwtTokensDto authResponse = authService.login(loginDto);
    return ResponseEntity.ok(authResponse);
  }

  @PostMapping(
      value = "/refresh",
      consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public ResponseEntity<JwtTokensDto> refresh(HttpServletRequest request) {
    JwtTokensDto authResponse = authService.refresh(request);
    return ResponseEntity.ok(authResponse);
  }
}
