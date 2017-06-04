package com.br.phdev.metafighter.cmp.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.br.phdev.metafighter.cmp.GameEntity;
import com.br.phdev.metafighter.cmp.event.ClickEvent;
import com.br.phdev.metafighter.cmp.event.Event;
import com.br.phdev.metafighter.cmp.event.animation.GoAndBack;
import com.br.phdev.metafighter.cmp.event.animation.Selected;
import com.br.phdev.metafighter.cmp.event.listeners.ActionListener;
import com.br.phdev.metafighter.cmp.event.listeners.ClickListener;
import com.br.phdev.metafighter.cmp.event.listeners.EventListener;
import com.br.phdev.metafighter.cmp.graphics.Sprite;
import com.br.phdev.metafighter.cmp.graphics.Texture;
import com.br.phdev.metafighter.cmp.window.Text;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class GameLabel extends GameEntity {

    private Selected selectedAnimationListener;
    private GoAndBack goAndBackAnimationListener;

    private Texture texture;
    private Text text;

    public GameLabel(RectF area, Texture texture, Sprite sprites[]) {
        super(area, new Paint(), sprites);
        if (texture != null) {
            this.texture = texture;
            this.texture.scaleImage((int) area.width(), (int) area.height());
        }
    }

    public void addText(String text, RectF textArea, float textSize, int color){
        this.text = new Text(text);
        this.text.setColor(color);
        this.text.setDrawableArea(textArea);
        this.text.setTextSize(textSize);
    }

    public Text getText() {
        return text;
    }

    public Sprite[] getSprites() {
        return sprites;
    }

    public void setSprites(Sprite[] sprites) {
        if (sprites!= null){
            for (int i = 0; i < sprites.length; i++){
                sprites[i].getTexture().scaleImage((int)area.width(), (int)area.height());
            }
            this.sprites = sprites;
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
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
    public void setX(float x){
        super.setX(x);
    }

    @Override
    public void setY(float y){
        super.setY(y);
    }

    @Override
    public void move(float x, float y){
        super.move(x, y);
    }

    public void setSelected(boolean selected){
        if (selectedAnimationListener != null)
            this.selectedAnimationListener.setActive(selected);
    }

    protected boolean processListeners(Event event){

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

                        if (goAndBackAnimationListener != null)
                            // Executa a animação de ir e voltar
                            goAndBackAnimationListener.go();
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

                            if (goAndBackAnimationListener != null)
                                goAndBackAnimationListener.back();

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
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas){
        if (super.sprites != null){
            for (int i = 0; i < super.sprites.length; i++){
                canvas.drawBitmap(super.sprites[i].getTexture().getImage(), getX(), getY(), super.paint);
            }
        }
        if (texture != null)
            canvas.drawBitmap(texture.getImage(), getX(), getY(), paint);

        if (this.text != null){
            this.text.draw(canvas);
        }
        if (selectedAnimationListener != null)
            if (selectedAnimationListener.isActive())
                selectedAnimationListener.draw(canvas);
    }

    @Override
    public void update(){
        super.update();
        if (selectedAnimationListener != null)
            if (selectedAnimationListener.isActive())
                selectedAnimationListener.update();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getActionMasked();
        float x = event.getX();
        float y = event.getY();

        if (checkCollision(new RectF(x,y,x,y), this.area)){
            if (listeners != null) {
                return this.processListeners(new ClickEvent(action, x, y, true, -1, this));
            }
        }
        else {
            if (clicked)
                this.processListeners(new ClickEvent(MotionEvent.ACTION_UP, x, y, false, -1, this));
        }

        return true;
    }
}
