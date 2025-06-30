/* (C) 2025 */
package com.checkout.staffing.planner.service;

import com.checkout.staffing.planner.model.dto.*;
import com.checkout.staffing.planner.model.entity.User;
import com.checkout.staffing.planner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  private final JwtService jwtService;

  private final UserDetailsService userDetailsService;

  public UserDto register(CreateUserDto createUserDto) {

    // check email unique
    User existingUserWithEmail = userRepository.findByEmail(createUserDto.getEmail()).orElse(null);
    if (existingUserWithEmail != null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
    User newUser = new User(createUserDto);
    newUser.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
    newUser = userRepository.save(newUser);
    return new UserDto(newUser);
  }

  public JwtTokensDto login(AuthRequest authRequest) {

    // authenticate
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), authRequest.getPassword()));

    // set authentication
    SecurityContextHolder.getContext().setAuthentication(authentication);
    // generate tokens
    return jwtService.generateTokenPair(authentication);
  }

  public JwtTokensDto refresh(RefreshTokenRequestDto refreshTokenRequest) {
    if (!jwtService.isRefreshToken(refreshTokenRequest.getRefreshToken())) {
      throw new IllegalArgumentException("Invalid refresh token");
    }

    String user = jwtService.extractUsernameFromToken(refreshTokenRequest.getRefreshToken());
    UserDetails userDetails = userDetailsService.loadUserByUsername(user);
    if (userDetails == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    String accessToken = jwtService.generateAccessToken(authenticationToken);
    return new JwtTokensDto(accessToken, refreshTokenRequest.getRefreshToken());
  }
}
