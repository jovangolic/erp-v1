package com.jovan.erp_v1.response;

import java.time.LocalDate;

import com.jovan.erp_v1.enumeration.ShiftType;
import com.jovan.erp_v1.model.ShiftPlanning;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftPlanningResponse {

    private Long id;
    private WorkCenterResponse workCenterResponse;
    private UserResponse userResponse;
    private LocalDate date;
    private ShiftType shiftType;
    private boolean assigned;

    public ShiftPlanningResponse(ShiftPlanning shift) {
        this.id = shift.getId();
        this.workCenterResponse = new WorkCenterResponse(shift.getWorkCenter());
        this.userResponse = new UserResponse(shift.getEmployee());
        this.date = shift.getDate();
        this.shiftType = shift.getShiftType();
        this.assigned = shift.isAssigned();
    }
}
