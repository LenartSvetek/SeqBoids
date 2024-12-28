package util.math.quaterion;

public class AxisAligned {
    public float x, y, z;
    public double angle;
    AxisAligned(float x, float y, float z, double angle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + Math.toDegrees(angle) + ")";
    }
}
