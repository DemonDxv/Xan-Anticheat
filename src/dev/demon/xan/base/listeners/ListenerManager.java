package dev.demon.xan.base.listeners;

import dev.demon.xan.Xan;
import dev.demon.xan.base.event.AnticheatListener;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {
    private List<AnticheatListener> listenerList = new ArrayList<>();

    public ListenerManager() {
        addListener(new PacketListener());
        setup();
    }

    private void setup() {
        listenerList.forEach(anticheatListener -> Xan.getInstance().getEventManager().registerListeners(anticheatListener, Xan.getInstance()));
    }

    private void addListener(AnticheatListener anticheatListener) {
        if (!listenerList.contains(anticheatListener)) listenerList.add(anticheatListener);
    }
}
