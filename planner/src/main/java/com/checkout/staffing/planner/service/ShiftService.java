/* (C) 2025 */
package com.checkout.staffing.planner.service;

import com.checkout.staffing.planner.model.dto.CreateWishBookDto;
import com.checkout.staffing.planner.model.dto.MessageDto;
import com.checkout.staffing.planner.model.dto.PlanShiftsDto;
import com.checkout.staffing.planner.model.dto.ShiftDto;
import com.checkout.staffing.planner.model.entity.Shift;
import com.checkout.staffing.planner.model.entity.User;
import com.checkout.staffing.planner.model.enums.ShiftType;
import com.checkout.staffing.planner.model.enums.UserRole;
import com.checkout.staffing.planner.repository.ShiftRepository;
import com.checkout.staffing.planner.repository.UserRepository;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ShiftService {

  private final ShiftRepository shiftRepository;

  private final UserRepository userRepository;

  public MessageDto addWishBook(
      Authentication authentication, List<CreateWishBookDto> createWishBookDto) {
    // get each desired shift and check if it's valid
    if (createWishBookDto != null && !createWishBookDto.isEmpty()) {
      User employee =
          userRepository
              .findByEmail(authentication.getName())
              .orElseThrow(
                  () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

      // get upcoming shifts
      List<Shift> shifts = shiftRepository.findAllByDateAfter(LocalDate.now(ZoneOffset.UTC));

      createWishBookDto.forEach(
          desiredShift ->
              validateAndAddShift(
                  shifts, employee, desiredShift.getDate(), desiredShift.getType()));
      shiftRepository.saveAll(shifts);
      return new MessageDto("Shifts saved!");
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body");
  }

  public MessageDto planShiftsForEmployees(List<PlanShiftsDto> planShiftsDtos) {
    if (planShiftsDtos != null && !planShiftsDtos.isEmpty()) {

      // filter only valid employee emails
      planShiftsDtos =
          planShiftsDtos.stream()
              .filter(
                  planShiftsDto ->
                      userRepository.findByEmail(planShiftsDto.getEmployeeEmail()).isPresent())
              .toList();
      List<Shift> shifts = shiftRepository.findAllByDateAfter(LocalDate.now(ZoneOffset.UTC));
      planShiftsDtos.forEach(
          planShiftDto -> {
            User employee = userRepository.findByEmail(planShiftDto.getEmployeeEmail()).get();
            validateAndAddShift(shifts, employee, planShiftDto.getDate(), planShiftDto.getType());
          });
      shiftRepository.saveAll(shifts);
      return new MessageDto("Shifts planned!");
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body");
  }

  public List<ShiftDto> getSchedule(Authentication authentication) {
    User user =
        userRepository
            .findByEmail(authentication.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    List<Shift> shifts;
    if (user.getRole().equals(UserRole.ADMIN)) {
      shifts = shiftRepository.findAllByDateAfter(LocalDate.now(ZoneOffset.UTC));
    } else {
      shifts =
          shiftRepository.findAllByDateAfterAndEmployee_Role(
              LocalDate.now(ZoneOffset.UTC), UserRole.EMPLOYEE);
    }
    return shifts.stream().map(ShiftDto::new).toList();
  }

  private void validateAndAddShift(
      List<Shift> shifts, User employee, LocalDate date, ShiftType type) {
    boolean hasShiftForCurrentDate =
        shifts.stream()
            .anyMatch(
                shift ->
                    // Regular shift today
                    (shift.getDate().equals(date)
                            && shift.getEmployee().getId().equals(employee.getId()))
                        ||
                        // Night shift from yesterday
                        (shift.getDate().equals(date.minusDays(1))
                            && shift.getShiftType() == ShiftType.NIGHT
                            && shift.getEmployee().getId().equals(employee.getId())));
    if (!hasShiftForCurrentDate) {
      int noOfExistingShiftsForDesiredDateAndType =
          (int)
              shifts.stream()
                  .filter(
                      shift -> shift.getDate().equals(date) && shift.getShiftType().equals(type))
                  .count();
      if (noOfExistingShiftsForDesiredDateAndType <= 1) {
        shifts.add(new Shift(employee, date, type));
      }
    }
  }
}
