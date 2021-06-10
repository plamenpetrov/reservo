package com.pp.reservo.domain.commands.clients;

import com.pp.reservo.domain.common.Command;
import com.pp.reservo.infrastructure.services.ClientService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Getter
@Setter
@Service("deleteClientCommand")
public class DeleteClientCommand implements Command {

    private final ClientService clientService;
    private Integer clientId;


    public DeleteClientCommand(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void execute() {
        this.clientService.deleteClient(clientId);
    }
}
