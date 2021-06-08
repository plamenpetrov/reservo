package com.pp.reservo.domain.dto.event.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDeletedDataEventDTO {
    Integer employeeId;
}
