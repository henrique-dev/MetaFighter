package phdev.com.br.metafighter.cmp.event.animation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import phdev.com.br.metafighter.cmp.Component;
import phdev.com.br.metafighter.cmp.Entity;
import phdev.com.br.metafighter.cmp.event.listeners.AnimationListener;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Selected implements AnimationListener, Component {

    private boolean active;
    private Entity entity;
    private RectF area;
    private Paint paint;

    public Selected(Entity entity){
        this.entity = entity;
        area = entity.getArea();
        this.paint = new Paint();
        paint.setColor(Color.BLUE);
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(area, paint);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        return false;
    }
}
