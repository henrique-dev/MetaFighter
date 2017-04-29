package phdev.com.br.metafighter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import phdev.com.br.metafighter.models.Component;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
class GameEngine extends SurfaceView implements SurfaceHolder.Callback{

    private MainThread thread;
    private Component screen;
    private Paint debugPaint;

    public GameEngine(Context context) {
        super(context);

        getHolder().addCallback(this);

        this.debugPaint = new Paint();
        this.debugPaint.setColor(Color.YELLOW);
        this.debugPaint.setTextSize(50);
        this.thread = new MainThread(getHolder(), this);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Surface criada.");
        Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Tela principal criada.");
        this.thread.setRunning(true);

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
                    this.screen = null;
                }
            }

        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas){

        if (canvas != null){
            final int savedState = canvas.save();

            canvas.drawColor(Color.BLACK);

            if (this.screen != null)
                this.screen.draw(canvas);

            this.drawDebug(canvas);

            canvas.restoreToCount(savedState);
        }
    }

    private void drawDebug(Canvas canvas){
        if (this.thread != null)
            canvas.drawText(this.thread.getAverageFPS() + "", 40, 40, debugPaint);
    }

    public void update(){

        if (this.screen != null)
            this.screen.update();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        if (this.screen != null)
            this.screen.onTouchEvent(event);

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
                    Log.e("GameEngine", e.getLocalizedMessage());
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


}
