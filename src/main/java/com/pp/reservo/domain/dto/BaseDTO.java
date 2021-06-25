package com.pp.reservo.domain.dto;

public abstract class BaseDTO {

    private Integer id;

    protected BaseDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
