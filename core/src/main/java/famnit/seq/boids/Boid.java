package famnit.seq.boids;

import util.Logger;
import util.math.quaterion.Quaternion;
import util.math.vector.Vector3;

public class Boid {
    Vector3 position;
    Quaternion rot;

    float movementSpeed = 100.f; // pixels/second



    Boid(float x, float y, float z, Quaternion rot) {
        position = new Vector3(x,y,z);
        this.rot = rot;
    }

    public void process(float deltaTime){


        Vector3 direction = rot.activeRotation(Vector3.FORWARD).normalized();
        if(rot.isUnit()) {
            direction = new Vector3(1,0,0);
        }

        position.add(direction.mul(movementSpeed * deltaTime));
        Logger.log("position: " + position + " direction: " + direction + " dirsize: " + direction.getSize());
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }
}
