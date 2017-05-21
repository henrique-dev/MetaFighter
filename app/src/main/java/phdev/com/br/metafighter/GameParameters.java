package phdev.com.br.metafighter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.RectF;
import android.util.Log;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public final class GameParameters {

    public int logIndex = 0;

    public boolean debug = true;

    public int maxPlayers = 2;

    public int orientation;

    public RectF screenSize;
    public int screenWidth;
    public int screenHeight;

    public float fontSizeButton;

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
