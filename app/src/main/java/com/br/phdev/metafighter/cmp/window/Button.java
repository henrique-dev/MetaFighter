package com.br.phdev.metafighter.cmp.window;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.br.phdev.metafighter.cmp.WindowEntity;
import com.br.phdev.metafighter.cmp.event.animation.GoAndBack;
import com.br.phdev.metafighter.cmp.event.listeners.AnimationListener;
import com.br.phdev.metafighter.cmp.event.listeners.EventListener;
import com.br.phdev.metafighter.cmp.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Button extends WindowEntity {

    private Text text;

    public Button(RectF area, String text, Texture texture) {
        super(area, new Paint(), texture);
        super.texture.scaleImage((int)super.getArea().width(), (int)super.getArea().height());
        if (text != null){
            this.text = new Text(text);
            this.text.setDrawableArea(super.getArea());
        }
        this.goAndBackAnimationListener = new GoAndBack(this);
        //this.text.adaptText();
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
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
    public void setX(float x){
        super.setX(x);
        if (text != null)
            this.text.setDrawableArea(super.getArea());
    }

    @Override
    public void setY(float y){
        super.setY(y);
        if (text != null)
            this.text.setDrawableArea(super.getArea());
    }

    @Override
    public void move(float x, float y){
        super.move(x, y);
        if(text != null){
            this.text.move(x, y);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(this.text != null)
            this.text.draw(canvas);
    }
}
