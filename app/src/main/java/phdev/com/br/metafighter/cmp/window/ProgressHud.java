package phdev.com.br.metafighter.cmp.window;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import phdev.com.br.metafighter.cmp.Entity;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class ProgressHud extends Entity {

    private int progress = 0;

    public ProgressHud(RectF area) {
        super(area);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public void draw(Canvas canvas) {
        System.out.println();
    }

    @Override
    public void update() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        return false;
    }
}
