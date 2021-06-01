package com.pp.reservo.domain.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateEmployeeRequest {
    @NotEmpty(message = "Name is required and can not be empty")
    @Length(min = 2, max = 255, message = "Name should be between 2 and 255 characters")
    @JsonProperty( "name")
    private String name;
}
