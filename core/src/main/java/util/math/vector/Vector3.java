package util.math.vector;

public class Vector3 {
    static public final Vector3 UP = new Vector3(0, 0, 1);
    static public final Vector3 FORWARD = new Vector3(0, 1, 0);
    static public final Vector3 RIGHT = new Vector3(1, 0, 0);
    float x;
    float y;
    float z;
    public Vector3() {
        x = y = z = 0;
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getSize() {
        return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    public float get(int i) throws IndexOutOfBoundsException {
        return switch (i) {
            case 0 -> x;
            case 1 -> y;
            case 2 -> z;
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Vector3 add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3 sum(float val) {
        return add(val, val, val);
    }

    public Vector3 sum(Vector3 vec2) {
        return add(vec2.x, vec2.y, vec2.z);
    }

    static public Vector3 sum(Vector3 vec1, Vector3 vec2) {
        return new Vector3(vec1.x + vec2.x, vec1.y + vec2.y, vec1.z + vec2.z);
    }

    static public Vector3 sum(Vector3 vec1, float val) {
        return new Vector3(vec1.x + val, vec1.y + val, vec1.z + val);
    }

    public Vector3 sub(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vector3 sub(float val) {
        return sub(val, val, val);
    }

    public Vector3 sub(Vector3 vec2) {
        return sub(vec2.x, vec2.y, vec2.z);
    }

    static public Vector3 sub(Vector3 vec1, Vector3 vec2) {
        return new Vector3(vec1.x - vec2.x, vec1.y - vec2.y, vec1.z - vec2.z);
    }

    static public Vector3 sub(Vector3 vec1, float val) {
        return new Vector3(vec1.x - val, vec1.y - val, vec1.z - val);
    }

    public Vector3 mul(float x, float y, float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vector3 mul(float val) {
        return mul(val, val, val);
    }

    public Vector3 mul(Vector3 vec2) {
        return mul(vec2.x, vec2.y, vec2.z);
    }

    static public Vector3 mul(Vector3 vec1, Vector3 vec2) {
        return new Vector3(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z);
    }

    static public Vector3 mul(Vector3 vec1, float val) {
        return new Vector3(vec1.x * val, vec1.y * val, vec1.z * val);
    }

    public Vector3 div(float x, float y, float z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }

    public Vector3 div(float val) {
        return div(val, val, val);
    }

    public Vector3 div(Vector3 vec2) {
        return div(vec2.x, vec2.y, vec2.z);
    }

    static public Vector3 div(Vector3 vec1, Vector3 vec2) {
        return new Vector3(vec1.x / vec2.x, vec1.y / vec2.y, vec1.z / vec2.z);
    }

    static public Vector3 div(Vector3 vec1, float val) {
        return new Vector3(vec1.x / val, vec1.y / val, vec1.z / val);
    }

    public Vector3 normalized(){
        float size = getSize();
        if(getSize()  <= 0.01) return this;
        if(getSize() - 1 <= 0.01) return this;
        return div(this, getSize());
    }

    public float dot(Vector3 vec2) {
        return dot(this, vec2);
    }

    static public float dot(Vector3 vec1, Vector3 vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z;
    }

    public Vector3 cross(Vector3 vec2) {
        return cross(this, vec2);
    }

    static public Vector3 cross(Vector3 vec1, Vector3 vec2) {
        return new Vector3(vec1.y * vec2.z - vec1.z * vec2.y, vec1.z * vec2.x - vec1.x * vec2.z, vec1.x * vec2.y - vec1.y * vec2.x);
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
