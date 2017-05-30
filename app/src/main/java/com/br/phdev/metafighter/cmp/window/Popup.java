package com.br.phdev.metafighter.cmp.window;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.br.phdev.metafighter.GameParameters;
import com.br.phdev.metafighter.cmp.WindowEntity;
import com.br.phdev.metafighter.cmp.event.Event;
import com.br.phdev.metafighter.cmp.event.listeners.AutoDestroyableListener;
import com.br.phdev.metafighter.cmp.misc.Timer;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Popup extends WindowEntity {

    private Text text;
    private Timer timer;
    private int timeSeconds;

    public Popup(String text, AutoDestroyableListener listener, int timeSeconds) {
        super(new RectF(), new Paint(), null);
        this.timeSeconds = timeSeconds;
        addEventListener(listener);
        //this.listener = listener;

        super.paint.setAlpha(200);

        this.text = new Text(text);

        RectF screenSize = GameParameters.getInstance().screenSize;

        super.setArea(new RectF(screenSize));
        super.setDrawableArea(area);


        //if (autoSize)
          //  fontSize = Text.adaptText(Text.getArrayFromText(text), screenSize);
        //else
        float fontSize = GameParameters.getInstance().defaultTextSize;

        /*
        super.setArea(new RectF( screenSize.centerX() - popupSize.width()/2,
                screenSize.centerY() - popupSize.height()/2,
                screenSize.centerX() + popupSize.width()/2,
                screenSize.centerY() + popupSize.height()/2));
                */

        this.text.setDrawableArea( super.getArea() );
        this.text.setTextSize(fontSize);
        this.timer = new Timer();
        this.timer.start();
    }

    @Override
    protected boolean processListeners(Event event){
        try{
            ((AutoDestroyableListener)listeners.get(0)).autoDestroy(this);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void update(){
        if (this.timer.getTicks()/1000000000 > timeSeconds){
            processListeners(new Event());
        }
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawRect(GameParameters.getInstance().screenSize, this.paint);
        this.text.draw(canvas);
    }
}
