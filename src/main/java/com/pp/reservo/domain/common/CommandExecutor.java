package com.pp.reservo.domain.common;

import org.springframework.stereotype.Component;

@Component
public class CommandExecutor {
    public void executeCommand(Command command) {
        command.execute();
    }
}
