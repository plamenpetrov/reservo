package com.pp.reservo.domain.dto.event.client;

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
public class ClientCreatedDataEventDTO implements BaseDataEventDTO {
    String name;

    @Override
    @JsonIgnore
    public String getEventType() {
        return "com.pp.reservo.clientCreated";
    }

    @Override
    @JsonIgnore
    public String getEventSource() {
        return "/api/clients";
    }
}
