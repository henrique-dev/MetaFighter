package com.br.phdev.metafighter;

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

import com.br.phdev.metafighter.cmp.Component;
import com.br.phdev.metafighter.cmp.connections.packets.Packet;
import com.br.phdev.metafighter.cmp.event.handlers.AutoDestroyableHandler;
import com.br.phdev.metafighter.cmp.game.Character;
import com.br.phdev.metafighter.cmp.graphics.Texture;
import com.br.phdev.metafighter.cmp.misc.Constant;
import com.br.phdev.metafighter.cmp.misc.GameContext;
import com.br.phdev.metafighter.cmp.window.BackGround;
import com.br.phdev.metafighter.cmp.window.LoadingScreen;
import com.br.phdev.metafighter.cmp.window.Popup;
import com.br.phdev.metafighter.cmp.window.ProgressHud;
import com.br.phdev.metafighter.cmp.window.Screen;
import com.br.phdev.metafighter.screens.MainScreen;
import com.br.phdev.metafighter.screens.MatchScreen;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class GameEngine extends SurfaceView implements SurfaceHolder.Callback{

    private MainThread thread;

    protected ConnectionManager connectionManager;

    private SoundManager soundManager;

    private LoadingScreen loadingScreen;
    private Screen screen;
    private Component message;

    private Paint debugPaint;

    private BluetoothManager bluetoothManager;

    public GameEngine(Context context) {
        super(context);

        Log.e("GameEngine", "AQUIII");

        getHolder().addCallback(this);

        if (thread == null)
            this.thread = new MainThread(getHolder(), this);
            Log.d("GameEngine", "GameEngine: Thread CRIADA");

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Surface criada.");
        Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Tela principal criada.");

        try{
            if (thread != null) {
                thread.setRunning(true);

                initBasicComponents();

                Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Iniciando a thread.");
                thread.start();
            }
        }
        catch (Exception e){}
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
                if (thread != null) {
                    this.thread.setRunning(false);
                    this.thread.join();
                }
                retry = false;
                screen = null;
                if (soundManager != null) {
                    soundManager.release();
                    soundManager = null;
                }
            }
            catch (InterruptedException e){
                Log.e("GameEngine", e.getMessage());
                Log.e("GameEngine", e.getCause().toString());
                //e.printStackTrace();
            }

        }
    }

    public void stopPreview(){
        if (thread != null)
            thread = null;
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

        this.loadingScreen = new LoadingScreen(new BackGround(screenSize, new Texture("images/backgrounds/1.png")), progressHud);

        connectionManager = new ConnectionManager(gameContext);

        soundManager = new SoundManager(getContext());



        new MainScreen(gameContext);
        //new MatchScreen(gameContext, Constant.GAMEMODE_SINGLEPLAYER, Character.GUEDES, Character.GUEDES);
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

        if (loadingScreen != null)
            if (loadingScreen.isVisible())
                loadingScreen.update();

        if (message != null)
            message.update();

    }

    public void processPackets(Packet packet){
        if (packet == null) {
            //Log.d("GameEngine", "processPackets: NULL");
            return;
        }
        screen.processPackets(packet);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        if (screen != null && message == null)
            screen.onTouchEvent(event);
        if (message != null)
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
                message = null;

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

                        if (connectionManager.isActive()) {
                            connectionManager.sendPackets();
                            processPackets(connectionManager.receivePackets());
                        }
                    }
                }
                catch(Exception e){
                    Log.e("GameEngine", "ERRO: " + " - " + (e.getMessage() == null ? "" : e.getMessage()));
                    //Log.e("GameEngine", e.getCause().toString());
                    //e.printStackTrace();
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

    public final GameContext gameContext = new GameContext() {

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
        public LoadingScreen getProgressCmp(){
            return loadingScreen;
        }

        @Override
        public void screenUpdate(Screen sc) {
            screen = sc;
        }

        @Override
        public ConnectionManager getConnectionType() {
            return connectionManager;
        }

        @Override
        public SoundManager getSoundManager(){
            return soundManager;
        }

        @Override
        public Context getAppContetxt(){
            return getContext();
        }
    };




}
