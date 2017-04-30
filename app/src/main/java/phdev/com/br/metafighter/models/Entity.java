package phdev.com.br.metafighter.models;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.listeners.ActionListener;
import phdev.com.br.metafighter.listeners.Event;
import phdev.com.br.metafighter.listeners.EventListener;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public abstract class Entity implements Component {

    private ActionListener listener;
    private RectF area;
    private boolean active;

    public Entity(RectF area){
        this.area = area;
        this.active = true;
        logMessages(this, null);
    }

    public ActionListener getListener() {
        return listener;
    }

    public void addActionListener(ActionListener listener) {
        this.listener = listener;
    }

    public void processActionEvent(Event evt){
        ActionListener al = listener;
        al.actionPerformed(evt);
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

        int action = event.getActionMasked();
        float x = event.getX();
        float y = event.getY();

        if (checkCollision(new RectF(x,y,x,y), this.area)){
            if (listener != null) {
                this.processActionEvent(new Event());
            }
        }

        return true;
    }

    @Deprecated
    protected static void logMessages(Object obj, String msg){
        if(!GameParameters.getInstance().debug)
            return;
        GameParameters.getInstance().log("      Objeto criado: " + obj.getClass().getSimpleName() + (msg != null ? msg : ""));
    }

    @Deprecated
    protected static boolean checkCollision(RectF A, RectF B){
        if(((A.left >= B.left && A.left <= B.right) && (A.right >= B.left && A.right <= B.right)) &&
                ((A.top >= B.top && A.top <= B.bottom) && (A.bottom >= B.top && A.bottom <= B.bottom)))
            return true;
        return false;
    }
}
