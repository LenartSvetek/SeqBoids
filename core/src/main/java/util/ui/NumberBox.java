package util.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import  com.badlogic.gdx.graphics.Color;


public class NumberBox {
    ShapeRenderer sr;
    SpriteBatch batch;
    BitmapFont font;

    boolean focused = false;

    int x;
    int y;
    int w;
    int h;

    int value = 0;

    String label;

    public NumberBox(ShapeRenderer sr, BitmapFont font, int x, int y, int w, int h, String label, int defaultValue) {
        this.sr = sr;
        this.font = font;
        batch = new SpriteBatch();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.label = label;
        value = defaultValue;
    }

    public void update() {
        int mX = Gdx.input.getX();
        int mY = Gdx.graphics.getHeight() - Gdx.input.getY();

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if(mX >= 150 + x && mX <= 150 + x + w && mY >= y && mY <= y + h){
                focused = true;
                value = 0;
            } else {
                focused = false;
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            focused = false;
        }


        if(focused){
            int num = -1;
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_0) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_0)){
                num = 0;
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1)){
                num = 1;
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)){
                num = 2;
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3)){
                num = 3;
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_4)){
                num = 4;
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_5)){
                num = 5;
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_6) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_6)){
                num = 6;
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_7) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_7)){
                num = 7;
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_8) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8)){
                num = 8;
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_9) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_9)){
                num = 9;
            }
            if(num != -1) {
                value = value * 10 + num;
            }
        }
    }

    public int getValue() {
        return value;
    }

    public void draw() {
        if(!focused)
            sr.begin(ShapeRenderer.ShapeType.Line);
        else
            sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.rect(150 + x, y, w, h);
        sr.end();
        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, label, x, y + 20);
        if(focused)
            font.setColor(Color.BLACK);
        else
            font.setColor(Color.WHITE);



        font.draw(batch, "" + value, 150 + x + 5, y + 20);
        batch.end();
    }
}
