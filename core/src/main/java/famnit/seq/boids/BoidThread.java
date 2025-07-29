package famnit.seq.boids;

import com.badlogic.gdx.Gdx;
import util.math.vector.Vector3;
import util.octree.Octree;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BoidThread extends Thread{
    static final int visualRange = 40;
    static final int avoidRange = 20;


    final ArrayList<Boid> boidsArr;
    final Octree<Integer> boids;
    final int from;
    final int to;

    ArrayList<Boid> _boidsArr;
    Octree<Integer> _boids;

    public BoidThread(ArrayList<Boid> boidsArr, Octree<Integer> boids, int from, int to, String name) {
        super(name);
        this.boidsArr = boidsArr;
        this.boids = boids;

        this.from = from;
        this.to = to;

        this._boidsArr = new ArrayList<>();
        this._boids = new Octree<>(boids.GetBottomBackLeft(), boids.GetTopFrontRight());
    }

    @Override
    public void run() {
        for(int i = from; i < to; i++) {
            Boid boid = boidsArr.get(i);
            Vector3 pos = boid.getPosition();

            Boid newBoid = new Boid(boid);



            ArrayList<Boid> otherBoids = boids.getNeighbors((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), visualRange).stream().map(boidsArr::get).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Boid> tooClose = boids.getNeighbors((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), avoidRange).stream().map(boidsArr::get).collect(Collectors.toCollection(ArrayList::new));

            //System.out.println(otherBoids.stream().count());

            coherence(boid, otherBoids, newBoid);
            separation(boid, tooClose, newBoid);
            alignment(boid, otherBoids, newBoid);

            nudgeDesiredSpeed(newBoid);
            limitSpeed(newBoid);
            keepWithinBounds(newBoid);

            newBoid.process(Gdx.graphics.getDeltaTime());

            if(_boids.IsLocationValid(newBoid.getPosition())) {
                _boidsArr.add(newBoid);
                pos = newBoid.getPosition();
                _boids.insert((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), boidsArr.size() - 1);
            }


        }



    }

    public ArrayList<Boid> GetBoidsArr(){
        return _boidsArr;
    }

    public Octree<Integer> get_boids() {
        return _boids;
    }




    private void coherence(Boid boid, ArrayList<Boid> otherBoids, Boid newBoid) {


        Vector3 cohPos = new Vector3();

        if(!otherBoids.isEmpty()) {
            for (Boid otherBoid : otherBoids) {
                cohPos.sum(otherBoid.getPosition());
            }

            cohPos.div(otherBoids.size());

            Vector3 deltaPosition = boid.getDeltaPosition();
            deltaPosition.setX(deltaPosition.getX() + (cohPos.getX() - boid.getPosition().getX()) * BoidsValues.coherenceFactor);
            deltaPosition.setY(deltaPosition.getY() + (cohPos.getY() - boid.getPosition().getY()) * BoidsValues.coherenceFactor);
            deltaPosition.setZ(deltaPosition.getZ() + (cohPos.getZ() - boid.getPosition().getZ()) * BoidsValues.coherenceFactor);

            newBoid.setDeltaPosition(deltaPosition);
        }
    }

    private void separation(Boid boid, ArrayList<Boid> otherBoids, Boid newBoid) {


        Vector3 sepPos = new Vector3();

        for (Boid otherBoid : otherBoids) {
            sepPos.setX(boid.getPosition().getX() - otherBoid.getPosition().getX());
            sepPos.setY(boid.getPosition().getY() - otherBoid.getPosition().getY());
            sepPos.setZ(boid.getPosition().getZ() - otherBoid.getPosition().getZ());
        }

        Vector3 deltaPos = boid.getDeltaPosition();

        deltaPos.setX(deltaPos.getX() + sepPos.getX() * BoidsValues.avoidFactor);
        deltaPos.setY(deltaPos.getY() + sepPos.getY() * BoidsValues.avoidFactor);
        deltaPos.setZ(deltaPos.getZ() + sepPos.getZ() * BoidsValues.avoidFactor);

        newBoid.setDeltaPosition(deltaPos);
    }

    private void alignment(Boid boid, ArrayList<Boid> otherBoids, Boid newBoid) {


        Vector3 avgDX = new Vector3();

        if(otherBoids.isEmpty()) return;

        for (Boid otherBoid : otherBoids) {
            avgDX.sum(otherBoid.getDeltaPosition());
        }

        avgDX.div(otherBoids.size());

        Vector3 deltaPos = boid.getDeltaPosition();

        deltaPos.setX(deltaPos.getX() + (avgDX.getX() - deltaPos.getX()) * BoidsValues.alignmentFactor);
        deltaPos.setY(deltaPos.getY() + (avgDX.getY() - deltaPos.getY()) * BoidsValues.alignmentFactor);
        deltaPos.setZ(deltaPos.getZ() + (avgDX.getZ() - deltaPos.getZ()) * BoidsValues.alignmentFactor);

        newBoid.setDeltaPosition(deltaPos);
    }

    private void nudgeDesiredSpeed(Boid boid) {


        float desiredSpeed = boid.desiredSpeed;
        Vector3 deltaPos = boid.getDeltaPosition();
        float speed = (float)Math.sqrt(Math.pow(deltaPos.getX(), 2) + Math.pow(deltaPos.getY(), 2));

        if(speed < desiredSpeed) {
            deltaPos.sum(Vector3.mul(deltaPos, BoidsValues.nudgeFactor * Gdx.graphics.getDeltaTime()));
        }

        boid.setDeltaPosition(deltaPos);
    }

    private void limitSpeed(Boid boid) {

        Vector3 deltaPos = boid.getDeltaPosition();
        float speed = (float)Math.sqrt(Math.pow(deltaPos.getX(), 2) + Math.pow(deltaPos.getY(), 2));

        if(speed > BoidsValues.speedLimit) {
            deltaPos.setX((deltaPos.getX() / speed) * BoidsValues.speedLimit);
            deltaPos.setY((deltaPos.getY() / speed) * BoidsValues.speedLimit);
            deltaPos.setZ((deltaPos.getZ() / speed) * BoidsValues.speedLimit);

            boid.setDeltaPosition(deltaPos);
        }
    }

    private void keepWithinBounds(Boid boid) {
        Vector3 deltaPos = boid.getDeltaPosition();
        Vector3 boidPos = boid.getPosition();

        if(boidPos.getX() < BoidsValues.margin)
            deltaPos.setX(deltaPos.getX() + BoidsValues.turnFactor);
        else if (boidPos.getX() > BoidsValues.mapSize[0] - BoidsValues.margin)
            deltaPos.setX(deltaPos.getX() - BoidsValues.turnFactor);
        if(boidPos.getY() < BoidsValues.margin)
            deltaPos.setY(deltaPos.getY() + BoidsValues.turnFactor);
        else if (boidPos.getY() > BoidsValues.mapSize[1] - BoidsValues.margin)
            deltaPos.setY(deltaPos.getY() - BoidsValues.turnFactor);
        if(boidPos.getZ() < BoidsValues.margin)
            deltaPos.setZ(deltaPos.getZ() + BoidsValues.turnFactor);
        else if (boidPos.getZ() > BoidsValues.mapSize[2] - BoidsValues.margin)
            deltaPos.setZ(deltaPos.getZ() - BoidsValues.turnFactor);

        boid.setDeltaPosition(deltaPos);
    }
}
