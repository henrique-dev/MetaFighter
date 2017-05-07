package phdev.com.br.metafighter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import phdev.com.br.metafighter.cmp.Component;
import phdev.com.br.metafighter.cmp.WindowEntity;
import phdev.com.br.metafighter.cmp.event.handlers.AutoDestroyableHandler;
import phdev.com.br.metafighter.cmp.event.handlers.MessageHandler;
import phdev.com.br.metafighter.cmp.event.listeners.IntentListener;
import phdev.com.br.metafighter.cmp.event.listeners.ProgressListener;
import phdev.com.br.metafighter.cmp.event.listeners.ScreenUpdateListener;
import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.LoadingScreen;
import phdev.com.br.metafighter.cmp.window.Popup;
import phdev.com.br.metafighter.cmp.window.ProgressHud;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.connections.packets.Packet;
import phdev.com.br.metafighter.screens.MainScreen;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class GameEngine extends SurfaceView implements SurfaceHolder.Callback{

    private MainThread thread;

    private LoadingScreen loadingScreen;
    private Component screen;
    private Component message;

    private Paint debugPaint;

    public GameEngine(Context context) {
        super(context);

        getHolder().addCallback(this);

        this.thread = new MainThread(getHolder(), this);

        setFocusable(true);
    }

    public void showIntent(Intent intent){
        getContext().startActivity(intent);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Surface criada.");
        Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Tela principal criada.");
        this.thread.setRunning(true);

        initBasicComponents();

        Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Iniciando a thread.");
        this.thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Surface alterada.");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Surface destruida.");

        boolean retry = true;
        while(retry){
            try{
                retry = false;
                this.thread.setRunning(false);
                this.thread.join();

            }
            catch (InterruptedException e){
                Log.e("GameEngine", e.getMessage());
                Log.e("GameEngine", e.getCause().toString());
                //e.printStackTrace();
            }
            finally {
                if(!this.thread.running){
                    Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Zerando a lista de telas.");
                    screen = null;
                }
            }

        }
    }

    private void initBasicComponents(){

        ProgressHud progressHud;

        this.debugPaint = new Paint();
        this.debugPaint.setColor(Color.YELLOW);
        this.debugPaint.setTextSize(50);

        RectF screenSize = GameParameters.getInstance().screenSize;
        float divX = screenSize.width()/8;
        float divY = screenSize.height()/8;
        RectF areaProgressHud = new RectF(divX, divY*5, screenSize.right - divX, screenSize.bottom - divY*2);
        progressHud = new ProgressHud(new Texture("cmp/progessHud/hud.png"), areaProgressHud);

        this.loadingScreen = new LoadingScreen(new BackGround(screenSize, new Texture("images/backgrounds/background7.png")), progressHud);

        new MainScreen(new HandlerScreen());

        //components.add()
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas){

        if (canvas != null){
            final int savedState = canvas.save();

            canvas.drawColor(Color.BLACK);

            if (screen != null)
                screen.draw(canvas);

            if (message != null)
                message.draw(canvas);

            if (loadingScreen != null)
                if (loadingScreen.isVisible())
                    loadingScreen.draw(canvas);


            canvas.restoreToCount(savedState);
        }
    }

    public void update(){

        if (screen != null)
            screen.update();


        if (message != null)
            message.update();

    }

    public void receivePacket(Packet packet){
        
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        if (screen != null && message == null)
            screen.onTouchEvent(event);

        return true;
    }

    private class MainThread extends Thread{

        private int FPS = 60;
        private int averageFPS;
        private final SurfaceHolder surfaceHolder;
        private GameEngine gameEngine;
        private boolean running;
        private Canvas canvas;

        // Metodo construtor que recebe e inicializa a surfaceHolder e a gameEngine.
        MainThread(SurfaceHolder surfaceHolder, GameEngine gameEngine){
            super();
            this.surfaceHolder = surfaceHolder;
            this.gameEngine = gameEngine;
        }

        // Metodo da thread responsavel pelo seu funcionamento.
        @Override
        public void run(){

            //**************************************************************
            // Variaveis responsaveis pelo sistema de quadros do jogo.
            long startTime;
            long timeMillis;
            long waitTime;
            long totalTime = 0;
            long frameCount = 0;
            long targetTime = 1000/FPS;
            //**************************************************************

            //**************************************************************
            // Loop do motor do jogo.
            while(running){
                startTime = System.nanoTime();
                canvas = null;
                try{
                    canvas = this.surfaceHolder.lockCanvas();


                    synchronized (surfaceHolder){
                        this.gameEngine.update();
                        this.gameEngine.draw(canvas);
                    }
                }
                catch(Exception e){
                    Log.e("GameEngine", e.getLocalizedMessage() == null ? "" : e.getLocalizedMessage());
                    //Log.e("GameEngine", e.getCause().toString());
                    e.printStackTrace();
                }
                finally {
                    if(canvas != null){
                        try{
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                timeMillis = (System.nanoTime() - startTime) / 1000000;
                waitTime = targetTime - timeMillis;
                try{
                    sleep(waitTime);
                }
                catch (Exception e){
                    //e.printStackTrace();
                }
                totalTime += System.nanoTime() - startTime;
                frameCount++;
                if(frameCount  == FPS){
                    averageFPS = (int)(1000/((totalTime/frameCount)/1000000));
                    frameCount = 0;
                    totalTime = 0;
                }
            }
        }

        protected void setRunning(boolean running) {
            this.running = running;
        }

        protected int getAverageFPS(){
            return this.averageFPS;
        }
    }

    public class HandlerScreen extends MessageHandler implements IntentListener, ProgressListener, ScreenUpdateListener{

        @Override
        public void sendIntentRequest(Intent intent) {
            getContext().startActivity(intent);
        }

        @Override
        public void sendMessage(final String msg, final int duration) {
            message = new Popup(msg, new AutoDestroyableHandler() {
                @Override
                public void autoDestroy(Object entity) {
                    try{
                        message = null;
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }, duration);
        }

        @Override
        public void progressUpdate(int value) {
            loadingScreen.increaseProgress(value);
        }

        @Override
        public void progressPrepare() {
            loadingScreen.setVisible(true);
        }

        @Override
        public void progressFinalize() {
            loadingScreen.setVisible(false);
            loadingScreen.resetProgress();
        }

        @Override
        public void screenUpdate(Screen sc) {
            screen = sc;
        }
    }




}
