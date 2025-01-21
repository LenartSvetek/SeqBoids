package famnit.seq.boids;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import util.Logger;
import util.math.EulerAngles;
import util.octree.Octree;
import util.math.vector.Vector3;

import java.util.ArrayList;
import java.util.Random;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private TextureRegion image;
    Octree<Boid> octree;
    BitmapFont font;

    @Override
    public void create() {

        if(Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop){
            batch = new SpriteBatch();
            image = new TextureRegion(new Texture("bird.png"));
            font = new BitmapFont();
        }
        octree = new Octree<Boid>(0, 0, 0, 800, 800, 800);

        Random r = new Random();
        for(int i = 0; i < 500; i++) {
            int _x = r.nextInt(150, 800 - 150);
            int _y = r.nextInt(150, 800 - 150);
            int _z = r.nextInt(150, 800 - 150);

            EulerAngles q = new EulerAngles(r.nextInt(360), r.nextInt(360), r.nextInt(360));

            octree.insert(_x, _y, _z, new Boid(_x, _y, _z, q));
        }
/*
        EulerAngles q = new EulerAngles();
        octree.insert(410, 410, 399, new Boid(410, 410, 399, q));

        q = new EulerAngles();
        octree.insert(420, 420, 400, new Boid(420, 420, 400, q));
        q = new EulerAngles();
        octree.insert(400, 400, 400, new Boid(400, 400, 400, q));
*/




    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        process();


        octree.foreach(obj -> {
            float scale = ((obj.getPosition().getZ() / 800.f) + 0.5f) * 2;


            batch.draw(image, obj.getPosition().getX(), obj.getPosition().getY(), 4, 4, 8, 8, scale, scale, obj.getRot().getYaw() - 45);
        });

        font.draw(batch, "Upper left, FPS=" + Gdx.graphics.getFramesPerSecond(), 0, 10);

        batch.end();
    }

    private void process() {
        Octree<Boid> newOctree = new Octree<Boid>(0, 0, 0, 800, 800, 800);

        int visualRange = 40;
        int avoidRange = 20;

        octree.foreach(boid -> {
            Vector3 pos = boid.getPosition();

            Boid newBoid = new Boid(boid);

            ArrayList<Boid> otherBoids = octree.getNeighbors((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), visualRange);
            ArrayList<Boid> tooClose = octree.getNeighbors((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), avoidRange);

            coherence(boid, otherBoids, newBoid);
            separation(boid, tooClose, newBoid);
            alignment(boid, otherBoids, newBoid);

            limitSpeed(newBoid);
            keepWithinBounds(newBoid);

            newBoid.process(Gdx.graphics.getDeltaTime());

            newOctree.insert((int)newBoid.getPosition().getX(), (int)newBoid.getPosition().getY(), (int)newBoid.getPosition().getZ(), newBoid);
        });

        octree = newOctree;

    }

    private void coherence(Boid boid, ArrayList<Boid> otherBoids, Boid newBoid) {
        float coherenceFactor = 0.005f;

        Vector3 cohPos = new Vector3();

        if(!otherBoids.isEmpty()) {
            for (Boid otherBoid : otherBoids) {
                cohPos.sum(otherBoid.getPosition());
            }

            cohPos.div(otherBoids.size());

            Vector3 deltaPosition = boid.getDeltaPosition();
            deltaPosition.setX(deltaPosition.getX() + (cohPos.getX() - boid.getPosition().getX()) * coherenceFactor);
            deltaPosition.setY(deltaPosition.getY() + (cohPos.getY() - boid.getPosition().getY()) * coherenceFactor);
            deltaPosition.setZ(deltaPosition.getZ() + (cohPos.getZ() - boid.getPosition().getZ()) * coherenceFactor);

            newBoid.setDeltaPosition(deltaPosition);
        }
    }

    private void separation(Boid boid, ArrayList<Boid> otherBoids, Boid newBoid) {
        float avoidFactor = 0.05f;

        Vector3 sepPos = new Vector3();

        for (Boid otherBoid : otherBoids) {
            sepPos.setX(boid.getPosition().getX() - otherBoid.getPosition().getX());
            sepPos.setY(boid.getPosition().getY() - otherBoid.getPosition().getY());
            sepPos.setZ(boid.getPosition().getZ() - otherBoid.getPosition().getZ());
        }

        Vector3 deltaPos = boid.getDeltaPosition();

        deltaPos.setX(deltaPos.getX() + sepPos.getX() * avoidFactor);
        deltaPos.setY(deltaPos.getY() + sepPos.getY() * avoidFactor);
        deltaPos.setZ(deltaPos.getZ() + sepPos.getZ() * avoidFactor);

        newBoid.setDeltaPosition(deltaPos);
    }

    private void alignment(Boid boid, ArrayList<Boid> otherBoids, Boid newBoid) {
        float alignmentFactor = 0.05f;

        Vector3 avgDX = new Vector3();

        if(otherBoids.isEmpty()) return;

        for (Boid otherBoid : otherBoids) {
            avgDX.sum(otherBoid.getDeltaPosition());
        }

        avgDX.div(otherBoids.size());

        Vector3 deltaPos = boid.getDeltaPosition();

        deltaPos.setX(deltaPos.getX() + (avgDX.getX() - deltaPos.getX()) * alignmentFactor);
        deltaPos.setY(deltaPos.getY() + (avgDX.getY() - deltaPos.getY()) * alignmentFactor);
        deltaPos.setZ(deltaPos.getZ() + (avgDX.getZ() - deltaPos.getZ()) * alignmentFactor);

        newBoid.setDeltaPosition(deltaPos);
    }

    private void limitSpeed(Boid boid) {
        float speedLimit = 1000;
        Vector3 deltaPos = boid.getDeltaPosition();
        float speed = (float)Math.sqrt(Math.pow(deltaPos.getX(), 2) + Math.pow(deltaPos.getY(), 2));

        if(speed > speedLimit) {
            deltaPos.setX((deltaPos.getX() / speed) * speedLimit);
            deltaPos.setY((deltaPos.getY() / speed) * speedLimit);
            deltaPos.setZ((deltaPos.getZ() / speed) * speedLimit);

            boid.setDeltaPosition(deltaPos);
        }
    }

    private void keepWithinBounds(Boid boid) {
        float margin = 150;
        float turnFactor = 0.05f;

        Vector3 deltaPos = boid.getDeltaPosition();
        Vector3 boidPos = boid.getPosition();

        if(boidPos.getX() < margin)
            deltaPos.setX(deltaPos.getX() + turnFactor);
        else if (boidPos.getX() > Gdx.graphics.getWidth() - margin)
            deltaPos.setX(deltaPos.getX() - turnFactor);
        if(boidPos.getY() < margin)
            deltaPos.setY(deltaPos.getY() + turnFactor);
        else if (boidPos.getY() > Gdx.graphics.getHeight() - margin)
            deltaPos.setY(deltaPos.getY() - turnFactor);
        if(boidPos.getZ() < margin)
            deltaPos.setZ(deltaPos.getZ() + turnFactor);
        else if (boidPos.getZ() > 800 - margin)
            deltaPos.setZ(deltaPos.getZ() - turnFactor);

        boid.setDeltaPosition(deltaPos);
    }


    @Override
    public void dispose() {
        if(Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop){
            batch.dispose();
            font.dispose();
        }


    }
}
