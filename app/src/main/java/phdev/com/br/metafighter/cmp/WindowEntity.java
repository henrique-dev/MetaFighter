package phdev.com.br.metafighter.cmp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import phdev.com.br.metafighter.cmp.window.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public abstract class WindowEntity extends Entity{

    protected Paint paint;
    protected Texture texture;
    protected RectF drawableArea;

    public WindowEntity(RectF area, Paint paint, Texture texture) {
        super(area);
        this.paint = paint;
        this.texture = texture;
        this.drawableArea = new RectF();
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        if (this.texture != null){
            canvas.drawBitmap(this.texture.getImage(), super.getX(), super.getY(), this.paint);
        }
    }
}