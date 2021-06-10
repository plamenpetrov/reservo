package com.pp.reservo.domain.dto.event.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreatedDataEventDTO {
    private Integer appointmentId;
    private Integer employeeId;
    private Integer clientId;
    private Integer duration;
    private Timestamp startAt;
}
