package util.math.quaterion;

public class Quaternion {
    float q1, q2, q3;
    double q0; // in rad
    public Quaternion() {
        q1 = q2 = q3 = 0.0f;
        q0 = 0.0;
    }

    public Quaternion(double q0, float q1, float q2, float q3) {
        this.q0 = q0;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
    }

    public void setFromAxisAngle(float x, float y, float z, double angle) {
        q1 = (float) (x * Math.sin(angle / 2));
        q2 = (float) (y * Math.sin(angle / 2));
        q3 = (float) (z * Math.sin(angle / 2));
        q0 = (Math.cos(angle / 2));
    }

    public AxisAligned getAxisAligned() {
        if(q0 == 1) {
            return new AxisAligned(1, 0, 0, 0);
        }
        double angle = 2 * Math.acos(q0);

        float x = (float)(q1 / Math.sin(angle/2));
        float y = (float)(q2 / Math.sin(angle/2));
        float z = (float)(q3 / Math.sin(angle/2));

        return new AxisAligned(x, y, z, angle);
    }

    @Override
    public String toString() {
        return "(" + Math.toDegrees(q0) + ", " + q1 + ", " + q2 + ", " + q3 + ")";
    }
}
