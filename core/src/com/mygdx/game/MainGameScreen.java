package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.Text;

/**
 * Created by G on 11/22/2015.
 */
public class MainGameScreen extends GameScreen {
    private float screenCenterX = screenWidth/2;
    private float screenCenterY = screenHeight/2;

    private static final float TITLE_LABEL_WIDTH = 0.20f;
    private static final float TITLE_LABEL_HEIGHT = 0.15f;
    private static final float TITLE_LABEL_OFFSET_Y = 0.15f;
    private static final float TITLE_LABEL_OFFSET_X = 0.05f;
    private static final float TITLE_LABEL_TEXT_SCALE = 4.0f;

    private static final float PLANKTON_INIT_OFFSET_X = 0.1f;
    private static final float PLANKTON_INIT_OFFSET_Y = 0.1f;
    private static final float INITIAL_OFFSET_Y = 0.05f;

    private static final float SPONGE_TEXTURE_SCALE = 0.5f;
    private static final float SPONGE_MOVE_SPEED = 0.05f;

    private static final float PLANKTON_TEXTURE_SCALE = 0.8f;
    private static final float PLANKTON_MOVE_SPEED = 0.5f;
    private static final int NUM_PLANKTONS_TO_LOAD = 8;
    private static final String PLANKTON_PATH = "planktons/plankton-";

    private static final float WAVE_CHANCE = 0.8f;
    private static final int WAVE_INTERVAL = 10;
    private static final int WAVE_COUNT_MIN = 5;
    private static final int WAVE_COUNT_MAX = 8;

    private static final float RANDOM_SPAWN_CHANCE = 0.8f;
    private static final int RANDOM_SPAWN_INTERVAL = 3;
    private static final int TOTAL_TO_SPAWN  = 100;

    private static final int FINAL_WAVE_TIME = 30;

    private MainGame game;

    private SpriteBatch spriteBatch;
    private Texture background;
    private Texture spongeTexture;
    private Array<Texture> planktonTextures;
    private Rectangle sponge;
    private Array<Rectangle> planktons;
    private Label scoreLabelTracker;

    private Stage screenStage;
    private Skin screenSkin;
    private Timer gameTimer;

    private int secondsElapsed = -4;
    private int lastWaveTime = 0;
    private int lastRandomSpawnTime = 0;
    private int numWaves = 0;
    private int allowedToSpawn = TOTAL_TO_SPAWN;
    private int score = 0;

    public MainGameScreen(MainGame g){
        game = g;
    }

    @Override
    public void show() {
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        screenCenterX = screenWidth/2;
        screenCenterY = screenHeight/2;

        //Game variables
        secondsElapsed = -4;
        lastWaveTime = 0;
        lastRandomSpawnTime = 0;
        numWaves = 0;
        allowedToSpawn = TOTAL_TO_SPAWN;
        score = 0;

        //Texture Variables
        spriteBatch = new SpriteBatch();
        background = new Texture("mouth.png");
        spongeTexture = new Texture("badlogic.jpg");
        sponge = new Rectangle();
        sponge.height = spongeTexture.getHeight() * SPONGE_TEXTURE_SCALE;
        sponge.width = spongeTexture.getWidth() * SPONGE_TEXTURE_SCALE;
        sponge.x = screenCenterX - sponge.width/2;
        sponge.y = screenCenterY - sponge.height/2;
        planktons = new Array<Rectangle>();
        planktonTextures = new Array<Texture>();

        //UI Variables
        screenStage = new Stage();
        screenSkin = new Skin();

        gameTimer = new Timer();
        gameTimer.start();

        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        screenSkin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        BitmapFont bFont = new BitmapFont();
        screenSkin.add("default", bFont);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        //labelStyle.background = screenSkin.newDrawable("white", GameColor.FLAT_BLUE);
        labelStyle.font = screenSkin.getFont("default");
        labelStyle.font.setUseIntegerPositions(true);
        screenSkin.add("default", labelStyle);

        float currY = screenHeight - ((TITLE_LABEL_HEIGHT + INITIAL_OFFSET_Y) * screenHeight);
        float titleLabelOffsetX = screenCenterX - (TITLE_LABEL_WIDTH*screenWidth/2);

        final Label titleLabel = new Label("Ready!",screenSkin);
        titleLabel.setAlignment(2);
        titleLabel.setWidth(TITLE_LABEL_WIDTH * screenWidth);
        titleLabel.setHeight(TITLE_LABEL_HEIGHT * screenHeight);
        titleLabel.setFontScale(TITLE_LABEL_TEXT_SCALE);
        titleLabel.setX(titleLabelOffsetX);
        titleLabel.setY(currY);
        screenStage.addActor(titleLabel);

        final Label scoreLabel = new Label("Score: "+score,screenSkin);
        scoreLabel.setAlignment(2);
        scoreLabel.setWidth(TITLE_LABEL_WIDTH * screenWidth);
        scoreLabel.setHeight(TITLE_LABEL_HEIGHT * screenHeight);
        scoreLabel.setFontScale(TITLE_LABEL_TEXT_SCALE);
        scoreLabel.setX(titleLabelOffsetX);
        scoreLabel.setY(currY);

        gameTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                String gameMessage = "";
                if(secondsElapsed < -1){
                    gameMessage = "Starting in "+ ((-secondsElapsed)-1) + "!";
                } else if(secondsElapsed == -1){
                    gameMessage = "Go!!!";
                    spawnWave();
                } else {
                    if(secondsElapsed == 0){
                        float currX = TITLE_LABEL_OFFSET_X * screenWidth;
                        titleLabel.setX(currX);
                        scoreLabel.setX(currX + titleLabel.getWidth() + TITLE_LABEL_OFFSET_X * screenWidth);
                        scoreLabelTracker = scoreLabel;
                        screenStage.addActor(scoreLabelTracker);
                    }
                    int minutes = secondsElapsed/60;
                    int seconds = secondsElapsed%60;
                    gameMessage = "Time: "+minutes+":"+String.format("%02d",seconds);
                    if(secondsElapsed <= FINAL_WAVE_TIME){

                        if(secondsElapsed > WAVE_INTERVAL + lastWaveTime && MathUtils.random(0.0f, 1.0f) > 1.0f - WAVE_CHANCE){
                            spawnWave();
                        }
                        else if(secondsElapsed > RANDOM_SPAWN_INTERVAL + lastRandomSpawnTime && MathUtils.random(0.0f,1.0f) > 1.0f - RANDOM_SPAWN_CHANCE){
                            int toSpawn = MathUtils.random(0, numWaves);
                            for(int i = 0; i <=toSpawn; i++) {
                                spawnPlankton();
                            }
                            lastRandomSpawnTime = secondsElapsed;
                        }
                        else if(planktons.size == 0){
                            spawnWave();
                        }
                        else if(planktons.size <= WAVE_COUNT_MIN){
                            int toSpawn = MathUtils.random(0, numWaves);
                            for(int i = 0; i <=toSpawn; i++) {
                                spawnPlankton();
                            }
                            lastRandomSpawnTime = secondsElapsed;
                        }
                    }
                }
                titleLabel.setText(gameMessage);
                secondsElapsed += 1;
            }
        },0.0f,1.0f);
    }

    public void endGame(){
        game.setLastGameScore(score);
        game.setGameScreen(MainGame.endGameScreenID, MainGame.mainGameScreenID);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, screenAlpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0,screenWidth, screenHeight);
        int ctr = 0;
        Iterator<Rectangle> iter = planktons.iterator();
        while(iter.hasNext()) {
            Rectangle plankton = iter.next();
            plankton.x += MathUtils.random(-1.0f,1.0f) * PLANKTON_MOVE_SPEED * screenWidth *delta;
            plankton.y += MathUtils.random(-1.0f,1.0f) * PLANKTON_MOVE_SPEED * screenHeight *delta;
            if(plankton.x < 0) plankton.x = 0;
            if(plankton.x > screenWidth - plankton.width) plankton.x = screenWidth - plankton.width;
            if(plankton.y < 0) plankton.y = 0;
            if(plankton.y > screenHeight - plankton.height) plankton.y = screenHeight - plankton.height;

            if(plankton.overlaps(sponge)){
                planktonTextures.get(ctr).dispose();
                planktonTextures.removeIndex(ctr);
                iter.remove();
                score += 1;
                if(scoreLabelTracker != null){
                    scoreLabelTracker.setText("Score: "+score);
                }
            } else {
                spriteBatch.draw(planktonTextures.get(ctr), plankton.x, plankton.y, plankton.width, plankton.height);
                ctr = ctr + 1;
            }
        }
        spriteBatch.draw(spongeTexture, sponge.x, sponge.y, sponge.width, sponge.height);
        spriteBatch.end();

        screenStage.act(delta);
        screenStage.draw();

        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            float deltaX = (touchPos.x - (sponge.x + sponge.width/2))*delta;
            float deltaY = ((screenHeight - touchPos.y) - (sponge.y + sponge.height/2))*delta;
            sponge.x += deltaX;
            sponge.y += deltaY;
        }

        if(sponge.x < 0) sponge.x = 0;
        if(sponge.x > screenWidth - sponge.width) sponge.x = screenWidth - sponge.width;
        if(sponge.y < 0) sponge.y = 0;
        if(sponge.y > screenHeight - sponge.height) sponge.y = screenHeight - sponge.height;

        if(planktons.size == 0 && secondsElapsed > FINAL_WAVE_TIME){
            endGame();
        }

    }

    private void spawnWave(){
        int toSpawn = MathUtils.random(WAVE_COUNT_MIN, WAVE_COUNT_MAX);
        for(int i = 0; i <=toSpawn; i++) {
            spawnPlankton();
        }
        lastWaveTime = secondsElapsed;
        lastRandomSpawnTime = secondsElapsed;
        numWaves += 1;
    }

    private void spawnPlankton() {
        if(allowedToSpawn > 0){
            Rectangle plankton = new Rectangle();
            plankton.x = MathUtils.random(0, screenWidth - PLANKTON_INIT_OFFSET_X*screenWidth);
            plankton.y = MathUtils.random(0, screenHeight - PLANKTON_INIT_OFFSET_Y*screenHeight);
            Texture planktonTexture = new Texture(PLANKTON_PATH+MathUtils.random(1, NUM_PLANKTONS_TO_LOAD)+".png");
            plankton.width = planktonTexture.getWidth() * PLANKTON_TEXTURE_SCALE;
            plankton.height = planktonTexture.getHeight() * PLANKTON_TEXTURE_SCALE;
            planktons.add(plankton);
            planktonTextures.add(planktonTexture);
            allowedToSpawn -= 1;
        }
    }

    @Override
    public void resize(int width, int height) {
        screenHeight = height;
        screenWidth = width;
        screenCenterX = width/2;
        screenCenterY = height/2;

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
        screenStage.dispose();
        screenSkin.dispose();
        spriteBatch.dispose();
        background.dispose();
        spongeTexture.dispose();
        for(Texture t : planktonTextures ){
            t.dispose();
        }
    }
}
