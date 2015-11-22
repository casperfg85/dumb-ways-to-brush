package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
public class EndGameScreen extends GameScreen {
    private float screenCenterX = screenWidth/2;
    private float screenCenterY = screenHeight/2;


    private static final float BACK_BUTTON_WIDTH = 0.35f;
    private static final float BACK_BUTTON_HEIGHT = 0.1f;
    private static final float BACK_BUTTON_OFFSET_Y = 0.05f;
    private static final float BACK_BUTTON_OFFSET_X = 0.05f;
    private static final float BACK_BUTTON_TEXT_SCALE = 3.0f;

    private static final float MENU_BUTTON_WIDTH = 0.35f;
    private static final float MENU_BUTTON_HEIGHT = 0.15f;
    private static final float MENU_BUTTON_OFFSET_Y = 0.05f;
    private static final float MENU_BUTTON_OFFSET_X = 0.2f;
    private static final float MENU_BUTTON_TEXT_SCALE = 3.0f;

    private static final float TITLE_LABEL_WIDTH = 0.75f;
    private static final float TITLE_LABEL_HEIGHT = 0.15f;
    private static final float TITLE_LABEL_OFFSET_Y = 0.15f;
    private static final float TITLE_LABEL_OFFSET_X = 0.2f;
    private static final float TITLE_LABEL_TEXT_SCALE = 4.0f;

    private static final float INITIAL_OFFSET_Y = 0.05f;

    private MainGame game;
    private Stage screenStage;
    private Skin screenSkin;

    public EndGameScreen(MainGame g){
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
        BitmapFont bFont = new BitmapFont();
        screenSkin.add("default",bFont);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = screenSkin.getFont("default");
        labelStyle.background = screenSkin.newDrawable("white", GameColor.FLAT_BLUE_DARK);
        screenSkin.add("default", labelStyle);

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = screenSkin.newDrawable("white", GameColor.FLAT_BLUE_DARK);
        textButtonStyle.down = screenSkin.newDrawable("white", GameColor.FLAT_BLUE_DARK);
        textButtonStyle.checked = screenSkin.newDrawable("white", GameColor.FLAT_BLUE_DARK);
        textButtonStyle.over = screenSkin.newDrawable("white", GameColor.FLAT_BLUE_DARK);
        textButtonStyle.font = screenSkin.getFont("default");
        screenSkin.add("default", textButtonStyle);

        float currY = screenHeight - ((INITIAL_OFFSET_Y+TITLE_LABEL_HEIGHT) * screenHeight);
        float backButtonOffsetX= BACK_BUTTON_OFFSET_X;
        float backButtonOffsetY = BACK_BUTTON_OFFSET_Y;
        float titleLabelOffsetX = screenCenterX - (TITLE_LABEL_WIDTH*screenWidth/2);

        Label titleLabel = new Label("End Game",screenSkin);
        titleLabel.setAlignment(2);
        titleLabel.setWidth(TITLE_LABEL_WIDTH * screenWidth);
        titleLabel.setHeight(TITLE_LABEL_HEIGHT * screenHeight);
        titleLabel.setFontScale(TITLE_LABEL_TEXT_SCALE);
        titleLabel.setX(titleLabelOffsetX);
        titleLabel.setY(currY);
        currY -= titleLabel.getHeight();
        currY -= TITLE_LABEL_OFFSET_Y * screenHeight;
        screenStage.addActor(titleLabel);

        Label scoreLabel = new Label("Score: "+game.getLastGameScore(),screenSkin);
        scoreLabel.setAlignment(2);
        scoreLabel.setWidth(TITLE_LABEL_WIDTH * screenWidth);
        scoreLabel.setHeight(TITLE_LABEL_HEIGHT * screenHeight);
        scoreLabel.setFontScale(TITLE_LABEL_TEXT_SCALE);
        scoreLabel.setX(titleLabelOffsetX);
        scoreLabel.setY(currY);
        currY -= scoreLabel.getHeight();
        currY -= TITLE_LABEL_OFFSET_Y * screenHeight;
        screenStage.addActor(scoreLabel);

        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        TextButton backButton = new TextButton("Back to Main Menu", screenSkin);
        backButton.setWidth(BACK_BUTTON_WIDTH * screenWidth);
        backButton.setHeight(BACK_BUTTON_HEIGHT * screenHeight);
        backButton.getLabel().setFontScale(BACK_BUTTON_TEXT_SCALE);
        backButton.setX(backButtonOffsetX);
        backButton.setY(backButtonOffsetY);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.resetGameData();
                game.setGameScreen(MainGame.mainMenuScreenID, MainGame.endGameScreenID);
            }

            ;
        });
        screenStage.addActor(backButton);


        Gdx.input.setInputProcessor(screenStage);

    }

    @Override
    public void render(float delta) {
        Color flatWhite = GameColor.FLAT_WHITE;
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
