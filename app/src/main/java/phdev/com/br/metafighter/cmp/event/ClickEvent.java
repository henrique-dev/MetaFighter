package phdev.com.br.metafighter.cmp.event;

import android.view.MotionEvent;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class ClickEvent extends Event {

    public static final int CLICKED = MotionEvent.ACTION_DOWN;
    public static final int RELEASED = MotionEvent.ACTION_UP;
    public static final int MOVE = MotionEvent.ACTION_MOVE;

    public int action;
    public float x;
    public float y;
    public boolean execute;

    public ClickEvent(int action, float x, float y, boolean execute){
        this.action = action;
        this.x = x;
        this.y = y;
        this.execute = execute;
    }


}