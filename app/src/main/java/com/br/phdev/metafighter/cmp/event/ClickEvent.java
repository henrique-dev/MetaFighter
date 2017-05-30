package com.br.phdev.metafighter.cmp.event;

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
    public int id;
    public Object parameter;

    public ClickEvent(int action, float x, float y, boolean execute, int id, Object parameter){
        this.action = action;
        this.x = x;
        this.y = y;
        this.execute = execute;
        this.id = id;
        this.parameter = parameter;
    }


}
