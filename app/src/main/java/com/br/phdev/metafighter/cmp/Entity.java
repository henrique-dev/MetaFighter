package com.br.phdev.metafighter.cmp;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import com.br.phdev.metafighter.GameParameters;
import com.br.phdev.metafighter.cmp.connections.packets.Move;
import com.br.phdev.metafighter.cmp.event.listeners.EventListener;
import com.br.phdev.metafighter.cmp.window.BackGround;
import com.br.phdev.metafighter.cmp.window.Button;
import com.br.phdev.metafighter.cmp.window.LoadingScreen;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public abstract class Entity implements Component {

    protected List<EventListener> listeners;
    protected RectF area;
    protected boolean active;
    protected boolean clicked = false;

    public Entity(RectF area){
        this.area = area;
        this.active = true;
        logMessages(this, null);

    }

    public List<EventListener> getListener() {
        return listeners;
    }

    protected void addEventListener(EventListener listener) {
        if (listeners == null)
            listeners = new ArrayList<>();
        this.listeners.add(listener);
    }

    protected void removeEventListener(EventListener listener){
        if (listeners == null)
            return;
        this.listeners.remove(listener);
    }

    public RectF getArea() {
        return area;
    }

    public void setArea(RectF area) {
        this.area = area;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected void setX(float x){
        float width = this.area.width();
        this.area.left = x;
        this.area.right = x + width;
    }

    protected float getX(){
        return this.area.left;
    }

    protected void setY(float y){
        float height = this.area.height();
        this.area.top = y;
        this.area.bottom = y + height;
    }

    protected float getY(){
        return this.area.top;
    }

    public void move(float x, float y){
        this.setX( this.getX() + x);
        this.setY( this.getY() + y);
    }

    @Override
    public void draw(Canvas canvas){

    }

    @Override
    public void update(){

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        return true;
    }


    protected static void logMessages(Object obj, String msg){
        if(!GameParameters.getInstance().debug)
            return;
        if (GameParameters.getInstance().debugCreateObjects) {
            //if (obj instanceof BackGround)
                GameParameters.getInstance().log("      Objeto criado: " + obj.getClass().getSimpleName() + (msg != null ? msg : ""));
            LoadingScreen.info = "Carregando " + obj.getClass().getSimpleName();

        }
    }


    protected static void log(String msg){
        //if(!GameParameters.getInstance().debug)
          //  return;
        GameParameters.getInstance().log(msg);
    }


    public static boolean checkCollision(RectF A, RectF B){
        if(((A.left >= B.left && A.left <= B.right) && (A.right >= B.left && A.right <= B.right)) &&
                ((A.top >= B.top && A.top <= B.bottom) && (A.bottom >= B.top && A.bottom <= B.bottom)))
            return true;
        return false;
    }
}
