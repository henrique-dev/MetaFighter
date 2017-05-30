package com.br.phdev.metafighter.cmp.misc;
/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Timer {

    private long startTicks;

    private long pausedTicks;

    private boolean started, paused;

    public Timer(){
        // Inicializa as variaveis.
        this.startTicks = 0;
        this.pausedTicks = 0;
        this.paused = false;
        this.started = false;
    }

    public Timer start(){
        // Inicia o timer.
        this.started = true;
        // Despausa o timer.
        this.paused = false;
        // Pega a hora atual.
        this.startTicks = System.nanoTime();
        this.pausedTicks = 0;
        return this;
    }

    public void stop(){
        // Para o timer.
        this.started = false;
        // Despausa o timer.
        this.paused = false;
        // Limpa as variaveis
        this.startTicks = 0;
        this.pausedTicks = 0;
    }

    public void pause(){
        // Se o timer esta rodando e não está pausado.
        if( this.started && !this.paused){
            // Pausa o timer.
            paused = true;

            // Calculao tempo da pausa.
            this.pausedTicks = System.nanoTime() - this.startTicks;
            this.startTicks = 0;
        }
    }

    public void unpause(){
        // Se o timer esta rodando e pausado.
        if(this.started && this.paused){
            // Despausa o timer.
            this.paused = false;
            // Calcula o tempo inicial.
            this.startTicks = System.nanoTime() - this.pausedTicks;
            this.pausedTicks = 0;
        }
    }

    public long getTicks(){
        // O tempo atual.
        long time = 0;
        // Se o timer esta rodando
        if(this.started){
            // Se o timer esta pausado
            if(this.paused){
                time = this.pausedTicks;
            }
            else{
                time = System.nanoTime() - this.startTicks;
            }
        }
        return time;
    }

    public boolean isStarted(){
        return this.started;
    }

    public boolean isPaused(){
        return this.paused;
    }

}
