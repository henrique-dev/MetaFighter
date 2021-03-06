package com.br.phdev.metafighter.cmp.event.animation;

import android.graphics.RectF;

import com.br.phdev.metafighter.cmp.Entity;
import com.br.phdev.metafighter.cmp.event.listeners.AnimationListener;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class GoAndBack implements AnimationListener {

    private Entity entity;
    private RectF originalArea;

    public GoAndBack(Entity entity){
        this.entity = entity;
        this.originalArea = new RectF(entity.getArea());
    }

    public void setOriginalArea(RectF originalArea){
        this.originalArea = originalArea;
    }

    public void go(){
        this.entity.move(5,5);
    }

    public void back(){
        this.entity.move(-5,-5);
    }

    public void goAndBack(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    go();
                    Thread.sleep(200);
                    //restore();
                    back();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).run();

    }

    public void restore(){
        this.entity.setArea(new RectF(this.originalArea));
    }


}
