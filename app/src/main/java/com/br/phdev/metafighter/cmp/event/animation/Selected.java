package com.br.phdev.metafighter.cmp.event.animation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.br.phdev.metafighter.cmp.Component;
import com.br.phdev.metafighter.cmp.Entity;
import com.br.phdev.metafighter.cmp.event.listeners.AnimationListener;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Selected implements AnimationListener, Component {

    private Fade fade;

    private int edgeSize;
    private boolean active;
    private Entity entity;
    private RectF area;
    private Paint paint;

    public Selected(Entity entity){
        this.entity = entity;
        area = entity.getArea();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        edgeSize = 3;
        fade = new Fade(15, 255);
        this.active = true;
        fade.setActive(true);
    }

    public Selected(int color){
        paint = new Paint();
        paint.setColor(color);
        edgeSize = 3;
        fade = new Fade(15, 255);
    }

    public boolean isActive() {
        return active;
    }

    public void setEstatic(){
        fade.setActive(false);
        fade.setAlpha(255);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        area = entity.getArea();
        fade = new Fade(15, 255);
        this.active = true;
        fade.setActive(true);
    }

    public RectF getArea() {
        return area;
    }

    public void setArea(RectF area) {
        this.area = area;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(area.left, area.top, area.right, area.top + edgeSize, paint);
        canvas.drawRect(area.right - edgeSize, area.top, area.right, area.bottom, paint);
        canvas.drawRect(area.left, area.bottom - edgeSize, area.right, area.bottom, paint);
        canvas.drawRect(area.left, area.top, area.left + edgeSize, area.bottom, paint);
    }

    @Override
    public void update() {

        if (fade != null) {
            fade.update();
            paint.setAlpha(fade.getAlpha());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        return false;
    }
}
