/* (C) 2025 */
package com.checkout.staffing.planner.controller;

import com.checkout.staffing.planner.model.dto.CreateWishBookDto;
import com.checkout.staffing.planner.model.dto.MessageDto;
import com.checkout.staffing.planner.model.dto.PlanShiftsDto;
import com.checkout.staffing.planner.model.dto.ShiftDto;
import com.checkout.staffing.planner.service.ShiftService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shifts")
public class ShiftController {

  private final ShiftService shiftService;

  @PostMapping()
  public ResponseEntity<MessageDto> wishBook(
      Authentication authentication, @RequestBody List<CreateWishBookDto> createWishBookDtos) {
    return ResponseEntity.ok(shiftService.addWishBook(authentication, createWishBookDtos));
  }

  @PostMapping("/planning")
  public ResponseEntity<MessageDto> planning(@RequestBody List<PlanShiftsDto> planShiftsDtos) {
    return ResponseEntity.ok(shiftService.planShiftsForEmployees(planShiftsDtos));
  }

  @GetMapping()
  public ResponseEntity<List<ShiftDto>> getSchedule(Authentication authentication) {
    return ResponseEntity.ok(shiftService.getSchedule(authentication));
  }
}
