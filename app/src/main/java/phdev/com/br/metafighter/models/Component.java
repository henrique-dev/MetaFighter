package phdev.com.br.metafighter.models;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface Component {

    public void draw(Canvas canvas);
    public void update();
    public boolean onTouchEvent(MotionEvent evt);

}
