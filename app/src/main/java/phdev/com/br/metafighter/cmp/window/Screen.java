package phdev.com.br.metafighter.cmp.window;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.GameEngine;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.Component;
import phdev.com.br.metafighter.cmp.WindowEntity;
import phdev.com.br.metafighter.cmp.event.listeners.AutoDestroyableListener;
import phdev.com.br.metafighter.cmp.event.listeners.EventListener;
import phdev.com.br.metafighter.cmp.event.listeners.MessageListener;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public abstract class Screen implements Component {

    protected List<Component> entities;
    protected ProgressHud progressHud;
    //protected Context context;
    protected EventListener listener;

    public Screen(EventListener listener){
        //this.context = context;
        this.listener = listener;
        this.entities = new ArrayList<>();

        this.progressHud = new ProgressHud(new RectF());

        this.entities.add(progressHud);

        try{
            this.init();
        }
        catch (Exception ex){
            Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": ERROR: " + ex.getMessage());
        }

        GameEngine.screen = this;

    }

    private void init(){


        new Thread(
                new Runnable(){
                    @Override
                    public void run() {
                        try{
                            if (loadTextures()) {
                                //Thread.sleep(500);
                                progressHud.setProgress(25);
                                if (loadFonts()) {
                                    //Thread.sleep(500);
                                    progressHud.setProgress(50);
                                    if (loadSounds()) {
                                        //Thread.sleep(500);
                                        progressHud.setProgress(75);
                                        if (loadComponents()){
                                            //Thread.sleep(500);
                                            progressHud.setProgress(100);
                                        }
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

    protected abstract boolean loadComponents();

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

    @Override
    public void draw(Canvas canvas) {
        for (Component cmp : entities) {
            cmp.draw(canvas);
            canvas.clipRect(GameParameters.getInstance().screenSize);
        }

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

    /*
    public class AutoDestroyHandler implements AutoDestroyableListener{

        @Override
        public void autoDestroy(WindowEntity entity) {
            entities.remove(entity);
        }
    }
    */
}
