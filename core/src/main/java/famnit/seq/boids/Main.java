package famnit.seq.boids;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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
import java.util.Vector;

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
        for(int i = 0; i < 10_000; i++) {
            int _x = r.nextInt(1, 800);
            int _y = r.nextInt(1, 800);
            int _z = r.nextInt(1, 800);

            EulerAngles q = new EulerAngles();

            octree.insert(_x, _y, _z, new Boid(_x, _y, _z, q));
        }

        EulerAngles q = new EulerAngles();
        octree.insert(410, 410, 399, new Boid(410, 410, 399, q));

        q = new EulerAngles();
        octree.insert(420, 420, 400, new Boid(420, 420, 400, q));
        q = new EulerAngles();
        octree.insert(400, 400, 400, new Boid(400, 400, 400, q));

        Vector3 vector = new Vector3(25,25,25);





    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        process();


        octree.foreach(obj -> {



            //Logger.log("boids pos: " + obj.getPosition() + " mousePos: " + new Vector3(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY(), 400));

            batch.draw(image, obj.getPosition().getX(), obj.getPosition().getY(), 4, 4, 8, 8, 1, 1, obj.getRot().getYaw() - 45);
        });

        font.draw(batch, "Upper left, FPS=" + Gdx.graphics.getFramesPerSecond(), 0, 10);

        batch.end();
    }

    private void process() {
        Vector3 mousePos = new Vector3(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY(), 400);
        Octree<Boid> newOctree = new Octree<Boid>(0, 0, 0, 800, 800, 800);

        octree.foreach(boid -> {
            EulerAngles q = EulerAngles.getEulerAnglesBetweenTwoPoints(boid.position, mousePos);

            boid.setRot(q);
            Vector3 pos = boid.getPosition();

            Boid newBoid = new Boid(pos.getX(), pos.getY(), pos.getZ(), boid.getRot());



            ArrayList<Boid> tooClose = octree.getNeighbors((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), 20);

            if(!tooClose.isEmpty()) {
                EulerAngles rot = new EulerAngles();
                for (Boid value : tooClose) {
                    rot = EulerAngles.sum(rot, EulerAngles.getEulerAnglesBetweenTwoPoints(pos, value.getPosition()));
                }
                newBoid.setRot(rot.inverse());
            }
            newBoid.process(Gdx.graphics.getDeltaTime());

            newOctree.insert((int)newBoid.getPosition().getX(), (int)newBoid.getPosition().getY(), (int)newBoid.getPosition().getZ(), newBoid);
        });

        octree = newOctree;

    }

    @Override
    public void dispose() {
        if(Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop){
            batch.dispose();
            font.dispose();
        }


    }
}
