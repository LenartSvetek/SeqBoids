package util.octree;

import util.math.vector.Vector3;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Tree data structure for saving data in 3D space
 * @param <T> element type
 * @author <a href="https://github.com/LenartSvetek">Lenart Svetek</a>
 */
public class Octree<T> {
    OctPoint topFrontRight;
    OctPoint bottomBackLeft;

    ArrayList<Octree<T>> octs = null;


    // point == null -> branch
    // point.isNull -> prazen
    // point -> leaf
    OctPoint point = null;
    ArrayList<T> objects = null;

    Octree() {
        point = new OctPoint();
    }

    Octree(int x, int y, int z, T object) {
        point = new OctPoint(x, y, z);

        objects = new ArrayList<>(1);
        objects.add(object);
    }

    Octree(int x, int y, int z, ArrayList<T> objects) {
        point = new OctPoint(x, y, z);

        this.objects = objects;
    }

    public Octree(int xl, int yl, int zl, int xh, int yh, int zh) {
        topFrontRight = new OctPoint(xh, yh, zh);
        bottomBackLeft = new OctPoint(xl, yl, zl);

        octs = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            octs.add(new Octree<>());
        }

        point = null;
    }

    public Octree(OctPoint BtmBckLeft, OctPoint TopFntRight) {
        topFrontRight = new OctPoint(TopFntRight.x, TopFntRight.y, TopFntRight.z);
        bottomBackLeft = new OctPoint(BtmBckLeft.x, BtmBckLeft.y, BtmBckLeft.z);

        octs = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            octs.add(new Octree<>());
        }
        point = null;
    }

    public OctPoint GetTopFrontRight() {
        return topFrontRight;
    }

    public OctPoint GetBottomBackLeft() {
        return bottomBackLeft;
    }

    public boolean IsLocationValid(Vector3 location) {
        return !topFrontRight.isInBoundsBigger((int) location.getX(), (int) location.getY(), (int) location.getZ()) && !bottomBackLeft.isInBoundsSmaller((int) location.getX(), (int) location.getY(), (int) location.getZ());
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
            if(x == _octree.point.x && y == _octree.point.y && z == _octree.point.z) {
                _octree.objects.add(object);
                return;
            }

            int _x = _octree.point.x;
            int _y = _octree.point.y;
            int _z = _octree.point.z;
            ArrayList<T> _objects = _octree.objects;

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
            octs.get(oct).insert(_x, _y, _z, _objects);
            return;
        }

    }

    private void insert(int x, int y, int z, ArrayList<T> objects) {
        if(topFrontRight.isInBoundsBigger(x, y, z) || bottomBackLeft.isInBoundsSmaller(x, y, z)) return;
        if(objects == null) return;

        OctPoint midPoint = OctPoint.getMidPoint(bottomBackLeft, topFrontRight);
        int oct = getOct(midPoint, x, y, z);

        Octree<T> _octree = octs.get(oct);

        if(_octree.point == null) {
            _octree.insert(x, y, z, objects);
            return;
        }
        else if(_octree.point.isNull) {
                octs.set(oct, new Octree<T>(x, y, z, objects));
                return;
        }
        else {
            if(x == _octree.point.x && y == _octree.point.y && z == _octree.point.z) {

                _octree.objects.addAll(objects);
                return;
            }

            int _x = _octree.point.x;
            int _y = _octree.point.y;
            int _z = _octree.point.z;
            ArrayList<T> _objects = _octree.objects;

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

            octs.get(oct).insert(x, y, z, objects);
            octs.get(oct).insert(_x, _y, _z, _objects);
            return;
        }

    }

    public void insert(Octree<T> octree) {
        if(point == null) {
            for (int i = 0; i < octs.size(); i++) {
                OctPoint pt = octs.get(i).point;
                if (pt != null && !pt.isNull) {
                    insert(pt.x, pt.y, pt.z, octree.octs.get(i).objects);
                }
                if (pt != null && pt.isNull) {
                    octs.set(i, octree.octs.get(i));
                } else {
                    OctPoint pt2 = octree.octs.get(i).point;
                    if(pt2 != null && !pt2.isNull) {
                        octree.octs.get(i).insert(pt2.x, pt2.y, pt2.z, octree.octs.get(i).objects);
                        continue;
                    }
                    if(pt2 != null) continue;
//                    octree.octs.get(i).insert(octs.get(i));
                }
            }
        }
        else if(point.isNull) {
            point = octree.point;
            topFrontRight = octree.topFrontRight;
            bottomBackLeft = octree.bottomBackLeft;

            octs = octree.octs;

            return;

        }
        else {
            if(octree.point != null && !octree.point.isNull && octree.point.x == point.x && octree.point.y == point.y && octree.point.z == point.z) {
                objects.addAll(octree.objects);
                return;
            }
            else if(octree.point != null && !octree.point.isNull) {
                int _x = point.x;
                int _y = point.y;
                int _z = point.z;
                ArrayList<T> _objects = objects;

                OctPoint midPoint = OctPoint.getMidPoint(octree.bottomBackLeft, octree.topFrontRight);
                int oct = getOct(midPoint, _x, _y, _z);

                switch (oct) {
                    case OctLocation.TOP_FRONT_RIGHT ->
                        octs.set(oct, new Octree<T>(midPoint.x, midPoint.y, midPoint.z, topFrontRight.x, topFrontRight.y, topFrontRight.z));
                    case OctLocation.TOP_FRONT_LEFT ->
                        octs.set(oct, new Octree<T>(bottomBackLeft.x, midPoint.y, midPoint.z, midPoint.x, topFrontRight.y, topFrontRight.z));
                    case OctLocation.TOP_BACK_LEFT ->
                        octs.set(oct, new Octree<T>(bottomBackLeft.x, bottomBackLeft.y, midPoint.z, midPoint.x, midPoint.y, topFrontRight.z));
                    case OctLocation.TOP_BACK_RIGHT ->
                        octs.set(oct, new Octree<T>(midPoint.x, bottomBackLeft.y, midPoint.z, topFrontRight.x, midPoint.y, topFrontRight.z));
                    case OctLocation.BOTTOM_FRONT_RIGHT ->
                        octs.set(oct, new Octree<T>(midPoint.x, midPoint.y, bottomBackLeft.z, topFrontRight.x, topFrontRight.y, midPoint.z));
                    case OctLocation.BOTTOM_FRONT_LEFT ->
                        octs.set(oct, new Octree<T>(bottomBackLeft.x, midPoint.y, bottomBackLeft.z, midPoint.x, topFrontRight.y, midPoint.z));
                    case OctLocation.BOTTOM_BACK_LEFT ->
                        octs.set(oct, new Octree<T>(bottomBackLeft.x, bottomBackLeft.y, bottomBackLeft.z, midPoint.x, midPoint.y, midPoint.z));
                    case OctLocation.BOTTOM_BACK_RIGHT ->
                        octs.set(oct, new Octree<T>(midPoint.x, bottomBackLeft.y, bottomBackLeft.z, topFrontRight.x, midPoint.y, midPoint.z));
                }

                int _ox = octree.point.x;
                int _oy = point.y;
                int _oz = point.z;
                ArrayList<T> _oobjects = objects;

                OctPoint omidPoint = OctPoint.getMidPoint(bottomBackLeft, topFrontRight);
                int ooct = getOct(omidPoint, _ox, _oy, _oz);
                if(ooct != oct)
                    switch (ooct) {
                        case OctLocation.TOP_FRONT_RIGHT ->
                            octs.set(oct, new Octree<T>(midPoint.x, midPoint.y, midPoint.z, topFrontRight.x, topFrontRight.y, topFrontRight.z));
                        case OctLocation.TOP_FRONT_LEFT ->
                            octs.set(oct, new Octree<T>(bottomBackLeft.x, midPoint.y, midPoint.z, midPoint.x, topFrontRight.y, topFrontRight.z));
                        case OctLocation.TOP_BACK_LEFT ->
                            octs.set(oct, new Octree<T>(bottomBackLeft.x, bottomBackLeft.y, midPoint.z, midPoint.x, midPoint.y, topFrontRight.z));
                        case OctLocation.TOP_BACK_RIGHT ->
                            octs.set(oct, new Octree<T>(midPoint.x, bottomBackLeft.y, midPoint.z, topFrontRight.x, midPoint.y, topFrontRight.z));
                        case OctLocation.BOTTOM_FRONT_RIGHT ->
                            octs.set(oct, new Octree<T>(midPoint.x, midPoint.y, bottomBackLeft.z, topFrontRight.x, topFrontRight.y, midPoint.z));
                        case OctLocation.BOTTOM_FRONT_LEFT ->
                            octs.set(oct, new Octree<T>(bottomBackLeft.x, midPoint.y, bottomBackLeft.z, midPoint.x, topFrontRight.y, midPoint.z));
                        case OctLocation.BOTTOM_BACK_LEFT ->
                            octs.set(oct, new Octree<T>(bottomBackLeft.x, bottomBackLeft.y, bottomBackLeft.z, midPoint.x, midPoint.y, midPoint.z));
                        case OctLocation.BOTTOM_BACK_RIGHT ->
                            octs.set(oct, new Octree<T>(midPoint.x, bottomBackLeft.y, bottomBackLeft.z, topFrontRight.x, midPoint.y, midPoint.z));
                    }

                octs.get(oct).insert(_x, _y, _z, _objects);
                octs.get(ooct).insert(_ox, _oy, _oz, _oobjects);

                point = null;
                topFrontRight = octree.topFrontRight;
                bottomBackLeft = octree.bottomBackLeft;

            } else if (octree.point != null) {
                return;
            } else {
                int _x = point.x;
                int _y = point.y;
                int _z = point.z;
                ArrayList<T> _objects = objects;

                OctPoint midPoint = OctPoint.getMidPoint(octree.bottomBackLeft, octree.topFrontRight);
                int oct = getOct(midPoint, _x, _y, _z);

                if(octree.octs.get(oct) != null && octree.octs.get(oct).point.isNull)
                    switch (oct) {
                        case OctLocation.TOP_FRONT_RIGHT ->
                            octs.set(oct, new Octree<T>(midPoint.x, midPoint.y, midPoint.z, topFrontRight.x, topFrontRight.y, topFrontRight.z));
                        case OctLocation.TOP_FRONT_LEFT ->
                            octs.set(oct, new Octree<T>(bottomBackLeft.x, midPoint.y, midPoint.z, midPoint.x, topFrontRight.y, topFrontRight.z));
                        case OctLocation.TOP_BACK_LEFT ->
                            octs.set(oct, new Octree<T>(bottomBackLeft.x, bottomBackLeft.y, midPoint.z, midPoint.x, midPoint.y, topFrontRight.z));
                        case OctLocation.TOP_BACK_RIGHT ->
                            octs.set(oct, new Octree<T>(midPoint.x, bottomBackLeft.y, midPoint.z, topFrontRight.x, midPoint.y, topFrontRight.z));
                        case OctLocation.BOTTOM_FRONT_RIGHT ->
                            octs.set(oct, new Octree<T>(midPoint.x, midPoint.y, bottomBackLeft.z, topFrontRight.x, topFrontRight.y, midPoint.z));
                        case OctLocation.BOTTOM_FRONT_LEFT ->
                            octs.set(oct, new Octree<T>(bottomBackLeft.x, midPoint.y, bottomBackLeft.z, midPoint.x, topFrontRight.y, midPoint.z));
                        case OctLocation.BOTTOM_BACK_LEFT ->
                            octs.set(oct, new Octree<T>(bottomBackLeft.x, bottomBackLeft.y, bottomBackLeft.z, midPoint.x, midPoint.y, midPoint.z));
                        case OctLocation.BOTTOM_BACK_RIGHT ->
                            octs.set(oct, new Octree<T>(midPoint.x, bottomBackLeft.y, bottomBackLeft.z, topFrontRight.x, midPoint.y, midPoint.z));
                    }

                point = null;
                topFrontRight = octree.topFrontRight;
                bottomBackLeft = octree.bottomBackLeft;
                octree.octs.get(oct).insert(_x, _y, _z, _objects);
                octs = octree.octs;
            }
            return;
        }
    }

    public ArrayList<T> get(int x, int y, int z) {
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
            return _octree.objects;
        }
        return null;
    }

    public ArrayList<T> getNeighbors(int x, int y, int z, int r) {
        OctPoint radiusCenter = new OctPoint(x, y, z);
        ArrayList<T> neighbours = new ArrayList<T>();
        for (Octree<T> _oct : octs) {
            OctPoint closest = new OctPoint();

            for(int i = 0; i < 3; i++){
                if(radiusCenter.get(i) < bottomBackLeft.get(i)) closest.set(i, bottomBackLeft.get(i));
                else if (radiusCenter.get(i) < topFrontRight.get(i)) closest.set(i, radiusCenter.get(i));
                else closest.set(i, topFrontRight.get(i));
            }

            int dx = closest.get(0) - radiusCenter.get(0);
            int dy = closest.get(1) - radiusCenter.get(1);
            int dz = closest.get(2) - radiusCenter.get(2);
            int distSquared = dx * dx + dy * dy + dz * dz;
            int rSquared = r * r;
            if (distSquared > rSquared) continue;

            if(_oct.point == null) {
                neighbours.addAll(_oct.getNeighbors(x, y, z, r));
                continue;
            }
            else if(_oct.point.isNull) {
                continue;
            }
            else if(x == _oct.point.x && y == _oct.point.y && z == _oct.point.z) {
                continue;
            }
            else if(
                Math.pow(_oct.point.get(0) - radiusCenter.get(0), 2) +
                    Math.pow(_oct.point.get(1) - radiusCenter.get(1), 2) +
                    Math.pow(_oct.point.get(2) - radiusCenter.get(2), 2)
                    > Math.pow(r, 2))
                continue;
            return _oct.objects;
        }

        return neighbours;
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


    /**
     * It loops from bottom back left oct to top front right oct<br/>
     * <b>Reason:</b> 2D rendering where z-cord is displayed by rendering position
     * @param action function to apply on objects
     */
    public void foreach(BiConsumer<T, OctPoint> action) {
        // Apply action to objects at this node if present
        if (objects != null && !objects.isEmpty()) {
            for (T object : objects) {
                action.accept(object, point);
            }
        }

        // Recursively call foreach on child octrees
        if (octs != null) {
            for (int i = octs.size() - 1; i >= 0; i--) {
                if (octs.get(i) != null) {
                    octs.get(i).foreach(action);
                }
            }
        }
    }

    public void foreach(Consumer<T> action) {
        // Apply action to objects at this node if present
        if (objects != null && !objects.isEmpty()) {
            for (T object : objects) {
                action.accept(object);
            }
        }

        // Recursively call foreach on child octrees
        if (octs != null) {
            for (int i = octs.size() - 1; i >= 0; i--) {
                if (octs.get(i) != null) {
                    octs.get(i).foreach(action);
                }
            }
        }
    }

}
