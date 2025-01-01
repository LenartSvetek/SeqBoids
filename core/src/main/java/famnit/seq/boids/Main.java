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
import util.math.quaterion.Quaternion;
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

        /*Random r = new Random();
        for(int i = 0; i < 1_500; i++) {
            int _x = r.nextInt(1, 800);
            int _y = r.nextInt(1, 800);
            int _z = r.nextInt(1, 800);

            Quaternion q = new Quaternion();
            q.setFromEulerAngles(r.nextInt(360), r.nextInt(360), r.nextInt(360));
            octree.insert(_x, _y, _z, new Boid(_x, _y, _z, q));
        }*/

        Quaternion q = new Quaternion();
        q.setFromEulerAngles(0, 0, 180);
        octree.insert(10, 10, 400, new Boid(410, 410, 410, q));

        q = new Quaternion();
        q.setFromEulerAngles(0, 0, 45);
        octree.insert(790, 790, 400, new Boid(420, 420, 410, q));
        q = new Quaternion();
        q.setFromEulerAngles(0, 0, 90);
        octree.insert(790, 0, 400, new Boid(400, 400, 410, q));

        Vector3 vector = new Vector3(25,25,25);
        Logger.log("Normalized vector: " + vector.normalized() + " size: " + vector.normalized().getSize());





    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        process();


        octree.foreach(obj -> {
            Quaternion rot = new Quaternion();
            rot.setFromEulerAngles(0, 0, 0);
            Quaternion q = Quaternion.lookAtFromPoints(obj.position, new Vector3(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY(), 400));
            //rot = rot.activeRotation(q.getFromEulerAngles());
            batch.draw(image, obj.getPosition().getX(), obj.getPosition().getY(), 0, 0, 8, 8, 1, 1, q.getFromEulerAngles().getZ() - 45);
        });

        font.draw(batch, "Upper left, FPS=" + Gdx.graphics.getFramesPerSecond(), 0, 10);

        batch.end();
    }

    private void process() {

        octree.foreach(boid -> {
            //boid.process(Gdx.graphics.getDeltaTime());
        });


    }

    @Override
    public void dispose() {
        if(Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop){
            batch.dispose();
            font.dispose();
        }


    }
}
