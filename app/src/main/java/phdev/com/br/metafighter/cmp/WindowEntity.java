package phdev.com.br.metafighter.cmp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import phdev.com.br.metafighter.cmp.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public abstract class WindowEntity extends Entity{

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

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        if (this.texture != null){
            canvas.drawBitmap(this.texture.getImage(), super.getX(), super.getY(), this.paint);
        }
        else
            canvas.drawRect(super.getArea(), this.paint);
    }
}
