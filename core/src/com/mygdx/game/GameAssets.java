package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by G on 11/22/2015.
 */
public class GameAssets {
    public static final int BORDER_WIDTH = 6;
    public static BitmapFont generateFont(String path, int size){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = BORDER_WIDTH;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();
        return font;
    }

    public static final Color FLAT_BLUE = Color.valueOf("3498dbff");
    public static final Color FLAT_WHITE = Color.valueOf("ecf0f1FF");
    public static final Color FLAT_PURPLE = Color.valueOf("9b59b6FF");
    public static final Color FLAT_YELLOW = Color.valueOf("f1c40fFF");
    public static final Color FLAT_ORANGE = Color.valueOf("f39c12FF");
    public static final Color FLAT_BLUE_DARK = Color.valueOf("2980b9FF");
}
