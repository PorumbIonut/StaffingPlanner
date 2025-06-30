/* (C) 2025 */
package com.checkout.staffing.planner.controller;

import com.checkout.staffing.planner.model.dto.*;
import com.checkout.staffing.planner.service.AuthService;
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
  public ResponseEntity<UserDto> register(@RequestBody CreateUserDto createUserDto) {
    return ResponseEntity.ok(authService.register(createUserDto));
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
  public ResponseEntity<JwtTokensDto> refresh(
      @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
    JwtTokensDto authResponse = authService.refresh(refreshTokenRequestDto);
    return ResponseEntity.ok(authResponse);
  }
}
