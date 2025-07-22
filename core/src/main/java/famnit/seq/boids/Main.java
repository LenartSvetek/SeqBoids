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
import java.util.stream.Collectors;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    static int CPU_CORES = Runtime.getRuntime().availableProcessors();
    static BoidThread[] THREADS = new BoidThread[CPU_CORES];

    private SpriteBatch batch;
    private TextureRegion image;
    Octree<Integer> octree;
    ArrayList<Boid> boidsArr;
    BitmapFont font;

    int[] mapSize = {800, 800, 800};
    int bufferZone = 100;
    int numOfBoids = 500;

    @Override
    public void create() {

        if(Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop){
            batch = new SpriteBatch();
            image = new TextureRegion(new Texture("bird.png"));
            font = new BitmapFont();
        }
        octree = new Octree<Integer>(-bufferZone, -bufferZone, -bufferZone, mapSize[0] + bufferZone, mapSize[1] + bufferZone, mapSize[2] + bufferZone);
        boidsArr = new ArrayList<Boid>(numOfBoids);

        Random r = new Random();
        for(int i = 0; i < numOfBoids; i++) {
            int _x = r.nextInt(150, 800 - 150);
            int _y = r.nextInt(150, 800 - 150);
            int _z = r.nextInt(150, 800 - 150);

            EulerAngles q = new EulerAngles(r.nextInt(360), r.nextInt(360), r.nextInt(360));

            octree.insert(_x, _y, _z, i);
            boidsArr.add(new Boid(_x, _y, _z, q));
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


        octree.foreach(boidIndex -> {
            Boid obj = boidsArr.get(boidIndex);
            if(    obj.position.getX() >= -10 && obj.position.getX() <= mapSize[0] + 10
                && obj.position.getY() >= -10 && obj.position.getY() <= mapSize[1] + 10
            ) {
                float scale = ((obj.getPosition().getZ() / 800.f) + 0.5f) * 2;

                batch.draw(image, obj.getPosition().getX(), obj.getPosition().getY(), 4, 4, 8, 8, scale, scale, obj.getRot().getYaw() - 45);
            }
        });

        font.draw(batch, "Upper left, FPS=" + Gdx.graphics.getFramesPerSecond(), 0, 10);

        batch.end();
    }

    private void process() {
        Octree<Integer> newOctree = new Octree<Integer>(-bufferZone, -bufferZone, -bufferZone, mapSize[0] + bufferZone, mapSize[1] + bufferZone, mapSize[2] + bufferZone);
        ArrayList<Boid> newBoidArr = new ArrayList<>();
        int visualRange = 40;
        int avoidRange = 20;


        int numOfBoidsPerThread = Math.ceilDiv(boidsArr.size(), CPU_CORES);

        for (int i = 0; i < CPU_CORES; i++) {

            if(i != CPU_CORES - 1)
                THREADS[i] = new BoidThread(boidsArr, octree, i * numOfBoidsPerThread, (i + 1) * numOfBoidsPerThread);
            else
                THREADS[i] = new BoidThread(boidsArr, octree, i * numOfBoidsPerThread, boidsArr.size());

            THREADS[i].start();
        }

        for(int i = 0; i < CPU_CORES; i++) {
            try {
                THREADS[i].join();

                for(Boid boid : THREADS[i].GetBoidsArr()) {
                    newBoidArr.add(boid);
                    Vector3 pos = boid.getPosition();
                    newOctree.insert((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), newBoidArr.size() - 1);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        octree.foreach(boidIndex -> {
//            Boid boid = boidsArr.get(boidIndex);
//            Vector3 pos = boid.getPosition();
//
//            Boid newBoid = new Boid(boid);
//
//
//
//            ArrayList<Boid> otherBoids = octree.getNeighbors((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), visualRange).stream().map((boidI) -> boidsArr.get(boidI)).collect(Collectors.toCollection(ArrayList::new));
//            ArrayList<Boid> tooClose = octree.getNeighbors((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), avoidRange).stream().map((boidI) -> boidsArr.get(boidI)).collect(Collectors.toCollection(ArrayList::new));
//
//            //System.out.println(otherBoids.stream().count());
//
//            coherence(boid, otherBoids, newBoid);
//            separation(boid, tooClose, newBoid);
//            alignment(boid, otherBoids, newBoid);
//
//            nudgeDesiredSpeed(newBoid);
//            limitSpeed(newBoid);
//            keepWithinBounds(newBoid);
//
//            newBoid.process(Gdx.graphics.getDeltaTime());
//
//            synchronized (newBoidArr) {
//                newBoidArr.add(newBoid);
//                newOctree.insert((int)newBoid.getPosition().getX(), (int)newBoid.getPosition().getY(), (int)newBoid.getPosition().getZ(), newBoidArr.size() - 1);
//            }
//
//        });

        octree = newOctree;
        boidsArr = newBoidArr;
    }




    @Override
    public void dispose() {
        if(Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop){
            batch.dispose();
            font.dispose();
        }


    }
}
