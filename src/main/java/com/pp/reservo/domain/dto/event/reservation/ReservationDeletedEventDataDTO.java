package com.pp.reservo.domain.dto.event.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDeletedEventDataDTO {
    private Integer reservationId;
}
