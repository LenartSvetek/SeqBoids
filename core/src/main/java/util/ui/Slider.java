package util.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import  com.badlogic.gdx.graphics.Color;


public class Slider {
    boolean focused = false;

    ShapeRenderer sr;
    BitmapFont font;

    float minValue;
    float maxValue;

    String label;

    float sliderValue;
    int x;
    int y;
    int w;
    int h;

    public Slider(int x, int y, int w, int h, float defualtValue, float minValue, float maxValue, String label, ShapeRenderer sr) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.minValue = minValue;
        this.maxValue = maxValue;
        sliderValue = defualtValue;
        this.sr = sr;
        this.label = label;
    }

    public void update(){
        int mX = Gdx.input.getX();
        int mY = Gdx.graphics.getHeight() - Gdx.input.getY();




        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            if(mX >= x && mX <= x + w && mY >= y && mY <= y + h){
                focused = true;
            }
        }
        if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            focused = false;
        }

        if(focused){
            sliderValue = (mX - x) / (float)w;
            if(sliderValue < 0){
                sliderValue = 0;
            }
            else if(sliderValue > 1){
                sliderValue = 1;
            }

            sliderValue = sliderValue * (maxValue - minValue) + minValue;
        }
    }


    public void draw() {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
        sr.rect(x, y, w, h);

        float dX = ((sliderValue - minValue) / (maxValue - minValue)) * w - 5;
        sr.setColor(Color.WHITE);
        sr.rect(x + dX, y - 5, 10, h + 10);

        sr.end();
    }

    public float getSliderValue() {
        return sliderValue;
    }
}
