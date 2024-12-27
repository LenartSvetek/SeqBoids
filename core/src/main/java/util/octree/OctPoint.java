package util.octree;

public class OctPoint {
    int x;
    int y;
    int z;

    boolean isNull;

    OctPoint() {
        isNull = true;
    }

    OctPoint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        isNull = false;
    }

    public void set(int i, int value) throws IndexOutOfBoundsException {
        isNull = false;
        switch (i) {
            case 0 -> x = value;
            case 1 -> y = value;
            case 2 -> z = value;
            default -> throw new IndexOutOfBoundsException();
        }
    }

    public int get(int i) throws IndexOutOfBoundsException, NullPointerException {
        if(isNull) throw new NullPointerException();
        return switch (i) {
            case 0 -> x;
            case 1 -> y;
            case 2 -> z;
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public OctPoint getMidPoint(OctPoint p2) {
        return getMidPoint(this, p2);
    }

    static public OctPoint getMidPoint(OctPoint p1, OctPoint p2) {
        return new OctPoint((p1.x + p2.x) / 2, (p1.y + p2.y) / 2, (p1.z + p2.z) / 2);
    }

    public boolean isInBoundsSmaller(int x, int y, int z) {
        return isInBoundsSmaller(x, y, z, this.x, this.y, this.z);
    }

    static public boolean isInBoundsSmaller(int x, int y, int z, int xb, int yb, int zb) {
        return x < xb || y < yb || z < zb;
    }

    public boolean isInBoundsBigger(int x, int y, int z) {
        return isInBoundsBigger(x, y, z, this.x, this.y, this.z);
    }

    static public boolean isInBoundsBigger(int x, int y, int z, int xb, int yb, int zb) {
        return x > xb || y > yb || z > zb;
    }
}
