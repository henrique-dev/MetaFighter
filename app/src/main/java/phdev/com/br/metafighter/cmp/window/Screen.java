package phdev.com.br.metafighter.cmp.window;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.Component;
import phdev.com.br.metafighter.cmp.event.listeners.EventListener;
import phdev.com.br.metafighter.cmp.event.listeners.MessageListener;
import phdev.com.br.metafighter.cmp.event.listeners.ProgressListener;
import phdev.com.br.metafighter.cmp.event.listeners.ScreenUpdateListener;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public abstract class Screen implements Component {

    protected Scene currentScene;

    protected EventListener listener;

    private ScreenUpdateListener screenUpdateListener;
    private ProgressListener progressListener;

    public Screen(EventListener listener){
        this.listener = listener;
        this.screenUpdateListener = (ScreenUpdateListener)listener;
        this.progressListener = (ProgressListener)listener;
        //this.entities = new ArrayList<>();
        currentScene = new Scene();

        try{
            this.init();
        }
        catch (Exception ex){
            Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": ERROR: " + ex.getMessage());
        }

        screenUpdateListener.screenUpdate(this);

    }

    private void init(){

        progressListener.progressPrepare();

        new Thread(
                new Runnable(){
                    @Override
                    public void run() {
                        try{
                            if (loadTextures()) {
                                progressListener.progressUpdate(25);
                                if (loadFonts()) {
                                    progressListener.progressUpdate(50);
                                    if (loadSounds()) {
                                        progressListener.progressUpdate(75);
                                        if (loadComponents()){
                                            progressListener.progressUpdate(100);
                                        }
                                    }
                                }
                            }
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                        progressListener.progressFinalize();
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

    protected abstract boolean loadComponents();

    /*
    protected void add(Component cmp){
        this.entities.add(cmp);
    }

    protected void remover(Component cmp){
        this.entities.remove(cmp);
    }

    public List<Component> getEntities() {
        return entities;
    }

    public void setEntities(List<Component> entities) {
        this.entities = entities;
    }
    */

    @Override
    public void draw(Canvas canvas) {
        /*
        for (Component cmp : entities) {
            cmp.draw(canvas);
            //canvas.clipRect(GameParameters.getInstance().screenSize);
        }
        */
        if (currentScene != null)
            currentScene.draw(canvas);


    }

    @Override
    public void update() {
        /*
        for (Component cmp : entities)
            cmp.update();
        //Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Progresso: " + progressHud.getProgress());
        */
        if (currentScene != null)
            currentScene.update();

    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        /*
        for (Component cmp : entities)
            cmp.onTouchEvent(evt);
            */
        if (currentScene != null)
            currentScene.onTouchEvent(evt);


        return true;
    }

    @Deprecated
    protected static void log(String msg){
        if(!GameParameters.getInstance().debug)
            return;
        GameParameters.getInstance().log(msg);
    }

    @Deprecated
    protected void sendMessageToScreen(String msg){
        log(msg);
        if (listener != null)
            ((MessageListener)listener).sendMessage(msg, 5);
    }
}
