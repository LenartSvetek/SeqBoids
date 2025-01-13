package famnit.seq.boids;

import util.Logger;
import util.math.EulerAngles;
import util.math.vector.Vector3;

public class Boid {
    Vector3 position;
    EulerAngles rot;

    float movementSpeed = 1000.f; // pixels/second



    Boid(float x, float y, float z, EulerAngles rot) {
        position = new Vector3(x,y,z);
        this.rot = rot;
    }

    Boid(Boid boid) {
        position = boid.position;
        rot = boid.rot;
    }

    public void process(float deltaTime){


        Vector3 direction = new Vector3();

        direction.setX(-(float)(Math.cos(rot.getPitchRadians()) * Math.sin(rot.getYawRadians())));
        direction.setY((float)(Math.cos(rot.getPitchRadians()) * Math.cos(rot.getYawRadians())));
        direction.setZ((float)(Math.sin(rot.getPitchRadians())));

        direction = direction.normalized();

        position.add(direction.mul(movementSpeed * deltaTime));
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
}
