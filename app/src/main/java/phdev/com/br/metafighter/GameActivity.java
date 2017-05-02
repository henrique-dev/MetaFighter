package phdev.com.br.metafighter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import phdev.com.br.metafighter.cmp.event.MessageListener;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class GameActivity extends Activity{

    private final int LANDSCAPE = 0;
    private final int PORTRAIT = 1;
    private GameEngine gameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.setupParameters();

        gameEngine = new GameEngine(this);

        super.setContentView(gameEngine);

        //BluetoothManager.getInstance().gameEngine = gameEngine;

    }

    private void setupParameters() {

        GameParameters.getInstance().orientation = LANDSCAPE;

        GameParameters.getInstance().assetManager = getAssets();

        this.setRequestedOrientation(GameParameters.getInstance().orientation);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        GameParameters.getInstance().screenSize = new RectF(0, 0, size.x, size.y);
        GameParameters.getInstance().screenWidth = size.x;
        GameParameters.getInstance().screenHeight = size.y;

    }

}
