package com.pp.reservo.domain.dto.event.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentUpdatedDataEventDTO {
    Integer id;
    String name;
    Integer duration;
}
