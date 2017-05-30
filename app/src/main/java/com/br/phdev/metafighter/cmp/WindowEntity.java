package com.br.phdev.metafighter.cmp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import com.br.phdev.metafighter.cmp.event.ClickEvent;
import com.br.phdev.metafighter.cmp.event.Event;
import com.br.phdev.metafighter.cmp.event.animation.GoAndBack;
import com.br.phdev.metafighter.cmp.event.animation.Selected;
import com.br.phdev.metafighter.cmp.event.listeners.ActionListener;
import com.br.phdev.metafighter.cmp.event.listeners.AnimationListener;
import com.br.phdev.metafighter.cmp.event.listeners.ClickListener;
import com.br.phdev.metafighter.cmp.event.listeners.EventListener;
import com.br.phdev.metafighter.cmp.event.listeners.PressingListener;
import com.br.phdev.metafighter.cmp.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public abstract class WindowEntity extends Entity{

    protected Selected selectedAnimationListener;
    protected GoAndBack goAndBackAnimationListener;

    protected boolean visible;
    protected Paint paint;
    protected Texture texture;
    protected RectF drawableArea;

    public WindowEntity(RectF area, Paint paint, Texture texture) {
        super(area);
        this.visible = true;
        this.paint = paint;
        this.texture = texture;
        this.drawableArea = new RectF(area);
    }

    public void addAnimationListener(AnimationListener listener){
        if (listener instanceof Selected) {
            selectedAnimationListener = (Selected)listener;
            return;
        }
        if (listener instanceof GoAndBack) {
            goAndBackAnimationListener = (GoAndBack)listener;
            return;
        }
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public RectF getDrawableArea() {
        return drawableArea;
    }

    public void setDrawableArea(RectF drawableArea) {
        this.drawableArea = drawableArea;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    protected boolean processListeners(Event event){

        for (EventListener listener : listeners){
            if (listener instanceof ClickListener || listener instanceof ActionListener){

                ClickListener ls = null;
                ActionListener al = null;
                PressingListener pl = null;

                if (listener instanceof PressingListener)
                    pl = (PressingListener) listener;
                if (listener instanceof ClickListener)
                    ls = (ClickListener)listener;
                else
                    al = (ActionListener)listener;

                ClickEvent clickEvent = (ClickEvent)event;

                switch (clickEvent.action){
                    // Quando a entidade é pressionada
                    case MotionEvent.ACTION_DOWN:
                        if (ls != null)
                            // Executa a ação correspondente
                            ls.pressedPerformed(clickEvent);

                        // Caso haja uma animação, à executa
                        if (goAndBackAnimationListener != null)
                            // Executa a animação de ir e voltar
                            ((GoAndBack) goAndBackAnimationListener).go();
                        // Define que a entidade foi pressionada
                        this.clicked = true;
                        break;
                    // Quando a entidade é solta
                    case MotionEvent.ACTION_UP:
                        // Caso ela tenha sido pressionada
                        if (clicked) {
                            if (ls != null)
                                // Executa a ação correspondente
                                ls.releasedPerformed(clickEvent);

                            // Caso haja uma animação, à executa
                            if (goAndBackAnimationListener != null)
                                // Executa a animação de ir e voltar
                                ((GoAndBack) goAndBackAnimationListener).back();

                            // Caso a entidade tenha sido soltada para executar sua função
                            if (clickEvent.execute) {
                                if (ls != null)
                                    // Executa a ação correspodente
                                    ls.actionPerformed(clickEvent);
                                if (al != null)
                                    al.actionPerformed(clickEvent);
                            }

                            // Define que a entidade foi e não esta mais pressionada.
                            return this.clicked = false;
                        }
                        if (pl != null){
                            ls.releasedPerformed(clickEvent);
                            return false;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (pl != null)
                            ls.pressedPerformed(clickEvent);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (ls != null)
                            // Executa a ação correspondente
                            ls.pressedPerformed(clickEvent);

                        // Caso haja uma animação, à executa
                        if (goAndBackAnimationListener != null)
                            // Executa a animação de ir e voltar
                            ((GoAndBack) goAndBackAnimationListener).go();
                        // Define que a entidade foi pressionada
                        this.clicked = true;
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        // Caso ela tenha sido pressionada
                        if (clicked) {
                            if (ls != null)
                                // Executa a ação correspondente
                                ls.releasedPerformed(clickEvent);

                            // Caso haja uma animação, à executa
                            if (goAndBackAnimationListener != null)
                                // Executa a animação de ir e voltar
                                ((GoAndBack) goAndBackAnimationListener).back();

                            // Caso a entidade tenha sido soltada para executar sua função
                            if (clickEvent.execute) {
                                if (ls != null)
                                    // Executa a ação correspodente
                                    ls.actionPerformed(clickEvent);
                                if (al != null)
                                    al.actionPerformed(clickEvent);
                            }

                            // Define que a entidade foi e não esta mais pressionada.
                            return this.clicked = false;
                        }
                        if (pl != null){
                            ls.releasedPerformed(clickEvent);
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        if (this.texture != null){
            canvas.drawBitmap(this.texture.getImage(), super.getX(), super.getY(), this.paint);
        }
        /*
        else
            canvas.drawRect(super.getArea(), this.paint);
            */
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);
        float x;
        float y;

        if (event.getActionIndex() == 1){
            x = event.getX(1);
            y = event.getY(1);
            if (checkCollision(new RectF(x,y,x,y), this.area)){
                if (listeners != null) {
                    //return this.processListeners(new ClickEvent(action, x, y, true, this.id, null));
                    this.processListeners(new ClickEvent(action, x, y, true, this.id, null));
                }
            }
            else {
                if (clicked)
                    this.processListeners(new ClickEvent(MotionEvent.ACTION_UP, event.getX(1), event.getY(1), false, this.id, null));
            }
        }
        if (event.getActionIndex() == 0){
            x = event.getX(0);
            y = event.getY(0);
            if (checkCollision(new RectF(x,y,x,y), this.area)){
                if (listeners != null) {
                    //return this.processListeners(new ClickEvent(action, x, y, true, this.id, null));
                    this.processListeners(new ClickEvent(action, x, y, true, this.id, null));
                }
            }
            else {
                if (clicked)
                    this.processListeners(new ClickEvent(MotionEvent.ACTION_UP, x, y, false, this.id, null));
            }
        }
        return true;
    }
}
