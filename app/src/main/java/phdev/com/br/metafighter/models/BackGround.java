package phdev.com.br.metafighter.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class BackGround extends WindowEntity{

    public BackGround(RectF rect, int color){
        super(rect, new Paint(), null);
        this.paint.setColor(color);
    }

    public BackGround(RectF rect, Texture texture){
        super(rect, new Paint(), texture);
        super.texture.scaleImage((int)rect.width(), (int)rect.height());
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(super.texture.getImage(), 0, 0, super.paint);
    }

}
