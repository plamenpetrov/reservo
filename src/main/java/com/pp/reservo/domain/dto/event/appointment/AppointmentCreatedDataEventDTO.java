package com.pp.reservo.domain.dto.event.appointment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pp.reservo.domain.dto.event.BaseDataEventDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreatedDataEventDTO implements BaseDataEventDTO {
    String name;
    Integer duration;

    @Override
    @JsonIgnore
    public String getEventType() {
        return "com.pp.reservo.appointmentCreated";
    }

    @Override
    @JsonIgnore
    public String getEventSource() {
        return "/api/appointments";
    }
}
