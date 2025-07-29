package famnit.seq.boids;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import util.Logger;
import util.math.EulerAngles;
import util.octree.Octree;
import util.math.vector.Vector3;
import util.ui.SettingsUI;
import util.ui.Slider;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    SettingsUI settings;

    static int CPU_CORES = Runtime.getRuntime().availableProcessors(); //Runtime.getRuntime().availableProcessors()
    static Thread[] THREADS = new BoidThread[CPU_CORES];

    private SpriteBatch batch;
    private TextureRegion image;
    ShapeRenderer shapeRenderer;
    Octree<Integer> octree;
    ArrayList<Boid> boidsArr;
    BitmapFont font;

    int[] mapSize = {800, 800, 800};
    int bufferZone = 100;
    int numOfBoids = 10000;

    Slider coherenceUI;
    Slider avoidUI;
    Slider alignmentUI;

    @Override
    public void create() {

        if(Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop){
            batch = new SpriteBatch();
            image = new TextureRegion(new Texture("bird.png"));
            font = new BitmapFont();
            shapeRenderer = new ShapeRenderer();

            settings = new SettingsUI();
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
        if(settings != null) {
            ScreenUtils.clear(0, 0, 0, 1f);
            settings.updateUI();
            settings.render();
            if(settings.isFinnished) {
                BoidsValues.nudgeFactor = settings.getNudgeFactor();
                BoidsValues.speedLimit = settings.getSpeedLimit();
                BoidsValues.margin = settings.getBufferZone();
                numOfBoids = settings.getNumOfBoids();
                mapSize = settings.getMapSize();
                BoidsValues.mapSize = mapSize;
                BoidsValues.turnFactor = settings.getTurnFactor();
                octree = new Octree<Integer>(-bufferZone, -bufferZone, -bufferZone, mapSize[0] + bufferZone, mapSize[1] + bufferZone, mapSize[2] + bufferZone);
                boidsArr = new ArrayList<Boid>(numOfBoids);

//                Gdx.gl.glViewport(0, 0,mapSize[0], mapSize[1]);
//                Gdx.graphics.setWindowedMode(mapSize[0], mapSize[1]);

                Random r = new Random();
                for(int i = 0; i < numOfBoids; i++) {
                    int _x = r.nextInt(150, mapSize[0] - 150);
                    int _y = r.nextInt(150, mapSize[1] - 150);
                    int _z = r.nextInt(150, mapSize[2] - 150);

                    EulerAngles q = new EulerAngles(r.nextInt(360), r.nextInt(360), r.nextInt(360));

                    octree.insert(_x, _y, _z, i);
                    boidsArr.add(new Boid(_x, _y, _z, q));

                }

                coherenceUI = new Slider(30, 10, 200, 10, BoidsValues.coherenceFactor, 0.0001f, 0.01f, "Coherence", shapeRenderer);
                avoidUI = new Slider(240, 10, 200, 10, BoidsValues.avoidFactor, 0.001f, 0.1f, "Coherence", shapeRenderer);
                alignmentUI = new Slider(450, 10, 200, 10, BoidsValues.alignmentFactor, 0.001f, 0.1f, "Coherence", shapeRenderer);

                settings = null;
            }
            return;
        }

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        process();


        boidsArr.forEach(obj -> {
//            Boid obj = boidsArr.get(objboidIndex);
            if(    obj.position.getX() >= -10 && obj.position.getX() <= mapSize[0] + 10
                && obj.position.getY() >= -10 && obj.position.getY() <= mapSize[1] + 10
            ) {
                float scale = ((obj.getPosition().getZ() / (float)mapSize[2]) + 0.5f) * 2;

                float dX = obj.position.getX() / mapSize[0] * 800;
                float dY = obj.position.getY() / mapSize[1] * 800;


                batch.draw(image, dX, dY, 4, 4, 8, 8, scale, scale, obj.getRot().getYaw() - 45);
            }
        });

        font.draw(batch, "Coherence: " + BoidsValues.coherenceFactor, 30, 40);
        font.draw(batch, "Avoid: " + BoidsValues.avoidFactor, 240, 40);
        font.draw(batch, "Alignment: " + BoidsValues.alignmentFactor, 450, 40);

        batch.end();

        coherenceUI.draw();
        avoidUI.draw();
        alignmentUI.draw();
    }

    private void process() {

        coherenceUI.update();
        BoidsValues.coherenceFactor = coherenceUI.getSliderValue();

        avoidUI.update();
        BoidsValues.avoidFactor = avoidUI.getSliderValue();

        alignmentUI.update();
        BoidsValues.alignmentFactor = alignmentUI.getSliderValue();

        Octree<Integer> newOctree = new Octree<Integer>(-bufferZone, -bufferZone, -bufferZone, mapSize[0] + bufferZone, mapSize[1] + bufferZone, mapSize[2] + bufferZone);
        ArrayList<Boid> newBoidArr = new ArrayList<>();


        int numOfBoidsPerThread = Math.ceilDiv(boidsArr.size(), CPU_CORES);

        for (int i = 0; i < CPU_CORES && i < boidsArr.size(); i++) {

            if(i != CPU_CORES - 1)
                THREADS[i] = new BoidThread(boidsArr, octree, i * numOfBoidsPerThread, (i + 1) * numOfBoidsPerThread, "BoidsThread-"+i);
            else
                THREADS[i] = new BoidThread(boidsArr, octree, i * numOfBoidsPerThread, boidsArr.size(), "BoidsThread-"+i);


            THREADS[i].start();
        }

        int offset = 0;
        OffsetThread[] offsetThreads = new OffsetThread[CPU_CORES];

        for(int i = 0; i < CPU_CORES && i < boidsArr.size(); i++) {
            try {
                THREADS[i].join();

                offset = newBoidArr.size();
                newBoidArr.addAll(((BoidThread)THREADS[i]).GetBoidsArr());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            Octree<Integer> boidOct = ((BoidThread)THREADS[i]).get_boids();
            offsetThreads[i] = new OffsetThread(newBoidArr, offset, newBoidArr.size(),"OffsetThread-"+i, boidOct.GetBottomBackLeft(), boidOct.GetTopFrontRight());

        }

        for(int i = 0; i < CPU_CORES && i < boidsArr.size(); i++) {
            offsetThreads[i].start();
        }


        for (int i = 0; i < CPU_CORES && i < boidsArr.size(); i++) {
            try {
                offsetThreads[i].join();

                newOctree.insert((offsetThreads[i]).get_boids());

//                (offsetThreads[i]).get_boids().foreach((ind, point) -> {
//                    newOctree.insert(point.get(0), point.get(1), point.get(2), ind);
//                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        boidsArr = newBoidArr;
        octree = newOctree;


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
    }




    @Override
    public void dispose() {
        if(Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop){
            batch.dispose();
            font.dispose();
        }


    }
}
