package dev.demon.xan.base.event.events;

import dev.demon.xan.base.event.AnticheatEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionEvent extends AnticheatEvent {

    private int id;
    private short action;
    private boolean accept;

    public TransactionEvent(int id, short action, boolean accept) {
        this.id = id;
        this.action = action;
        this.accept = accept;
    }
}
