package com.julianbarker.spacetravel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class GameInfo {

    public final static String TITLE = "Space Travel";
    public final static int WIDTH = 480;
    public final static int HEIGHT = 768;

    public static boolean isRunning = true; // default not running

    public enum Scene
    {
        GAME,
        PAUSE,
        GAMEOVER,
        MENU
    }

    public static Scene scene = Scene.MENU; // default state

    // Custom font details
    public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Stfont-Regular.ttf"));
    public static FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
}
