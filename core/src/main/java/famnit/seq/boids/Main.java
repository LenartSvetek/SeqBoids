package famnit.seq.boids;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import util.octree.Octree;

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
        for(int i = 0; i < 1_500; i++) {
            int _x = r.nextInt(800);
            int _y = r.nextInt(800);
            int _z = r.nextInt(800);

            octree.insert(_x, _y, _z, new Boid(_x, _y, _z, 0));
        }


    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        process();

        octree.foreach(obj -> {
            batch.draw(image, obj.getX(), obj.getY(), 0, 0, 8, 8, 1, 1, obj.rot - 45);
        });

        font.draw(batch, "Upper left, FPS=" + Gdx.graphics.getFramesPerSecond(), 0, 10);

        batch.end();
    }

    private void process() {
        octree.foreach(boid -> {
            // rule of cohesion
            ArrayList<Boid> neighbours = octree.getNeighbors(boid.getX(), boid.getY(), boid.getZ(), 25);

            if(!neighbours.isEmpty()) {
                int aX = 0;
                int aY = 0;
                int aZ = 0;

                for (Boid neighbour : neighbours) {
                    aX += neighbour.getX();
                    aY += neighbour.getY();
                    aZ += neighbour.getZ();
                }

                aX /= neighbours.size();
                aY /= neighbours.size();
                aZ /= neighbours.size();

                Vector3 avgPos = new Vector3(aX, aY, aZ);
                Vector3 boidPos = new Vector3(boid.getX(), boid.getY(), boid.getZ());
            }
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
