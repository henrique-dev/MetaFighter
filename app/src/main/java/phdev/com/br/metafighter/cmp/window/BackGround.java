package phdev.com.br.metafighter.cmp.window;

import android.graphics.Paint;
import android.graphics.RectF;

import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.cmp.WindowEntity;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class BackGround extends WindowEntity {

    public BackGround(RectF rect, int color){
        super(rect, new Paint(), null);
        paint.setColor(color);
    }

    public BackGround(RectF rect, Paint paint){
        super(rect, paint, null);
    }

    public BackGround(RectF rect, Texture texture){
        super(rect, new Paint(), texture);
        super.texture.scaleImage((int)rect.width(), (int)rect.height());
    }

    protected BackGround(BackGround backGround){
        super(backGround.getArea(), backGround.getPaint(), backGround.getTexture());
    }

}
