package com.pp.reservo.domain.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ClientDTO extends BaseDTO {
    private String name;

    public ClientDTO() {

    }

    @NotNull
    @NotEmpty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
