package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by G on 11/22/2015.
 */
public class MainMenuScreen extends GameScreen {
    private float screenCenterX = screenWidth/2;
    private float screenCenterY = screenHeight/2;

    private static final String MERMAID_FONT_PATH = "fonts/Mermaid1001.ttf";
    private static final int MERMAID_FONT_SIZE = 72;

    private static final float MENU_BUTTON_WIDTH = 0.25f;
    private static final float MENU_BUTTON_HEIGHT = 0.10f;
    private static final float MENU_BUTTON_OFFSET_Y = 0.05f;
    private static final float MENU_BUTTON_OFFSET_X = 0.2f;
    private static final float MENU_BUTTON_TEXT_SCALE = 0.75f;

    private static final float TITLE_LABEL_WIDTH = 0.75f;
    private static final float TITLE_LABEL_HEIGHT = 0.15f;
    private static final float TITLE_LABEL_OFFSET_Y = 0.15f;
    private static final float TITLE_LABEL_OFFSET_X = 0.2f;
    private static final float TITLE_LABEL_TEXT_SCALE = 1.0f;

    private static final float INITIAL_OFFSET_Y = 0.05f;

    private MainGame game;
    private Stage screenStage;
    private Skin screenSkin;

    public MainMenuScreen(MainGame g){
        game = g;
    }

    @Override
    public void show() {
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        screenCenterX = screenWidth/2;
        screenCenterY = screenHeight/2;

        screenStage = new Stage();
        screenSkin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        screenSkin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        BitmapFont bFont = GameAssets.generateFont(MERMAID_FONT_PATH, MERMAID_FONT_SIZE);
        screenSkin.add("default", bFont);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        //labelStyle.background = screenSkin.newDrawable("white", GameAssets.FLAT_BLUE);
        labelStyle.font = screenSkin.getFont("default");
        labelStyle.font.setUseIntegerPositions(true);
        screenSkin.add("default", labelStyle);

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = screenSkin.newDrawable("white", GameAssets.FLAT_BLUE);
        textButtonStyle.down = screenSkin.newDrawable("white", GameAssets.FLAT_BLUE_DARK);
        textButtonStyle.checked = screenSkin.newDrawable("white", GameAssets.FLAT_BLUE);
        textButtonStyle.over = screenSkin.newDrawable("white", GameAssets.FLAT_BLUE);
        textButtonStyle.font = screenSkin.getFont("default");
        screenSkin.add("default", textButtonStyle);

        float currY = screenHeight - ((TITLE_LABEL_HEIGHT + INITIAL_OFFSET_Y) * screenHeight);
        float buttonOffsetX = screenCenterX - (MENU_BUTTON_WIDTH*screenWidth/2);
        float titleLabelOffsetX = screenCenterX - (TITLE_LABEL_WIDTH*screenWidth/2);

        Label titleLabel = new Label("Dumb ways to brush!",screenSkin);
        titleLabel.setAlignment(2);
        titleLabel.setWidth(TITLE_LABEL_WIDTH * screenWidth);
        titleLabel.setHeight(TITLE_LABEL_HEIGHT * screenHeight);
        titleLabel.setFontScale(TITLE_LABEL_TEXT_SCALE);
        titleLabel.setX(titleLabelOffsetX);
        titleLabel.setY(currY);
        currY -= titleLabel.getHeight();
        currY -= TITLE_LABEL_OFFSET_Y * screenHeight;
        screenStage.addActor(titleLabel);

        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        TextButton startButton = new TextButton("PLAY", screenSkin);
        startButton.setWidth(MENU_BUTTON_WIDTH * screenWidth);
        startButton.setHeight(MENU_BUTTON_HEIGHT * screenHeight);
        startButton.getLabel().setFontScale(MENU_BUTTON_TEXT_SCALE);
        startButton.setX(buttonOffsetX);
        startButton.setY(currY);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.resetGameData();
                game.setGameScreen(MainGame.mainGameScreenID, MainGame.mainMenuScreenID);
            }

            ;
        });

        currY -= startButton.getHeight();
        currY -= MENU_BUTTON_OFFSET_Y * screenHeight;
        screenStage.addActor(startButton);




        TextButton optionsButton = new TextButton("Options", screenSkin);
        optionsButton.setWidth(MENU_BUTTON_WIDTH * screenWidth);
        optionsButton.setHeight(MENU_BUTTON_HEIGHT * screenHeight);
        optionsButton.getLabel().setFontScale(MENU_BUTTON_TEXT_SCALE);
        optionsButton.setX(buttonOffsetX);
        optionsButton.setY(currY);
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.resetGameData();
                game.setGameScreen(MainGame.optionsScreenID, MainGame.mainMenuScreenID);
            }

            ;
        });
        currY -= startButton.getHeight();
        currY -= MENU_BUTTON_OFFSET_Y * screenHeight;
        screenStage.addActor(optionsButton);

        TextButton exitButton = new TextButton("Exit Game", screenSkin);
        exitButton.setWidth(MENU_BUTTON_WIDTH * screenWidth);
        exitButton.setHeight(MENU_BUTTON_HEIGHT * screenHeight);
        exitButton.getLabel().setFontScale(MENU_BUTTON_TEXT_SCALE);
        exitButton.setX(buttonOffsetX);
        exitButton.setY(currY);
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.resetGameData();
                game.exit();
            };
        });
        currY -= exitButton.getHeight();
        screenStage.addActor(exitButton);

        Gdx.input.setInputProcessor(screenStage);

    }

    @Override
    public void render(float delta) {
        Color flatWhite = GameAssets.FLAT_ORANGE;
        Gdx.gl.glClearColor(flatWhite.r, flatWhite.g, flatWhite.b, screenAlpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        screenStage.act(delta);
        screenStage.draw();

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
        screenSkin.dispose();
        screenStage.dispose();
    }
}
