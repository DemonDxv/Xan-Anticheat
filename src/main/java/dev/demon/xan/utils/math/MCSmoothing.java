package dev.demon.xan.utils.math;

public class MCSmoothing {

    private float x = 0, y = 0, z = 0;

    public float smooth(float toSmooth, float increment) {
        x += toSmooth;
        toSmooth = (x - y) * increment;
        z += (toSmooth - z) * 0.5f;

        if (toSmooth > 0f && toSmooth > z || toSmooth < 0f && toSmooth < z) {
            toSmooth = z;
        }

        y += toSmooth;
        return toSmooth;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public float getY() {
        return y;
    }

    public void reset() {
        x = 0;
        y = 0;
        z = 0;
    }

}