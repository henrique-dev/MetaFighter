package com.br.phdev.metafighter.cmp;

import android.graphics.Paint;
import android.graphics.RectF;

import com.br.phdev.metafighter.cmp.graphics.Sprite;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public abstract class GameEntity extends Entity {

    protected Paint paint;
    protected RectF drawableArea;
    protected Sprite sprites[];

    public GameEntity(RectF area, Paint paint, Sprite sprites[]) {
        super(area);
        this.paint = paint;
        this.sprites = sprites;
        this.drawableArea = new RectF();
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public RectF getDrawableArea() {
        return drawableArea;
    }

    public void setDrawableArea(RectF drawableArea) {
        this.drawableArea = drawableArea;
    }

    public Sprite[] getSprites() {
        return sprites;
    }

    public void setSprites(Sprite[] sprites) {
        this.sprites = sprites;
    }
}
