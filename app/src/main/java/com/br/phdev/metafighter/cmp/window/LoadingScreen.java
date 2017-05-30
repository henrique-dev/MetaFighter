package com.br.phdev.metafighter.cmp.window;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.br.phdev.metafighter.GameParameters;
import com.br.phdev.metafighter.cmp.Component;
import com.br.phdev.metafighter.cmp.misc.Timer;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class LoadingScreen implements Component {

    private boolean visible;
    private BackGround backGround;
    private ProgressHud progressHud;
    private Text text;
    private Timer timer;
    private int currentTime;

    public LoadingScreen(BackGround backGround, ProgressHud progressHud){
        this.backGround = backGround;
        this.progressHud = progressHud;
        text = new Text("Carregando");
        float defaultTextSize = GameParameters.getInstance().defaultTextSize;
        //RectF textArea = new RectF(progressHud.getArea().left, progressHud.getArea().top + defaultTextSize*2, progressHud.getArea().right, progressHud.getArea().top);
        text.setTextSize(defaultTextSize * 2);
        text.setColor(Color.WHITE);
        text.setDrawableArea(new RectF(progressHud.getArea()));
        timer = new Timer().start();
        currentTime = 0;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void increase(int value){
        this.progressHud.increase(value);
    }

    public void start(){
        visible = true;
    }

    public void stop(){
        visible = false;
        progressHud.reset();
    }

    @Override
    public void draw(Canvas canvas) {
        backGround.draw(canvas);
        progressHud.draw(canvas);
        text.draw(canvas);
    }

    @Override
    public void update() {
        int tmpTime = (int)(timer.getTicks()/1000000000);
        if (tmpTime > currentTime){
            currentTime = tmpTime;
            String tmpText = text.getText();
            if (tmpText.equals("Carregando"))
                text.setText("Carregando.");
            else
                if (tmpText.equals("Carregando."))
                    text.setText("Carregando..");
                else
                    if (tmpText.equals("Carregando.."))
                        text.setText("Carregando...");
                    else
                        if (tmpText.equals("Carregando..."))
                            text.setText("Carregando");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        return false;
    }
}
