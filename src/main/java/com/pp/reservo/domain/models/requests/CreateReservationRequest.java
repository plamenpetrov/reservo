package com.pp.reservo.domain.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pp.reservo.domain.models.requests.common.DatetimeAllowed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateReservationRequest {

    @NotNull(message = "Appointment id is required and can not be empty")
    @Min(1)
    @JsonProperty( "appointment_id")
    private Integer appointmentId;

    @NotNull(message = "Employee id is required and can not be empty")
    @Min(1)
    @JsonProperty( "employee_id")
    private Integer employeeId;

    @NotNull(message = "Client id is required and can not be empty")
    @Min(1)
    @JsonProperty( "client_id")
    private Integer clientId;

    @DatetimeAllowed
    @JsonProperty( "start_at")
    private String startAt;
}
