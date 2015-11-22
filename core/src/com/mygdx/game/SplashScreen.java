package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by G on 11/22/2015.
 */
public class SplashScreen extends GameScreen {
    private float screenCenterX = screenWidth/2;
    private float screenCenterY = screenHeight/2;

    private MainGame game;
    private SpriteBatch spriteBatch;
    private Texture splash;

    public SplashScreen(MainGame g){
        game = g;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        splash = new Texture("badlogic.jpg");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float splashPosX = screenCenterX - splash.getWidth()/2;
        float splashPosY = screenCenterY - splash.getHeight()/2;
        spriteBatch.setColor(1,1,1, screenAlpha);
        spriteBatch.begin();
        spriteBatch.draw(splash, splashPosX, splashPosY);
        spriteBatch.end();

    }

    @Override
    public void resize(int width, int height) {
        screenHeight = height;
        screenWidth = width;

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        splash.dispose();
    }
}
