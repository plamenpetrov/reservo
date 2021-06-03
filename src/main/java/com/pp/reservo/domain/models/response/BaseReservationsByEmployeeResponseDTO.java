package com.pp.reservo.domain.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class BaseReservationsByEmployeeResponseDTO {
    private String employeeName;
    private List<ReservationsByEmployeeResponseDTO> reservations;
}
