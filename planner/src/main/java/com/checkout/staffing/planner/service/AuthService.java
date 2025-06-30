/* (C) 2025 */
package com.checkout.staffing.planner.service;

import com.checkout.staffing.planner.exceptions.CustomErrorResponse;
import com.checkout.staffing.planner.exceptions.ErrorCodes;
import com.checkout.staffing.planner.model.dto.AuthRequest;
import com.checkout.staffing.planner.model.dto.CreateUserDto;
import com.checkout.staffing.planner.model.dto.JwtTokensDto;
import com.checkout.staffing.planner.model.dto.UserDto;
import com.checkout.staffing.planner.model.entity.User;
import com.checkout.staffing.planner.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
      throw new CustomErrorResponse(ErrorCodes.BAD_REQUEST_USER_EXISTS, "", "");
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

  public JwtTokensDto refresh(HttpServletRequest request) {
    String refreshToken = jwtService.extractRefreshTokenFromRequest(request);
    if (!jwtService.isRefreshToken(refreshToken)) {
      throw new IllegalArgumentException("Invalid refresh token");
    }

    String user = jwtService.extractUsernameFromToken(refreshToken);
    UserDetails userDetails = userDetailsService.loadUserByUsername(user);
    if (userDetails == null) {
      throw new CustomErrorResponse(ErrorCodes.BAD_REQUEST_USER_NOT_FOUND);
    }

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    String accessToken = jwtService.generateAccessToken(authenticationToken);
    return new JwtTokensDto(accessToken, refreshToken);
  }
}
