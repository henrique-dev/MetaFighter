package com.br.phdev.metafighter.cmp.window;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.br.phdev.metafighter.cmp.WindowEntity;
import com.br.phdev.metafighter.cmp.event.ClickEvent;
import com.br.phdev.metafighter.cmp.event.Event;
import com.br.phdev.metafighter.cmp.event.animation.GoAndBack;
import com.br.phdev.metafighter.cmp.event.listeners.ActionListener;
import com.br.phdev.metafighter.cmp.event.listeners.AnimationListener;
import com.br.phdev.metafighter.cmp.event.listeners.ClickListener;
import com.br.phdev.metafighter.cmp.event.listeners.EventListener;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class TableItem extends WindowEntity {

    private Text text;
    private boolean clicked = false;
    private boolean moving = false;
    private int id;

    public TableItem(String text){
        super(new RectF(), new Paint(), null);
        this.text = new Text(text);
        //this.text.setDrawableArea(area);
        //this.text.setAutosize(true);
    }

    @Override
    public void setArea(RectF area){
        super.setArea(area);
        this.text.setDrawableArea(area);
        //this.text.setAutosize(true);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public RectF getArea(){
        return super.getArea();
    }

    @Override
    public void addEventListener(EventListener listener){
        super.addEventListener(listener);
    }

    @Override
    public void removeEventListener(EventListener listener){
        super.removeEventListener(listener);
    }

    @Override
    public void addAnimationListener(AnimationListener listener){
        super.addAnimationListener(listener);
    }

    @Override
    public boolean processListeners(Event event){
        for (EventListener listener : listeners){
            if (listener instanceof ClickListener || listener instanceof ActionListener){

                ClickListener ls = null;
                ActionListener al = null;

                if (listener instanceof ClickListener)
                    ls = (ClickListener)listener;
                else
                    al = (ActionListener)listener;

                ClickEvent clickEvent = (ClickEvent)event;

                switch (clickEvent.action){
                    // Quando a entidade é pressionada
                    case ClickEvent.CLICKED:
                        if (ls != null)
                            // Executa a ação correspondente
                            ls.pressedPerformed(clickEvent);

                        // Define que a entidade foi pressionada
                        this.clicked = true;
                        break;
                    // Quando a entidade é solta
                    case ClickEvent.RELEASED:
                        // Caso ela tenha sido pressionada
                        if (clicked) {
                            if (ls != null)
                                // Executa a ação correspondente
                                ls.releasedPerformed(clickEvent);

                                /*
                                // Caso haja uma animação, à executa
                                if (goAndBackAnimationListener != null)
                                    // Executa a animação de ir e voltar
                                    if (clickEvent.execute && !moving)
                                        ((GoAndBack)goAndBackAnimationListener).back();
                                        */

                            // Caso a entidade tenha sido soltada para executar sua função
                            if (clickEvent.execute && !moving) {
                                // Executa a ação correspodente

                                // Caso haja uma animação, à executa
                                if (goAndBackAnimationListener != null) {
                                    ((GoAndBack) goAndBackAnimationListener).setOriginalArea(new RectF(super.getArea()));
                                    ((GoAndBack) goAndBackAnimationListener).goAndBack();
                                }
                                if (ls != null)
                                    ls.actionPerformed(clickEvent);
                                if (al != null)
                                    al.actionPerformed(clickEvent);
                            }

                            this.moving = false;

                            // Define que a entidade foi e não esta mais pressionada.
                            return this.clicked = false;
                        }
                        break;
                    case ClickEvent.MOVE:
                        if (clicked) {
                            this.moving = true;
                        }
                        break;
                }
            }
        }
        return true;
    }

    public Text getText(){
        return this.text;
    }

    @Override
    public void move(float x, float y){
        super.move(x, y);
        this.text.setDrawableArea(super.getArea());
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        this.text.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = event.getActionMasked();
        float x = event.getX();
        float y = event.getY();

        if (checkCollision(new RectF(x,y,x,y), this.area)){
            if (super.listeners != null) {
                return this.processListeners(new ClickEvent(action, x, y, true, -1, null));
            }
        }
        else {
            if (clicked)
                this.processListeners(new ClickEvent(MotionEvent.ACTION_UP, x, y, false, -1, null));
        }

        return true;
    }

}
