package dev.demon.xan.utils.time;


import dev.demon.xan.Xan;

public class TickTimer {
    private int ticks = Xan.getInstance().getCurrentTicks(), defaultPassed;

    public TickTimer(int defaultPassed) {
        this.defaultPassed = defaultPassed;
    }

    public void reset() {
        ticks = Xan.getInstance().getCurrentTicks();
    }

    public boolean hasPassed() {
        return Xan.getInstance().getCurrentTicks() - ticks > defaultPassed;
    }

    public boolean hasPassed(int amount) {
        return Xan.getInstance().getCurrentTicks() - ticks > amount;
    }

    public boolean hasNotPassed() {
        return Xan.getInstance().getCurrentTicks() - ticks <= defaultPassed;
    }

    public boolean hasNotPassed(int amount) {
        return Xan.getInstance().getCurrentTicks() - ticks <= amount;
    }

    public int getPassed() {
        return Xan.getInstance().getCurrentTicks() - ticks;
    }
}