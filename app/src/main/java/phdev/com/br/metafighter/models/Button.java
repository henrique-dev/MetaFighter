package phdev.com.br.metafighter.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Button extends Entity{

    private Texture texture;
    private Paint paint;

    public Button(RectF area, Texture texture) {
        super(area);
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        return false;
    }
}
