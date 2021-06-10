package com.pp.reservo.domain.dto.event.employee;

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
public class EmployeeCreatedDataEventDTO implements BaseDataEventDTO {
    String name;

    @Override
    @JsonIgnore
    public String getEventType() {
        return "com.pp.reservo.employeeCreated";
    }

    @Override
    @JsonIgnore
    public String getEventSource() {
        return "/api/employees";
    }
}
