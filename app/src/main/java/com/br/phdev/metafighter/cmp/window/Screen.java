package com.br.phdev.metafighter.cmp.window;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import com.br.phdev.metafighter.GameParameters;
import com.br.phdev.metafighter.cmp.Component;
import com.br.phdev.metafighter.cmp.connections.packets.Packet;
import com.br.phdev.metafighter.cmp.misc.GameContext;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public abstract class Screen implements Component {

    protected GameContext context;

    protected Scene currentScene;

    protected List<Packet> packets;

    public Screen(GameContext context){
        this.context = context;

        packets = new ArrayList<>();

        //currentScene = new Scene();

        /*
        try {
            this.init();
        } catch (Exception ex) {
            Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": ERROR: " + ex.getMessage());
        }
        */

        context.screenUpdate(this);

    }

    protected final void init(){

        //context.progressPrepare();
        context.getProgressCmp().start();

        new Thread(
                new Runnable(){
                    @Override
                    public void run() {
                        try{
                            if (loadTextures()) {
                                //context.progressUpdate(25);
                                context.getProgressCmp().increase(25);
                                if (loadFonts()) {
                                    context.getProgressCmp().increase(50);
                                    if (loadSounds()) {
                                        context.getProgressCmp().increase(75);
                                        if (loadComponents()){
                                            context.getProgressCmp().increase(100);
                                        }
                                    }
                                }
                            }
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                        //context.progressFinalize();
                        context.getProgressCmp().stop();
                    }
            }
        ).start();


    }

    protected abstract boolean loadTextures();

    protected abstract boolean loadFonts();

    protected abstract boolean loadSounds();

    protected abstract boolean loadComponents();

    @Override
    public void draw(Canvas canvas) {

        if (currentScene != null)
            currentScene.draw(canvas);


    }

    @Override
    public void update() {

        if (currentScene != null)
            currentScene.update();

    }

    public void processPackets(Packet packet){
        currentScene.processPacket(packet);
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {

        if (currentScene != null)
            currentScene.onTouchEvent(evt);


        return true;
    }


    protected static void log(String msg){

        GameParameters.getInstance().log(msg);
    }


    protected void sendMessageToScreen(String msg){
        log(msg);
        context.sendMessage(msg, 5);
    }
}
