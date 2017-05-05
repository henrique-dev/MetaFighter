package phdev.com.br.metafighter.cmp.misc;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.cmp.Component;
import phdev.com.br.metafighter.cmp.event.Event;
import phdev.com.br.metafighter.cmp.event.animation.GoAndBack;
import phdev.com.br.metafighter.cmp.event.listeners.ActionListener;
import phdev.com.br.metafighter.cmp.event.listeners.ControllerListener;
import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.cmp.window.Button;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Controller implements Component {

    public static final int DIREC_UP = 0;
    public static final int DIREC_DOWN = 1;
    public static final int DIREC_LEFT = 2;
    public static final int DIREC_RIGHT = 3;
    public static final int ACTION_1 = 4;
    public static final int ACTION_2 = 5;
    public static final int ACTION_3 = 6;
    public static final int ACTION_4 = 7;

    private Texture directionalTexture;
    private Texture buttonTexture;

    private List<Component> buttons;

    private Button direcUpButton;
    private Button direcDownButton;
    private Button direcLeftButton;
    private Button direcRightButton;

    private RectF directionalArea;
    private RectF buttonsArea;

    private ControllerListener listener;

    public Controller(RectF directionalArea, Texture directionalTexture, RectF buttonsArea, Texture buttonTexture, ControllerListener listener){
        this.listener = listener;
        buttons = new ArrayList<>();

        this.directionalTexture = directionalTexture;

        float divx = directionalArea.width() / 3;
        float divy = directionalArea.height() / 3;

        direcUpButton = new Button(new RectF(
                directionalArea.left + divx,
                directionalArea.top,
                directionalArea.left + divx*2,
                directionalArea.top + divy),
                null, this.directionalTexture);
        direcUpButton.getPaint().setAlpha(170);
        direcUpButton.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                fireAction(DIREC_UP);
            }
        });
        direcUpButton.addAnimationListener(new GoAndBack(direcUpButton));


        direcDownButton = new Button(new RectF(
                directionalArea.left + divx,
                directionalArea.top + divy*2,
                directionalArea.left + divx*2,
                directionalArea.bottom ),
                null, this.directionalTexture);
        direcDownButton.getPaint().setAlpha(170);
        direcDownButton.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                fireAction(DIREC_DOWN);
            }
        });
        direcDownButton.addAnimationListener(new GoAndBack(direcDownButton));


        direcLeftButton = new Button(new RectF(
                directionalArea.left,
                directionalArea.top + divy,
                directionalArea.left + divx,
                directionalArea.top + divy*2 ),
                null, this.directionalTexture);
        direcLeftButton.getPaint().setAlpha(170);
        direcLeftButton.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                fireAction(DIREC_LEFT);
            }
        });
        direcLeftButton.addAnimationListener(new GoAndBack(direcLeftButton));


        direcRightButton = new Button(new RectF(
                directionalArea.left + divx*2,
                directionalArea.top + divy,
                directionalArea.right,
                directionalArea.top + divy*2 ),
                null, this.directionalTexture);
        direcRightButton.getPaint().setAlpha(170);
        direcRightButton.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                fireAction(DIREC_RIGHT);
            }
        });
        direcRightButton.addAnimationListener(new GoAndBack(direcRightButton));

        Button noAction = new Button(new RectF(
                directionalArea.left + divx,
                directionalArea.top + divy,
                directionalArea.left + divx*2,
                directionalArea.top + divy*2 ),
                null, this.directionalTexture);
        noAction.getPaint().setAlpha(170);


        buttons.add(direcUpButton);
        buttons.add(direcDownButton);
        buttons.add(direcLeftButton);
        buttons.add(direcRightButton);
        buttons.add(noAction);
    }

    public void fireAction(int action){
        switch (action){
            case DIREC_UP:
                listener.upPerformed(new Event());
                break;
            case DIREC_DOWN:
                listener.downPerformed(new Event());
                break;
            case DIREC_LEFT:
                listener.leftPerformed(new Event());
                break;
            case DIREC_RIGHT:
                listener.rightPerformed(new Event());
                break;
            case ACTION_1:
                listener.action1Performed(new Event());
                break;
            case ACTION_2:
                listener.action2Performed(new Event());
                break;
            case ACTION_3:
                listener.action3Performed(new Event());
                break;
            case ACTION_4:
                listener.action4Performed(new Event());
                break;
        }
    }


    @Override
    public void draw(Canvas canvas) {
        for (Component cmp : buttons)
            cmp.draw(canvas);
    }

    @Override
    public void update() {
        for (Component cmp: buttons)
            cmp.update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        for (Component cmp: buttons)
            cmp.onTouchEvent(evt);
        return true;
    }
}
