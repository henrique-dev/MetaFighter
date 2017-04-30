package phdev.com.br.metafighter.cmp.window;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import phdev.com.br.metafighter.cmp.WindowEntity;
import phdev.com.br.metafighter.cmp.window.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Button extends WindowEntity {

    public Button(RectF area, Texture texture) {
        super(area, new Paint(), texture);
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
