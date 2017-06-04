package com.br.phdev.metafighter.cmp.game;

import android.graphics.RectF;
import android.util.Log;

import com.br.phdev.metafighter.GameParameters;
import com.br.phdev.metafighter.cmp.event.listeners.ControllerListener;
import com.br.phdev.metafighter.cmp.misc.Timer;
import com.br.phdev.metafighter.screens.MatchScreen;

import java.util.Random;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Bot {

    private Player player;
    private Player bot;

    private ControllerListener controller;
    private Timer timer;
    private int currentTime;
    private boolean startTask = false;

    private IAThread iaThread;

    private Random rand;

    private Bot instance;

    public Bot(Player player1, Player player2){

        bot = player2;
        player = player1;

        rand = new Random();

        controller = bot.getControllerListener();
        timer = new Timer().start();
        currentTime = (int)(timer.getTicks()/1000000000);

        instance = this;

    }

    public void start(){
        if (iaThread == null)
            iaThread = new IAThread();

        iaThread.running = true;
        iaThread.start();
    }

    public void stop(){
        iaThread.running = false;
        iaThread = null;
    }


    private void avancaNoJogador(){
        if (!bot.isInvert())
            controller.arrowRightPressed();
        else
            controller.arrowLeftPressed();
    }

    private void paraMovimento(){
        if (bot.getCurrentAction() == Player.WALKING_LEFT_ACTION || bot.getCurrentAction() == Player.WALKING_RIGHT_ACTION) {
            controller.arrowLeftReleased();
            controller.arrowRightReleased();
        }
    }

    private void soco(){
        controller.action1Performed();
    }

    private void chute(){
        controller.action2Performed();
    }

    protected boolean checkCollision(Object objA, Object objB, float percentClose){

        if (objA instanceof Player && objB instanceof  Player){

            Player A = (Player)objA;
            Player B = (Player)objB;

            float AX = A.getX();
            float BX = B.getX();

            float divX = A.getMainArea().width()/percentClose;

            if (!A.isInvert()){
                if (AX >= BX - divX)
                    return true;
            }
            else
                if (AX <= BX + divX)
                    return true;
        }
        return false;
    }

    private class IAThread extends Thread{

        boolean running;

        @Override
        public void run(){

            while (running){

                int tmpTime = (int)(timer.getTicks()/100000000);

                if (tmpTime > currentTime){
                    currentTime+= 5;

                    Log.d("GameEngine", "run: executando");

                    if (checkCollision(bot, player, Player.NORMAL_DISTANCE)){
                        paraMovimento();
                        if (rand.nextInt(50) > 25){
                            avancaNoJogador();
                        }
                        if (checkCollision(bot, player, Player.VERYCLOSE_DISTANCE)){
                            if (checkCollision(bot, player, Player.KICK_DISTANCE)){
                                paraMovimento();
                                if (rand.nextInt(50) < 10) {
                                    chute();
                                }
                                else
                                if (checkCollision(bot, player, Player.PUNCH_DISTANCE)){
                                    if (rand.nextInt(50) < 15){
                                        soco();
                                    }
                                }
                                else{
                                    if (rand.nextInt(50) < 10) {
                                        avancaNoJogador();
                                    }
                                }
                            }
                            else {
                                avancaNoJogador();
                            }
                        }
                    }
                    else{
                        if (rand.nextInt(500) < 85) {
                            avancaNoJogador();
                        }
                        else
                        if (rand.nextInt(200) > 50) {
                            paraMovimento();
                        }
                    }

                }

                if (checkCollision(bot, player, Player.PUNCH_DISTANCE))
                    paraMovimento();
            }

        }

    }

}
