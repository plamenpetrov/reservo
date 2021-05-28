package com.pp.reservo.domain.dto;

public abstract class BaseDTO {

    private String id;

    protected BaseDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
