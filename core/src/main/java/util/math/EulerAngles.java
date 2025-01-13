package util.math;

import util.math.vector.Vector3;

public class EulerAngles {
    float roll;
    float pitch;
    float yaw;

    public EulerAngles() {
        roll = 0;
        pitch = 0;
        yaw = 0;
    }

    public EulerAngles(float roll, float pitch, float yaw) {
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    static public EulerAngles sum(EulerAngles a, EulerAngles b) {
        return new EulerAngles(a.roll + b.roll, a.pitch + b.pitch, a.yaw + b.yaw);
    }

    public EulerAngles inverse() {
        return inverse(this);
    }

    static public EulerAngles inverse(EulerAngles a) {
        return new EulerAngles(a.roll, -a.pitch, -a.yaw);
    }

    public float getRoll() {
        return roll;
    }

    public float getRollRadians() {
        return (float)Math.toRadians(roll);
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getPitch() {
        return pitch;
    }

    public float getPitchRadians() {
        return (float)Math.toRadians(pitch);
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getYawRadians() {
        return (float)Math.toRadians(yaw);
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    static public EulerAngles getEulerAnglesBetweenTwoPoints(Vector3 a, Vector3 b) {
        EulerAngles angle = new EulerAngles();

        //a = a.normalized();
        //b = b.normalized();

        Vector3 delta = Vector3.sub(a, b).normalized();

        angle.setYaw((float)Math.toDegrees(Math.atan2(delta.getX(), -delta.getY())));

        angle.setPitch(-(float)Math.toDegrees(Math.atan2(delta.getZ(), Math.sqrt(Math.pow(delta.getX(), 2) + Math.pow(delta.getY(), 2)))));

        return angle;
    }
}
