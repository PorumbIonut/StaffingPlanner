/* (C) 2025 */
package com.checkout.staffing.planner.service;

import com.checkout.staffing.planner.model.dto.JwtTokensDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@PropertySource("application.properties")
public class JwtService {

  @Value("${jwt.secret-key}")
  private String jwtSecret;

  @Value("${jwt.expiration-ms}")
  private long jwtExpirationMs;

  @Value("${jwt.refresh-expiration-ms}")
  private long jwtRefreshExpirationMs;

  public JwtTokensDto generateTokenPair(Authentication authentication) {
    String accessToken = generateAccessToken(authentication);
    String refreshToken = generateRefreshToken(authentication);
    return new JwtTokensDto(accessToken, refreshToken);
  }

  public boolean validateTokenForUser(String token, UserDetails userDetails) {
    final String username = extractUsernameFromToken(token);

    return username != null && username.equals(userDetails.getUsername());
  }

  public boolean isValidToken(String token) {
    return extractAllClaims(token) != null;
  }

  public boolean isRefreshToken(String token) {
    Claims claims = extractAllClaims(token);

    return claims != null && "refresh".equals(claims.get("token_type"));
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
  }

  public String extractUsernameFromToken(String token) {
    Claims claims = extractAllClaims(token);
    if (claims != null) {
      return claims.getSubject();
    }
    return null;
  }

  public String generateAccessToken(Authentication authentication) {
    Map<String, String> claims = new HashMap<>();
    claims.put("roles", authentication.getAuthorities().toString());
    return generateToken(authentication, jwtExpirationMs, claims);
  }

  private String generateRefreshToken(Authentication authentication) {
    Map<String, String> claims = new HashMap<>();
    claims.put("roles", authentication.getAuthorities().toString());
    claims.put("token_type", "refresh");

    return generateToken(authentication, jwtRefreshExpirationMs, claims);
  }

  private String generateToken(
      Authentication authentication, long expirationMs, Map<String, String> claims) {
    UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

    Date now = new Date(); // Time of token creation
    Date expiryDate = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
        .header()
        .add("typ", "JWT")
        .and()
        .subject(userPrincipal.getUsername())
        .claims(claims)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSignInKey())
        .compact();
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64URL.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String extractAccessTokenFromRequest(HttpServletRequest request) {
    final String authHeader = request.getHeader("Authorization");
    return authHeader.substring(7);
  }
}
