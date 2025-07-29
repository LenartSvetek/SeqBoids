package util.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.function.Consumer;

public class Button {
    ShapeRenderer sr;
    SpriteBatch batch;
    BitmapFont font;

    boolean focused = false;

    int x;
    int y;
    int w;
    int h;

    Consumer<Object> onClick;


    String label;

    public Button(ShapeRenderer sr, BitmapFont font, int x, int y, int w, int h, String label) {
        this.sr = sr;
        this.font = font;
        batch = new SpriteBatch();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.label = label;
    }

    public Button(ShapeRenderer sr, BitmapFont font, int x, int y, int w, int h, String label, Consumer<Object> onClick) {
        this.sr = sr;
        this.font = font;
        batch = new SpriteBatch();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.label = label;
        this.onClick = onClick;
    }

    public void update() {
        int mX = Gdx.input.getX();
        int mY = Gdx.graphics.getHeight() - Gdx.input.getY();

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            if(mX >= x && mX <= x + w && mY >= y && mY <= y + h){
                focused = true;
            }
        }
        if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT) && focused){
            if(mX >= x && mX <= x + w && mY >= y && mY <= y + h){
                if(onClick != null)
                    this.onClick.accept(label);
            }
            focused = false;
        }
    }

    public void draw() {
        if(!focused)
            sr.begin(ShapeRenderer.ShapeType.Line);
        else
            sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.rect(x, y, w, h);
        sr.end();
        batch.begin();
        if(focused)
            font.setColor(Color.BLACK);
        else
            font.setColor(Color.WHITE);
        font.draw(batch, label, x + 5, y + 20);

        batch.end();
    }
}
