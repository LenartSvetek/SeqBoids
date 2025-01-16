package famnit.seq.boids;

import util.Logger;
import util.math.EulerAngles;
import util.math.vector.Vector3;

import java.util.Random;

public class Boid {
    Vector3 position;
    Vector3 deltaPosition = new Vector3();
    EulerAngles rot;
    Vector3 rotSensitivity = new Vector3(100, 100, 100);

    float movementSpeed = 1000.f; // pixels/second



    Boid(float x, float y, float z, EulerAngles rot) {
        position = new Vector3(x,y,z);
        this.rot = rot;

        deltaPosition.setX(-(float)(Math.cos(rot.getPitchRadians()) * Math.sin(rot.getYawRadians())));
        deltaPosition.setY((float)(Math.cos(rot.getPitchRadians()) * Math.cos(rot.getYawRadians())));
        deltaPosition.setZ((float)(Math.sin(rot.getPitchRadians())));


        deltaPosition.mul(new Random().nextFloat(100));
    }

    Boid(Boid boid) {
        position = boid.position;
        rot = boid.rot;
    }

    public void process(float deltaTime){
        position.setX(position.getX() + deltaPosition.getX() * deltaTime);
        position.setY(position.getY() + deltaPosition.getY() * deltaTime);
        position.setZ(position.getZ() + deltaPosition.getZ() * deltaTime);

        rot = EulerAngles.getEulerAnglesBetweenTwoPoints(new Vector3(), deltaPosition);
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public EulerAngles getRot() {
        return rot;
    }

    public void setRot(EulerAngles rot) {
        this.rot = rot;
    }

    public Vector3 getDeltaPosition() {
        return deltaPosition;
    }

    public void setDeltaPosition(Vector3 deltaPosition) {
        this.deltaPosition = deltaPosition;
    }
}
