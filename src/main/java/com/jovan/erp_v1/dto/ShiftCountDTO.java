package com.jovan.erp_v1.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftCountDTO {

    private LocalDate date;
    private Long activeShifts;
}
