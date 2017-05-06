package phdev.com.br.metafighter.cmp.event.animation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import phdev.com.br.metafighter.cmp.Entity;
import phdev.com.br.metafighter.cmp.event.listeners.AnimationListener;
import phdev.com.br.metafighter.cmp.event.listeners.AutoDestroyableListener;
import phdev.com.br.metafighter.cmp.misc.Timer;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Flash implements AnimationListener {

    private Timer timer;
    private AutoDestroyableListener listener;
    private Fade fade;
    private Entity entity;
    private Paint paint;
    private int secondsToDestroy = -1;

    public Flash(Fade fade, Entity entity, int secondsToDestroy, AutoDestroyableListener listener){
        this.fade = fade;
        this.entity = entity;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        fade.setActive(true);

        this.secondsToDestroy = secondsToDestroy;

        if (secondsToDestroy > 0){
            this.listener = listener;
            timer = new Timer();
            timer.start();
        }
    }

    public Flash(Fade fade, Entity entity){
        this.fade = fade;
        this.entity = entity;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        fade.setActive(true);
    }

    public void active(){
        fade.setActive(true);
    }

    public void update(){
        fade.update();
        paint.setAlpha(fade.getAlpha());
        if (listener != null){
            if (timer.getTicks()/1000000000 > secondsToDestroy){
                listener.autoDestroy(this);
            }
        }
    }

    public void draw(Canvas canvas){
        canvas.drawRect(entity.getArea(), paint);
    }


}
