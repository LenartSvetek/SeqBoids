package util.math.vector;

public class Vector3 {
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

    public void add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void add(float val) {
        add(val, val, val);
    }

    public void add(Vector3 vec2) {
        add(vec2.x, vec2.y, vec2.z);
    }

    static public Vector3 add(Vector3 vec1, Vector3 vec2) {
        return new Vector3(vec1.x + vec2.x, vec1.y + vec2.y, vec1.z + vec2.z);
    }

    static public Vector3 add(Vector3 vec1, float val) {
        return new Vector3(vec1.x + val, vec1.y + val, vec1.z + val);
    }

    public void sub(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }

    public void sub(float val) {
        sub(val, val, val);
    }

    public void sub(Vector3 vec2) {
        sub(vec2.x, vec2.y, vec2.z);

    }

    static public Vector3 sub(Vector3 vec1, Vector3 vec2) {
        return new Vector3(vec1.x - vec2.x, vec1.y - vec2.y, vec1.z - vec2.z);
    }

    static public Vector3 sub(Vector3 vec1, float val) {
        return new Vector3(vec1.x - val, vec1.y - val, vec1.z - val);
    }

    void mul(float x, float y, float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
    }

    public void mul(float val) {
        mul(val, val, val);
    }

    public void mul(Vector3 vec2) {
        mul(vec2.x, vec2.y, vec2.z);
    }

    static public Vector3 mul(Vector3 vec1, Vector3 vec2) {
        return new Vector3(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z);
    }

    static public Vector3 mul(Vector3 vec1, float val) {
        return new Vector3(vec1.x * val, vec1.y * val, vec1.z * val);
    }

    public void div(float x, float y, float z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
    }

    public void div(float val) {
        div(val, val, val);
    }

    public void div(Vector3 vec2) {
        div(vec2.x, vec2.y, vec2.z);
    }

    static public Vector3 div(Vector3 vec1, Vector3 vec2) {
        return new Vector3(vec1.x / vec2.x, vec1.y / vec2.y, vec1.z / vec2.z);
    }

    static public Vector3 div(Vector3 vec1, float val) {
        return new Vector3(vec1.x / val, vec1.y / val, vec1.z / val);
    }

    public Vector3 normalized(){
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
