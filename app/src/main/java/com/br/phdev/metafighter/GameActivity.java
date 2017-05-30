package com.br.phdev.metafighter;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

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

        setupParameters();

        gameEngine = new GameEngine(this);

        Log.d("GameEngine", "onCreate: " + getPackageName());

        super.setContentView(gameEngine);

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e("GameEngine", "RESUMIU");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.e("GameEngine", "PAUSOU");
        gameEngine.stopPreview();
    }

    private void setupParameters() {

        GameParameters.getInstance().orientation = LANDSCAPE;
        GameParameters.getInstance().assetManager = getAssets();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        GameParameters.getInstance().screenSize = new RectF(0, 0, size.x, size.y);
        GameParameters.getInstance().screenWidth = size.x;
        GameParameters.getInstance().screenHeight = size.y;

        GameParameters.getInstance().defaultTextSize = (size.y)/28;



    }

}
