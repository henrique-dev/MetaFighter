package phdev.com.br.metafighter.cmp.window;

import android.graphics.RectF;
import android.view.MotionEvent;

import phdev.com.br.metafighter.cmp.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class LabelMove extends Label {

    private float startX = -1;
    private float startY = -1;

    public LabelMove(RectF area, String text, Texture texture) {
        super(area, text, texture);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = event.getActionMasked();
        float x = event.getX();
        float y = event.getY();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                    this.startX = x;
                    this.startY = y;
                break;
            case MotionEvent.ACTION_UP:
                this.startX = -1;
                this.startY = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                move(x - startX, y - startY);
                this.startX = x;
                this.startY = y;
                break;
        }

        return true;
    }
}
