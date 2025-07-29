package famnit.seq.boids;

import util.math.vector.Vector3;
import util.octree.OctPoint;
import util.octree.Octree;

import java.util.ArrayList;

public class OffsetThread extends Thread{



    final ArrayList<Boid> boids;
    final int from;
    final int to;

    Octree<Integer> _boids;

    public OffsetThread(ArrayList<Boid> boids, int from, int to, String name, OctPoint BtmBackLeft, OctPoint TopFrontRight) {
        super(name);
        this.boids = boids;

        this._boids = new Octree<>(BtmBackLeft, TopFrontRight);

        this.from = from;
        this.to = to;
    }

    @Override
    public void run() {
        for(int i = from; i < to; ++i) {
            Vector3 pos = boids.get(i).getPosition();
            _boids.insert((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), i);
        }
    }



    public Octree<Integer> get_boids() {
        return _boids;
    }

}
