package com.br.phdev.metafighter;

import android.content.res.AssetManager;
import android.graphics.RectF;
import android.util.Log;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class GameParameters {

    public int logIndex = 0;

    public boolean debug = true;
    public boolean debugCreateObjects = false;
    public boolean debugCreateImages = false;

    public int orientation;

    public RectF screenSize;
    public int screenWidth;
    public int screenHeight;

    public float defaultTextSize;

    public AssetManager assetManager;

    private static final GameParameters ourInstance = new GameParameters();

    public static GameParameters getInstance() {
        return ourInstance;
    }

    private GameParameters() {
    }

    public void log(String msg){
        Log.i("GameEngine", logIndex++ + ": " + msg);
    }
}
