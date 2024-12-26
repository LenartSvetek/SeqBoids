package util.octree;

import util.Logger;

import java.util.ArrayList;
import java.util.Collections;

public class Octree<T> {
    OctPoint topFrontRight = null;
    OctPoint bottomBackLeft = null;

    ArrayList<Octree<T>> octs = null;

    OctPoint point = null;
    T object = null;

    Octree() {
        point = new OctPoint();
    }

    Octree(int x, int y, int z, T object) {
        point = new OctPoint(x, y, z);
        this.object = object;
    }

    public Octree(int xl, int yl, int zl, int xh, int yh, int zh) {
        topFrontRight = new OctPoint(xh, yh, zh);
        bottomBackLeft = new OctPoint(xl, yl, zl);

        octs = new ArrayList<Octree<T>>(Collections.nCopies(8, new Octree<T>()));
        point = null;
    }

    public void insert(int x, int y, int z, T object) {
        if(topFrontRight.isInBoundsBigger(x, y, z) || bottomBackLeft.isInBoundsSmaller(x, y, z)) return;

        OctPoint midPoint = OctPoint.getMidPoint(bottomBackLeft, topFrontRight);
        int oct = getOct(midPoint, x, y, z);

        Octree<T> _octree = octs.get(oct);

        if(_octree.point == null) {
            _octree.insert(x, y, z, object);
            return;
        }
        else if(_octree.point.isNull) {
            octs.set(oct, new Octree<T>(x, y, z, object));
            return;
        }
        else {
            int _x = _octree.point.x;
            int _y = _octree.point.y;
            int _z = _octree.point.z;
            T _object = _octree.object;

            switch (oct) {
                case OctLocation.TOP_FRONT_RIGHT -> octs.set(oct, new Octree<T>(midPoint.x, midPoint.y, midPoint.z, topFrontRight.x, topFrontRight.y, topFrontRight.z));
                case OctLocation.TOP_FRONT_LEFT -> octs.set(oct, new Octree<T>(bottomBackLeft.x, midPoint.y, midPoint.z, midPoint.x, topFrontRight.y, topFrontRight.z));
                case OctLocation.TOP_BACK_LEFT -> octs.set(oct, new Octree<T>(bottomBackLeft.x, bottomBackLeft.y, midPoint.z, midPoint.x, midPoint.y, topFrontRight.z));
                case OctLocation.TOP_BACK_RIGHT -> octs.set(oct, new Octree<T>(midPoint.x, bottomBackLeft.y, midPoint.z, topFrontRight.x, midPoint.y, topFrontRight.z));
                case OctLocation.BOTTOM_FRONT_RIGHT -> octs.set(oct, new Octree<T>(midPoint.x, midPoint.y, bottomBackLeft.z, topFrontRight.x, topFrontRight.y, midPoint.z));
                case OctLocation.BOTTOM_FRONT_LEFT -> octs.set(oct, new Octree<T>(bottomBackLeft.x, midPoint.y, bottomBackLeft.z, midPoint.x, topFrontRight.y, midPoint.z));
                case OctLocation.BOTTOM_BACK_LEFT -> octs.set(oct, new Octree<T>(bottomBackLeft.x, bottomBackLeft.y, bottomBackLeft.z, midPoint.x, midPoint.y, midPoint.z));
                case OctLocation.BOTTOM_BACK_RIGHT -> octs.set(oct,new Octree<T>(midPoint.x, bottomBackLeft.y, bottomBackLeft.z, topFrontRight.x, midPoint.y, midPoint.z));
            }

            octs.get(oct).insert(x, y, z, object);
            octs.get(oct).insert(_x, _y, _z, _object);
            return;
        }
    }

    public T get(int x, int y, int z) {
        if(topFrontRight.isInBoundsBigger(x, y, z) || bottomBackLeft.isInBoundsSmaller(x, y, z)) return null;

        OctPoint midPoint = OctPoint.getMidPoint(bottomBackLeft, topFrontRight);
        int oct = getOct(midPoint, x, y, z);

        Octree<T> _octree = octs.get(oct);

        if(_octree.point == null) {
            return _octree.get(x, y, z);
        }
        else if(_octree.point.isNull) {
            return null;
        }
        else if(x == _octree.point.x && y == _octree.point.y && z == _octree.point.z) {
            return _octree.object;
        }
        return null;
    }

    private int getOct(OctPoint midPoint, int x, int y, int z) {
        int oct = OctLocation.IN_BETWEEN;

        if(z >= midPoint.z){
            if(y >= midPoint.y){
                if (x >= midPoint.x) oct = OctLocation.TOP_FRONT_RIGHT;
                else oct = OctLocation.TOP_FRONT_LEFT;
            }
            else {
                if(x >= midPoint.x) oct = OctLocation.TOP_BACK_RIGHT;
                else oct = OctLocation.TOP_BACK_LEFT;
            }
        }
        else {
            if(y >= midPoint.y){
                if (x >= midPoint.x) oct = OctLocation.BOTTOM_FRONT_RIGHT;
                else oct = OctLocation.BOTTOM_FRONT_LEFT;
            }
            else {
                if (x >= midPoint.x) oct = OctLocation.BOTTOM_BACK_RIGHT;
                else oct = OctLocation.BOTTOM_BACK_LEFT;
            }
        }
        return oct;
    }
}
