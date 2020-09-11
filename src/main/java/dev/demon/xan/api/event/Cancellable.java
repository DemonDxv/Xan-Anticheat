package dev.demon.xan.api.event;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean var1);
}

