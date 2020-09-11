package dev.demon.xan.base.event.impl;

import dev.demon.xan.base.event.AnticheatEvent;
import dev.demon.xan.base.event.Cancellable;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ServerShutdownEvent extends AnticheatEvent implements Cancellable {

    @Setter
    private Object packet;

    @Setter
    private boolean cancelled;



    public ServerShutdownEvent() {
    }
}
