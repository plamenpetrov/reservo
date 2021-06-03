package com.pp.reservo.domain.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ReservationsByClientResponseDTO extends BaseReservationsResponseDTO {
    private String employeeName;
}
