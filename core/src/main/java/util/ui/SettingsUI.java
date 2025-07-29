package util.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SettingsUI {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    NumberBox nudgeUI;
    float nudgeFactor = 0.05f;

    NumberBox speedUI;
    int speedLimit = 1000;

    NumberBox mapXUI;
    NumberBox mapYUI;
    NumberBox mapZUI;
    int[] mapSize = {800, 800, 800};

    NumberBox bufferUI;
    int bufferZone = 100;

    NumberBox numUI;
    int numOfBoids = 5000;

    Button btn;

    public boolean isFinnished = false;

    public SettingsUI() {
        System.out.println("Setting up UI");

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();

        shapeRenderer = new ShapeRenderer();

        nudgeUI = new NumberBox(shapeRenderer, font, 10, Gdx.graphics.getHeight() - 40, 150, 30, "NudgeFactor(/100): ", 5);
        speedUI = new NumberBox(shapeRenderer, font, 10, Gdx.graphics.getHeight() - 80, 150, 30, "speed: ", (int)speedLimit);
        mapXUI = new NumberBox(shapeRenderer, font, 10, Gdx.graphics.getHeight() - 120, 150, 30, "mapX: ", mapSize[0]);
        mapYUI = new NumberBox(shapeRenderer, font, 10, Gdx.graphics.getHeight() - 160, 150, 30, "mapY: ", mapSize[1]);
        mapZUI = new NumberBox(shapeRenderer, font, 10, Gdx.graphics.getHeight() - 200, 150, 30, "mapZ: ", mapSize[2]);
        bufferUI = new NumberBox(shapeRenderer, font, 10, Gdx.graphics.getHeight() - 240, 150, 30, "buffer: ", bufferZone);
        numUI = new NumberBox(shapeRenderer, font, 10, Gdx.graphics.getHeight() - 280, 150, 30, "num of boids: ", numOfBoids);
        btn = new Button(shapeRenderer, font, 160, Gdx.graphics.getHeight() - 320, 150, 30, "Continue", (obj) -> {
            isFinnished = true;
        });
    }

    public void updateUI() {
        nudgeUI.update();
        speedUI.update();
        mapXUI.update();
        mapYUI.update();
        mapZUI.update();
        bufferUI.update();
        numUI.update();
        btn.update();
    }

    public void render() {
        nudgeUI.draw();
        speedUI.draw();
        mapXUI.draw();
        mapYUI.draw();
        mapZUI.draw();
        bufferUI.draw();
        numUI.draw();
        btn.draw();
    }

    public float getNudgeFactor() {
        return nudgeUI.getValue() / 100.f;
    }

    public int getSpeedLimit() {
        return speedUI.getValue();
    }

    public int[] getMapSize() {
        return new int[] {mapXUI.getValue(), mapYUI.getValue(), mapZUI.getValue()};
    }

    public int getBufferZone() {
        return bufferUI.getValue();
    }

    public int getNumOfBoids() {
        return numUI.getValue();
    }
}
