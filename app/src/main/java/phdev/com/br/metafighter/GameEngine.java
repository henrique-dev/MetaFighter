package phdev.com.br.metafighter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
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

    public class MainThread extends Thread{

        // Taxa máxima de quadros por segundo a ser trabalhada.
        private int FPS = 60;
        // Utilizada para armzazenar a taxa atual de quadros por segundo do jogo.
        private double averageFPS;
        // Utilizada para trabalhar com o Canvas e SurfaceView.
        private final SurfaceHolder surfaceHolder;
        // Utilizada para chamar os metodos update() e draw()
        private GameEngine gameEngine;
        // Para definir se o loop está ou não ativo.
        private boolean running;
        // Define o canvas para desenho.
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
                    averageFPS = 1000/((totalTime/frameCount)/1000000);
                    frameCount = 0;
                    totalTime = 0;
                    //System.out.println(averageFPS);
                }
            }
            //**********************************************************************


        }

        // Metodo responsavel pela execução do loop do motor do jogo.
        void setRunning(boolean running) {
            this.running = running;
        }
    }

    private MainThread thread;

    private Component screen;

    public GameEngine(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Surface criada.");


        Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Tela principal criada.");

        thread.setRunning(true);

        Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Iniciando a thread.");

        thread.start();
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
                thread.setRunning(false);
                thread.join();

            }
            catch (InterruptedException e){
                Log.e("GameEngine", e.getMessage());
                Log.e("GameEngine", e.getCause().toString());
                //e.printStackTrace();
            }
            finally {
                // Para e esvazia toda a lista de telas.
                if(!this.thread.running){
                    Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Zerando a lista de telas.");
                    //screens = null;
                    //this.screenManager = null;
                }
            }

        }
    }

    // Metodo responsavel por desenhar na tela.
    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas){

        if (canvas != null){
            final int savedState = canvas.save();

            if (this.screen != null)
                this.screen.draw(canvas);

            canvas.restoreToCount(savedState);
        }
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


}
