package com.pp.reservo.domain.commands.clients;

import com.pp.reservo.domain.common.Command;
import com.pp.reservo.domain.dto.ClientDTO;
import com.pp.reservo.infrastructure.services.ClientService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Getter
@Setter
@Service("updateClientCommand")
public class UpdateClientCommand implements Command {

    private final ClientService clientService;
    private ClientDTO clientDto;

    public UpdateClientCommand(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void execute() {
        this.clientService.storeClient(clientDto);
    }
}
