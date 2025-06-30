/* (C) 2025 */
package com.checkout.staffing.planner.repository;

import com.checkout.staffing.planner.model.entity.Shift;
import com.checkout.staffing.planner.model.enums.UserRole;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

  List<Shift> findAllByDateAfter(LocalDate now);

  List<Shift> findAllByDateAfterAndEmployee_Role(LocalDate now, UserRole userRole);
}
