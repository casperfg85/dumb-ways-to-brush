package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * Created by G on 11/22/2015.
 */
public abstract class GameScreen implements Screen {
    protected float screenAlpha = 1.0f;
    protected float screenWidth = Gdx.graphics.getWidth();
    protected float screenHeight = Gdx.graphics.getHeight();

    public void setScreenAlpha(float f){
        screenAlpha = f;
    }
    public float getScreenAlpha(){
        return screenAlpha;
    }
}
