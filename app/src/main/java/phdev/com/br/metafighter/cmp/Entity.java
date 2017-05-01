package phdev.com.br.metafighter.cmp;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.event.ActionListener;
import phdev.com.br.metafighter.cmp.event.AnimationListener;
import phdev.com.br.metafighter.cmp.event.ClickEvent;
import phdev.com.br.metafighter.cmp.event.ClickListener;
import phdev.com.br.metafighter.cmp.event.Event;
import phdev.com.br.metafighter.cmp.event.EventListener;
import phdev.com.br.metafighter.cmp.event.animation.GoAndBack;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public abstract class Entity implements Component {

    protected AnimationListener animationListener;
    protected List<EventListener> listeners;
    protected RectF area;
    private boolean active;
    private boolean clicked = false;
    private float startx = -1;
    private float starty = -1;

    public Entity(RectF area){
        this.area = area;
        this.active = true;
        logMessages(this, null);
    }

    public List<EventListener> getListener() {
        return listeners;
    }

    protected void addAnimationListener(AnimationListener listener){
        this.animationListener = listener;
    }

    protected void removeAnimationListener(){
        this.animationListener = null;
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

    protected boolean processListeners(Event event){

        for (EventListener listener : listeners){
            if (listener instanceof ActionListener) {
                ((ActionListener) listener).actionPerformed(event);
            }
            if (listener instanceof ClickListener){

                ClickListener ls = (ClickListener)listener;
                ClickEvent clickEvent = (ClickEvent)event;

                switch (clickEvent.action){
                    // Quando a entidade é pressionada
                    case ClickEvent.CLICKED:
                        // Executa a ação correspondente
                        ls.pressedPerformed(clickEvent);

                        // Caso haja uma animação, à executa
                        if (animationListener != null)
                            // Executa a animação de ir e voltar
                            ((GoAndBack)animationListener).go();
                        // Define que a entidade foi pressionada
                        this.clicked = true;
                        break;
                    // Quando a entidade é solta
                    case ClickEvent.RELEASED:
                        // Caso ela tenha sido pressionada
                        if (clicked) {
                            // Executa a ação correspondente
                            ls.releasedPerformed(clickEvent);

                            // Caso haja uma animação, à executa
                            if (animationListener != null)
                                // Executa a animação de ir e voltar
                                ((GoAndBack)animationListener).back();

                            // Caso a entidade tenha sido soltada para executar sua função
                            if (clickEvent.execute)
                                // Executa a ação correspodente
                                ls.executePerformed(clickEvent);

                            // Define que a entidade foi e não esta mais pressionada.
                            return this.clicked = false;
                        }
                        break;
                }
            }
        }
        return true;
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
            if (listeners != null) {
                return this.processListeners(new ClickEvent(action, x, y, true));
            }
        }
        else {
            if (clicked)
                this.processListeners(new ClickEvent(MotionEvent.ACTION_UP, x, y, false));
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
