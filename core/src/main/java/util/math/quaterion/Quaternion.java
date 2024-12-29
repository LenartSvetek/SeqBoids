package util.math.quaterion;

import util.Logger;
import util.math.vector.Vector3;

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

    public Quaternion(Quaternion q) {
        this.q0 = q.q0;
        this.q1 = q.q1;
        this.q2 = q.q2;
        this.q3 = q.q3;
    }

    public void set(double q0, float q1, float q2, float q3) {
        this.q0 = q0;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
    }

    public void set(Quaternion q) {
        set(q.q0, q.q1, q.q2, q.q3);
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

    public void setFromEulerAngles(double roll, double pitch, double yaw) {
        q1 = (float) (Math.sin(roll / 2) * Math.cos(pitch / 2) * Math.cos(yaw / 2) - Math.cos(roll / 2) * Math.sin(pitch / 2) * Math.sin(yaw / 2));
        q2 = (float) (Math.cos(roll / 2) * Math.sin(pitch / 2) * Math.cos(yaw / 2) + Math.sin(roll / 2) * Math.cos(pitch / 2) * Math.sin(yaw / 2));
        q3 = (float) (Math.cos(roll / 2) * Math.cos(pitch / 2) * Math.sin(yaw / 2) - Math.sin(roll / 2) * Math.sin(pitch / 2) * Math.cos(yaw / 2));

        q0 = Math.cos(roll / 2) * Math.cos(pitch / 2) * Math.cos(yaw / 2) + Math.sin(roll / 2) * Math.sin(pitch / 2) * Math.sin(roll / 2);
    }

    public Vector3 getFromEulerAngles() {
        double roll, pitch, yaw;

        roll = Math.atan2(2 * (q0 * q1 + q2 * q3), Math.pow(q0, 2) - Math.pow(q1, 2) - Math.pow(q2, 2) + Math.pow(q3, 2));
        pitch = Math.asin(2 * (q0 * q2 - q1 * q3));
        yaw = Math.atan2(2 * (q0 * q3 + q1 * q2), Math.pow(q0, 2) + Math.pow(q1, 2) - Math.pow(q2, 2) - Math.pow(q3, 2));

        return new Vector3((float)Math.toDegrees(roll),(float)Math.toDegrees(pitch), (float)Math.toDegrees(yaw));
    }

    public void mul(double q0, float q1, float q2, float q3) {
        Quaternion temp = new Quaternion(this);

        this.q0 = (temp.q0 * q0 - temp.q1 * q1 - temp.q2 * q2 - temp.q3 * q3);
        this.q1 = (float)(temp.q0 * q1 + temp.q1 * q0 - temp.q2 * q3 + temp.q3 * q2);
        this.q2 = (float)(temp.q0 * q2 + temp.q1 * q3 + temp.q2 * q0 - temp.q3 * q1);
        this.q3 = (float)(temp.q0 * q3 - temp.q1 * q2 + temp.q2 * q1 + temp.q3 * q0);
    }

    public void mul(Quaternion q) {
        mul(q.q0, q.q1, q.q2, q.q3);
    }

    static public Quaternion mul(Quaternion q0, Quaternion q1) {
        Quaternion q = new Quaternion();

        q.q0 = (q0.q0 * q1.q0 - q0.q1 * q1.q1 - q0.q2 * q1.q2 - q0.q3 * q1.q3);
        q.q1 = (float)(q0.q0 * q1.q1 + q0.q1 * q1.q0 - q0.q2 * q1.q3 + q0.q3 * q1.q2);
        q.q2 = (float)(q0.q0 * q1.q2 + q0.q1 * q1.q3 + q0.q2 * q1.q0 - q0.q3 * q1.q1);
        q.q3 = (float)(q0.q0 * q1.q3 - q0.q1 * q1.q2 + q0.q2 * q1.q1 + q0.q3 * q1.q0);

        return q;
    }

    public Quaternion inverse() {
        return inverse(this);
    }

    static public Quaternion inverse(Quaternion q) {
        return new Quaternion(q.q0, -q.q1, -q.q2, -q.q3);
    }

    public Quaternion activeRotation(Vector3 v) {
        Quaternion q = new Quaternion();
        q.setFromEulerAngles(v.get(0), v.get(1), v.get(2));
        return activeRotation(this, q);
    }

    public static Quaternion activeRotation(Quaternion q, Quaternion rotation) {
        rotation.q0 = 0;

        Quaternion iQ = rotation.inverse();
        Logger.log(rotation + "");
        iQ.mul(q);
        iQ.mul(rotation);
        return iQ;
    }

    public Quaternion passiveRotation(Vector3 v) {
        return activeRotation(this, new Quaternion(0, v.get(0), v.get(1), v.get(2)));
    }

    public static Quaternion passiveRotation(Quaternion q, Quaternion rotation) {
        rotation.q0 = 0;

        Quaternion iQ = rotation.inverse();

        rotation.mul(q);
        rotation.mul(iQ);

        return rotation;
    }

    public float getSize(){
        return (float)Math.sqrt(Math.pow(q0,2) + Math.pow(q1,2) + Math.pow(q2,2) + Math.pow(q3,2));
    }

    public boolean isUnit(){
        return q0 == 1 && q1 == 0 && q2 == 0 && q3 == 0;
    }

    @Override
    public String toString() {
        return "(" + Math.toDegrees(q0) + ", " + q1 + ", " + q2 + ", " + q3 + ")";
    }
}
