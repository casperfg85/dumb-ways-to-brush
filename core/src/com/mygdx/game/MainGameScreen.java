package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.soap.Text;

/**
 * Created by G on 11/22/2015.
 */
public class MainGameScreen extends GameScreen {
    private float screenCenterX = screenWidth/2;
    private float screenCenterY = screenHeight/2;

    private static final float TITLE_LABEL_WIDTH = 0.15f;
    private static final float TITLE_LABEL_HEIGHT = 0.15f;
    private static final float TITLE_LABEL_OFFSET_Y = 0.15f;
    private static final float TITLE_LABEL_OFFSET_X = 0.05f;
    private static final float LABEL_TRACKER_OFFSET = 0.025f;
    private static final float TITLE_LABEL_TEXT_SCALE = 4.0f;

    private static final float PLANKTON_INIT_OFFSET_X = 0.1f;
    private static final float PLANKTON_INIT_OFFSET_Y = 0.1f;
    private static final float INITIAL_OFFSET_Y = 0.05f;

    private static final float SPONGE_TEXTURE_SCALE = 0.5f;
    private static final float SPONGE_MOVE_SPEED = 0.05f;

    private static final float PLANKTON_TEXTURE_SCALE = 0.8f;
    private static final float PLANKTON_MOVE_SPEED = 0.5f;
    private static final int NUM_PLANKTONS_TO_LOAD = 8;
    private static final int NUM_COLORS = 3;
    private static final String PLANKTON_PATH = "planktons/plankton-";
    private static final String RED_PLANKTON_PATH = "planktons-red/plankton-";
    private static final String GREEN_PLANKTON_PATH = "planktons-green/plankton-";
    private static final String BLUE_PLANKTON_PATH = "planktons-blue/plankton-";
    private static final float PLANKTON_DECAY = 0.01f;


    private static final float WAVE_CHANCE = 0.8f;
    private static final int WAVE_INTERVAL = 10;
    private static final int WAVE_COUNT_MIN = 5;
    private static final int WAVE_COUNT_MAX = 8;
    private static final int MAX_PER_COLOR = 30;

    private static final float RANDOM_SPAWN_CHANCE = 0.8f;
    private static final int RANDOM_SPAWN_INTERVAL = 3;
    private static final int TOTAL_TO_SPAWN  = 100;

    private static final int FINAL_WAVE_TIME = 30;

    private MainGame game;

    private SpriteBatch spriteBatch;
    private Texture background;
    private GameEntity sponge;
    private Array<GameEntity> planktons;
    private Label scoreLabelTracker;
    private Label redLabelTracker;
    private Label greenLabelTracker;
    private Label blueLabelTracker;

    private Stage screenStage;
    private Skin screenSkin;
    private Timer gameTimer;

    private int secondsElapsed = -4;
    private int lastWaveTime = 0;
    private int lastRandomSpawnTime = 0;
    private int numWaves = 0;
    private int allowedToSpawn = TOTAL_TO_SPAWN;
    private int score = 0;
    private HashMap<Integer, Integer> colorCounter;
    private Sprite[] weaponSelectorSprite;


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
        colorCounter = new HashMap<Integer, Integer>();
        colorCounter.put(GameEntity.RED, 0);
        colorCounter.put(GameEntity.GREEN, 0);
        colorCounter.put(GameEntity.BLUE, 0);

        weaponSelectorSprite = new Sprite[NUM_COLORS];
        weaponSelectorSprite[0] = new Sprite(new Texture("sponge-red.png"));
        weaponSelectorSprite[0].setBounds(0,0,weaponSelectorSprite[0].getWidth()*SPONGE_TEXTURE_SCALE, weaponSelectorSprite[0].getHeight()*SPONGE_TEXTURE_SCALE);
        weaponSelectorSprite[1] = new Sprite(new Texture("sponge-green.png"));
        weaponSelectorSprite[1].setBounds(0, 0, weaponSelectorSprite[1].getWidth() * SPONGE_TEXTURE_SCALE, weaponSelectorSprite[1].getHeight() * SPONGE_TEXTURE_SCALE);
        weaponSelectorSprite[2] = new Sprite(new Texture("sponge-blue.png"));
        weaponSelectorSprite[2].setBounds(0, 0, weaponSelectorSprite[2].getWidth() * SPONGE_TEXTURE_SCALE, weaponSelectorSprite[2].getHeight() * SPONGE_TEXTURE_SCALE);

        //Texture Variables
        spriteBatch = new SpriteBatch();
        background = new Texture("mouth.png");

        //Game Entity constructor
            // Texture spr
            // String cls,
            // float scale,
            // int screenWidth,
            // int screenHeight,
            // float move
        sponge = new GameEntity(new Texture("sponge.png"), GameEntity.PLAYER, SPONGE_TEXTURE_SCALE, screenWidth, screenHeight, SPONGE_MOVE_SPEED);
        planktons = new Array<GameEntity>();

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

        final Label redLabel = new Label("Red: "+colorCounter.get(GameEntity.RED),screenSkin);
        redLabel.setAlignment(2);
        redLabel.setWidth(TITLE_LABEL_WIDTH * screenWidth);
        redLabel.setHeight(TITLE_LABEL_HEIGHT * screenHeight);
        redLabel.setFontScale(TITLE_LABEL_TEXT_SCALE);
        redLabel.setX(titleLabelOffsetX);
        redLabel.setY(currY);

        final Label blueLabel = new Label("Blue: "+colorCounter.get(GameEntity.GREEN),screenSkin);
        blueLabel.setAlignment(2);
        blueLabel.setWidth(TITLE_LABEL_WIDTH * screenWidth);
        blueLabel.setHeight(TITLE_LABEL_HEIGHT * screenHeight);
        blueLabel.setFontScale(TITLE_LABEL_TEXT_SCALE);
        blueLabel.setX(titleLabelOffsetX);
        blueLabel.setY(currY);

        final Label greenLabel = new Label("Green: "+colorCounter.get(GameEntity.BLUE),screenSkin);
        greenLabel.setAlignment(2);
        greenLabel.setWidth(TITLE_LABEL_WIDTH * screenWidth);
        greenLabel.setHeight(TITLE_LABEL_HEIGHT * screenHeight);
        greenLabel.setFontScale(TITLE_LABEL_TEXT_SCALE);
        greenLabel.setX(titleLabelOffsetX);
        greenLabel.setY(currY);

        gameTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                String gameMessage = "";
                if(secondsElapsed < -1){
                    gameMessage = "Starting in "+ ((-secondsElapsed)-1) + "!";
                } else if(secondsElapsed == -1){
                    gameMessage = "Go!!!";
                } else {
                    if(secondsElapsed == 0){
                        float currX = TITLE_LABEL_OFFSET_X * screenWidth;
                        titleLabel.setX(currX);
                        currX = currX + titleLabel.getWidth() + TITLE_LABEL_OFFSET_X * screenWidth;
                        scoreLabel.setX(currX);
                        currX = currX + titleLabel.getWidth() + LABEL_TRACKER_OFFSET * screenWidth;
                        redLabel.setX(currX);
                        currX = currX + titleLabel.getWidth() + LABEL_TRACKER_OFFSET * screenWidth;
                        greenLabel.setX(currX);
                        currX = currX + titleLabel.getWidth() + LABEL_TRACKER_OFFSET * screenWidth;
                        blueLabel.setX(currX);

                        scoreLabelTracker = scoreLabel;
                        redLabelTracker = redLabel;
                        greenLabelTracker = greenLabel;
                        blueLabelTracker = blueLabel;

                        screenStage.addActor(scoreLabelTracker);
                        screenStage.addActor(redLabelTracker);
                        screenStage.addActor(greenLabelTracker);
                        screenStage.addActor(blueLabelTracker);
                        spawnWave();
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
        Sprite currWeapon = weaponSelectorSprite[game.getWeaponColor()-1];
        Gdx.gl.glClearColor(0, 0, 0, screenAlpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, screenWidth, screenHeight);

        spriteBatch.draw(currWeapon, 0, 0, currWeapon.getWidth(), currWeapon.getHeight());
        int ctr = 0;
        Iterator<GameEntity> iter = planktons.iterator();
        while(iter.hasNext()) {
            GameEntity plankton = iter.next();
            plankton.randomMove(delta);

            if(game.getIsTouched() && plankton.getEntityClass() == game.getWeaponColor()){
                plankton.setAlpha(Math.max(plankton.getColor().a - PLANKTON_DECAY, 0));
            } else {
                plankton.setAlpha(Math.min(plankton.getColor().a + PLANKTON_DECAY, 1));
            }
            if(plankton.getColor().a <= 0){
                int currColorCount = colorCounter.get(plankton.getEntityClass());
                colorCounter.put(plankton.getEntityClass(), currColorCount-1);
                if(plankton.getEntityClass() == GameEntity.RED){
                    redLabelTracker.setText("Red: "+colorCounter.get(plankton.getEntityClass()));
                } else if(plankton.getEntityClass() == GameEntity.GREEN) {
                    greenLabelTracker.setText("Green: "+colorCounter.get(plankton.getEntityClass()));
                } else if(plankton.getEntityClass() == GameEntity.BLUE){
                    blueLabelTracker.setText("Blue: "+colorCounter.get(plankton.getEntityClass()));
                }

                plankton.getTexture().dispose();
                iter.remove();
                score += 1;
                if(scoreLabelTracker != null){
                    scoreLabelTracker.setText("Score: "+score);
                }
            }
            /*
            if(plankton.overlaps(sponge)){
                plankton.getSprite().dispose();
                iter.remove();
                score += 1;
                if(scoreLabelTracker != null){
                    scoreLabelTracker.setText("Score: "+score);
                }
            } else {
                spriteBatch.draw(plankton.getSprite(), plankton.x, plankton.y, plankton.width, plankton.height);
                ctr = ctr + 1;
            }
            */
            plankton.draw(spriteBatch);
            ctr = ctr + 1;
        }

        sponge.draw(spriteBatch);
        spriteBatch.end();

        screenStage.act(delta);
        screenStage.draw();

        if(Math.abs(Gdx.input.getDeltaX()) > 10.0f){
            int weapColor = (game.getWeaponColor()%NUM_COLORS + 1) ;
            game.setWeaponColor(weapColor);
        }

        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            sponge.setPosTo((int) touchPos.x, (int) touchPos.y);
            sponge.randomMove(delta);
            sponge.setScale(0.5f);
            game.setIsTouched(true);
        } else {
            sponge.setScale(0.75f);
            game.setIsTouched(false);
        }

        if(planktons.size == 0 && secondsElapsed > FINAL_WAVE_TIME){
            endGame();
        }
        for(int i = 1;i < NUM_COLORS;i++){
            if(colorCounter.get(i) > MAX_PER_COLOR){
                endGame();
            }
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

            float x = MathUtils.random(0, screenWidth - PLANKTON_INIT_OFFSET_X*screenWidth);
            float y = MathUtils.random(0, screenHeight - PLANKTON_INIT_OFFSET_Y*screenHeight);

            int entityClass = MathUtils.random(1,NUM_COLORS);
            String path = "";
            if(entityClass == GameEntity.BLUE){
                path = BLUE_PLANKTON_PATH;
            } else if (entityClass == GameEntity.GREEN){
                path = GREEN_PLANKTON_PATH;
            } else if (entityClass == GameEntity.RED){
                path = RED_PLANKTON_PATH;
            }
            Texture planktonTexture = new Texture(path+MathUtils.random(1, NUM_PLANKTONS_TO_LOAD)+".png");
            float width = planktonTexture.getWidth() * PLANKTON_TEXTURE_SCALE;
            float height = planktonTexture.getHeight() * PLANKTON_TEXTURE_SCALE;

            GameEntity plankton = new GameEntity(planktonTexture);
            plankton.setEntityClass(entityClass);
            plankton.setBounds(x, y, width, height);
            plankton.setSpriteScale(PLANKTON_TEXTURE_SCALE);
            plankton.setScreenBounds((int) screenWidth, (int) screenHeight);
            plankton.setMoveSpeed(PLANKTON_MOVE_SPEED);

            int currColorCount = colorCounter.get(plankton.getEntityClass());
            colorCounter.put(plankton.getEntityClass(), currColorCount + 1);
            if(plankton.getEntityClass() == GameEntity.RED){
                redLabelTracker.setText("Red: "+colorCounter.get(plankton.getEntityClass()));
            } else if(plankton.getEntityClass() == GameEntity.GREEN) {
                greenLabelTracker.setText("Green: "+colorCounter.get(plankton.getEntityClass()));
            } else if(plankton.getEntityClass() == GameEntity.BLUE){
                blueLabelTracker.setText("Blue: "+colorCounter.get(plankton.getEntityClass()));
            }


            planktons.add(plankton);
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
    }
}
