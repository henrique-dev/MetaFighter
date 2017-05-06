package phdev.com.br.metafighter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.RectF;
import android.util.Log;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class GameParameters {

    public int logIndex = 0;

    public boolean debug = false;

    public int orientation;

    public int screenWidth;

    public int screenHeight;

    public AssetManager assetManager;

    public RectF screenSize;

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
