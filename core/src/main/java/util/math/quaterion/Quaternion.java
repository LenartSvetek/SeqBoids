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

    /**
     * <p>
     *     roll, pitch, yaw are in degrees
     * </p>
     * @param roll
     * @param pitch
     * @param yaw
     */
    public void setFromEulerAngles(double roll, double pitch, double yaw) {
        roll = Math.toRadians(roll);
        pitch = Math.toRadians(pitch);
        yaw = Math.toRadians(yaw);

        q1 = (float) (Math.sin(roll / 2) * Math.cos(pitch / 2) * Math.cos(yaw / 2) - Math.cos(roll / 2) * Math.sin(pitch / 2) * Math.sin(yaw / 2));
        q2 = (float) (Math.cos(roll / 2) * Math.sin(pitch / 2) * Math.cos(yaw / 2) + Math.sin(roll / 2) * Math.cos(pitch / 2) * Math.sin(yaw / 2));
        q3 = (float) (Math.cos(roll / 2) * Math.cos(pitch / 2) * Math.sin(yaw / 2) - Math.sin(roll / 2) * Math.sin(pitch / 2) * Math.cos(yaw / 2));

        q0 = Math.cos(roll / 2) * Math.cos(pitch / 2) * Math.cos(yaw / 2) + Math.sin(roll / 2) * Math.sin(pitch / 2) * Math.sin(yaw / 2);
        Logger.log("q0: "+q0);

    }

    public Vector3 getFromEulerAngles() {
        double roll, pitch, yaw;


        pitch = Math.asin(2 * (q0 * q2 - q1 * q3));
        if(Math.abs(pitch - (Math.PI / 2)) <= 0.0001) {
            roll = 0.0;
            yaw = -2 * Math.atan2(q1, q0);
        }
        else if(Math.abs(pitch + (Math.PI / 2)) <= 0.0001) {
            roll = 0.0;
            yaw = 2 * Math.atan2(q1, q0);
        }
        else {
            roll = Math.atan2(2 * (q0 * q1 + q2 * q3), Math.pow(q0, 2) - Math.pow(q1, 2) - Math.pow(q2, 2) + Math.pow(q3, 2));
            yaw = Math.atan2(2 * (q0 * q3 + q1 * q2), Math.pow(q0, 2) + Math.pow(q1, 2) - Math.pow(q2, 2) - Math.pow(q3, 2));
        }


        return new Vector3((float)Math.toDegrees(roll),(float)Math.toDegrees(pitch), (float)Math.toDegrees(yaw));
    }

    public static Quaternion lookAt(Vector3 eye, Vector3 target) {
        Quaternion q = new Quaternion();

        // Step 1: Calculate the forward vector (target - eye)
        Vector3 forward = target;  // Direction vector
        forward.sub(eye);
        forward = forward.normalized();

        // Step 2: Calculate the right vector (cross product of up and forward)
        Vector3 right = Vector3.UP.cross(forward).normalized();

        // Step 3: Recalculate the up vector (cross product of forward and right)
        Vector3 upCorrected = forward.cross(right).normalized();

        // Step 4: Calculate the angle of rotation between the forward vector and the up vector
        float dot = forward.dot(upCorrected);  // Dot product of forward and upCorrected
        dot = Math.min(1.0f, Math.max(-1.0f, dot)); // Clamp the dot product to avoid numerical errors

        double angle = Math.acos(dot);  // Angle between forward and upCorrected vectors

        // Step 5: Calculate the axis of rotation (cross product of forward and upCorrected)
        Vector3 axis = forward.cross(upCorrected).normalized();

        q.setFromAxisAngle(axis.getX(), axis.getY(), axis.getZ(), angle);
        return q.normlized();
    }

    static public Quaternion lookAtFromPoints(Vector3 A, Vector3 B) {
        Quaternion q = new Quaternion();

        // Step 1: Compute the direction vector from point A to point B
        Vector3 forward = B;  // Direction from A to B
        forward.sub(A);
        forward = forward.normalized();

        // Step 3: Compute the axis of rotation (cross product of forward and target direction)
        Vector3 axis = Vector3.FORWARD.cross(forward).normalized();

        // Step 4: Compute the angle between the two vectors (dot product)
        double dot = Vector3.FORWARD.dot(forward);
        // Ensure the dot product is within the range [-1, 1] for numerical stability
        dot = Math.max(-1.0, Math.min(1.0, dot));
        double angle = Math.acos(dot);  // Angle between the vectors

        q.setFromAxisAngle(axis.getX(), axis.getY(), axis.getZ(), angle);

        return q.normlized();
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

    public Vector3 activeRotation(Vector3 v) {
        Quaternion q = new Quaternion(0, v.get(0), v.get(1), v.get(2));

        return activeRotation(q, this);
    }

    public static Vector3 activeRotation(Quaternion q, Quaternion rotation) {
        q.q0 = 0;

        Quaternion iQ = rotation.inverse();

        iQ.mul(q);
        iQ.mul(rotation);

        return new Vector3(iQ.q1, iQ.q2, iQ.q3);
    }

    public Vector3 passiveRotation(Vector3 v) {
        Quaternion q = new Quaternion(0, v.get(0), v.get(1), v.get(2));

        return passiveRotation(q, this);
    }

    public static Vector3 passiveRotation(Quaternion q, Quaternion rotation) {
        q.q0 = 0;

        Quaternion iQ = rotation.inverse();

        Quaternion temp = new Quaternion(rotation.q0, rotation.q1, rotation.q2, rotation.q3);

        temp.mul(q);
        temp.mul(iQ);

        return new Vector3(temp.q1, temp.q2, temp.q3);
    }

    public float getSize(){
        return (float)Math.sqrt(Math.pow(q0,2) + Math.pow(q1,2) + Math.pow(q2,2) + Math.pow(q3,2));
    }

    public Quaternion normlized() {
        return normalized(this);
    }

    static public Quaternion normalized(Quaternion q) {
        float size = q.getSize();
        return new Quaternion(q.q0 / size, q.q1 / size, q.q2 / size, q.q3 / size);
    }

    public boolean isUnit(){
        return q0 - 1  <= 1e-6 && q1 <= 1e-6 && q2 <= 1e-6 && q3 <= 1e-6;
    }

    @Override
    public String toString() {
        return "(" + q0 + ", " + q1 + ", " + q2 + ", " + q3 + ")";
    }
}
