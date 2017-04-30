package phdev.com.br.metafighter.models;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.GameParameters;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public abstract class Screen implements Component{

    private List<Component> entities;
    private ProgressHud progressHud;

    public Screen(){
        entities = new ArrayList<>();
        progressHud = new ProgressHud(new RectF());

        entities.add(progressHud);

        try{
            this.init();
        }
        catch (Exception ex){
            Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": ERROR: " + ex.getMessage());
        }

    }

    private void init(){


        new Thread(
                new Runnable(){
                    @Override
                    public void run() {
                        try{
                            if (loadTextures()) {
                                Thread.sleep(500);
                                progressHud.setProgress(35);
                                if (loadFonts()) {
                                    Thread.sleep(500);
                                    progressHud.setProgress(70);
                                    if (loadSounds()) {
                                        Thread.sleep(500);
                                        progressHud.setProgress(100);
                                    }
                                }
                            }
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
            }
        ).start();


        /*
        if (loadTextures()) {
            progressHud.setProgress(35);
            if (loadFonts()) {
                progressHud.setProgress(70);
                if (loadSounds()) {
                    progressHud.setProgress(100);
                }
            }
        }
        */


    }

    protected abstract boolean loadTextures();

    protected abstract boolean loadFonts();

    protected abstract boolean loadSounds();

    protected void add(Component cmp){
        this.entities.add(cmp);
    }

    protected void remover(Component cmp){
        this.entities.remove(cmp);
    }

    @Override
    public void draw(Canvas canvas) {
        for (Component cmp : entities)
            cmp.draw(canvas);

    }

    @Override
    public void update() {
        for (Component cmp : entities)
            cmp.update();
        //Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Progresso: " + progressHud.getProgress());
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        for (Component cmp : entities)
            cmp.onTouchEvent(evt);
        return true;
    }
}
