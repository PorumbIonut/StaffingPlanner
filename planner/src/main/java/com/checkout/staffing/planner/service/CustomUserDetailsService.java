/* (C) 2025 */
package com.checkout.staffing.planner.service;

import com.checkout.staffing.planner.model.entity.User;
import com.checkout.staffing.planner.repository.UserRepository;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username).orElse(null);
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
    return new org.springframework.security.core.userdetails.User(
        user.getEmail(), user.getPassword(), getAuthority(user));
  }

  private Collection<? extends GrantedAuthority> getAuthority(User user) {
    GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
    return List.of(authority);
  }
}
