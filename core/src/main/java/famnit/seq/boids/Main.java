package famnit.seq.boids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;

import util.Logger;
import util.octree.Octree;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private TextureRegion image;
    Octree<Integer> octree;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new TextureRegion(new Texture("bird.png"));

        octree = new Octree<Integer>(0, 0, 0, 800, 800, 800);

        octree.insert(1, 1, 1, 3123);
        octree.insert(1, 1, 3, 311);
        octree.insert(1, -1, 1, 31233);

        Logger.log("at (1, 1, 1): " + octree.get(1, 1, 1));
        Logger.log("at (1, 1, 3): " + octree.get(1, 1, 3));
        Logger.log("at (1, 2, 1): " + octree.get(1, 2, 1));
        Logger.log("at (1, -1, 1): " + octree.get(1, -1, 1));
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 400, 0, 0, 0, image.getRegionWidth(), image.getRegionHeight(), 1, 1, -45);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();

    }
}
