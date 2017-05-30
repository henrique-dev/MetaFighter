package com.br.phdev.metafighter.cmp;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface Component {

    void draw(Canvas canvas);
    void update();
    boolean onTouchEvent(MotionEvent evt);

}
