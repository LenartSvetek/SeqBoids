package util;

public class Rotation {
    float yaw;
    float roll;
    float pitch;

    public Rotation(float yaw, float roll, float pitch) {
        this.yaw = yaw;
        this.roll = roll;
        this.pitch = pitch;
    }

    public void set(int i, int value) throws IndexOutOfBoundsException {
        switch (i) {
            case 0 -> yaw = value;
            case 1 -> roll = value;
            case 2 -> pitch = value;
            default -> throw new IndexOutOfBoundsException();
        }
    }

    public float get(int i) throws IndexOutOfBoundsException {
        return switch (i) {
            case 0 -> yaw;
            case 1 -> roll;
            case 2 -> pitch;
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public void sub(int i, int value) throws IndexOutOfBoundsException {
        add(i, -value);
    }

    public void add(int i, int add) throws IndexOutOfBoundsException {
        switch (i) {
            case 0 -> yaw += add;
            case 1 -> roll += add;
            case 2 -> pitch += add;
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
