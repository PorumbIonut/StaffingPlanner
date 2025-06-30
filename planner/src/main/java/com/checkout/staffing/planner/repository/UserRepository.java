/* (C) 2025 */
package com.checkout.staffing.planner.repository;

import com.checkout.staffing.planner.model.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String username);
}
