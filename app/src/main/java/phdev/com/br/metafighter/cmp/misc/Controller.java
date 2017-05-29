package phdev.com.br.metafighter.cmp.misc;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.cmp.Component;
import phdev.com.br.metafighter.cmp.event.ClickEvent;
import phdev.com.br.metafighter.cmp.event.Event;
import phdev.com.br.metafighter.cmp.event.animation.GoAndBack;
import phdev.com.br.metafighter.cmp.event.listeners.ActionListener;
import phdev.com.br.metafighter.cmp.event.listeners.ClickListener;
import phdev.com.br.metafighter.cmp.event.listeners.ControllerListener;
import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.cmp.window.Button;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Controller implements Component {

    public static final int ARROW_UP_PRESSED = 4;
    public static final int ARROW_DOWN_PRESSED = 5;
    public static final int ARROW_LEFT_PRESSED = 6;
    public static final int ARROW_RIGHT_PRESSED = 7;
    public static final int ARROW_UP_RELEASED = 8;
    public static final int ARROW_DOWN_RELEASED = 9;
    public static final int ARROW_LEFT_RELEASED = 10;
    public static final int ARROW_RIGHT_RELEASED = 11;
    public static final int ARROW_UP_ACTION = 12;
    public static final int ARROW_DOWN_ACTION = 13;
    public static final int ARROW_LEFT_ACTION = 14;
    public static final int ARROW_RIGHT_ACTION = 15;

    public static final int ACTION_1 = 16;
    public static final int ACTION_2 = 17;
    public static final int ACTION_3 = 18;
    public static final int ACTION_3_PRESSED = 19;
    public static final int ACTION_3_RELEASED = 20;
    public static final int ACTION_4 = 21;

    private Texture directionalTexture;
    private Texture buttonTexture;

    private List<Component> buttons;

    private Button direcUpButton;
    private Button direcDownButton;
    private Button direcLeftButton;
    private Button direcRightButton;

    private Button action1Button;
    private Button action2Button;
    private Button action3Button;
    private Button action4Button;

    private RectF directionalArea;
    private RectF buttonsArea;

    private ControllerListener listener;

    public Controller(RectF directionalArea, Texture directionalTexture, RectF buttonsArea, Texture buttonTexture, ControllerListener listener){
        this.listener = listener;
        buttons = new ArrayList<>();

        this.directionalTexture = directionalTexture;
        this.buttonTexture = buttonTexture;

        float divx = directionalArea.width() / 3;
        float divy = directionalArea.height() / 3;

        direcUpButton = new Button(new RectF(
                directionalArea.left + divx,
                directionalArea.top,
                directionalArea.left + divx*2,
                directionalArea.top + divy),
                null, this.directionalTexture);
        direcUpButton.getPaint().setAlpha(170);
        direcUpButton.addEventListener(new ClickListener() {

            @Override
            public void pressedPerformed(ClickEvent event) {
                fireAction(ARROW_UP_PRESSED);
            }

            @Override
            public void releasedPerformed(ClickEvent event) {
                fireAction(ARROW_UP_RELEASED);
            }

            @Override
            public void actionPerformed(Event event) {
                fireAction(ARROW_UP_ACTION);
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
        direcDownButton.addEventListener(new ClickListener() {

            @Override
            public void pressedPerformed(ClickEvent event) {
                fireAction(ARROW_DOWN_PRESSED);
            }

            @Override
            public void releasedPerformed(ClickEvent event) {
                fireAction(ARROW_DOWN_RELEASED);
            }

            @Override
            public void actionPerformed(Event event) {
                fireAction(ARROW_DOWN_ACTION);
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
        direcLeftButton.addEventListener(new ClickListener() {
            @Override
            public void pressedPerformed(ClickEvent event) {
                fireAction(ARROW_LEFT_PRESSED);
            }

            @Override
            public void releasedPerformed(ClickEvent event) {
                fireAction(ARROW_LEFT_RELEASED);
            }

            @Override
            public void actionPerformed(Event event) {
                fireAction(ARROW_LEFT_ACTION);
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
        direcRightButton.addEventListener(new ClickListener() {
            @Override
            public void pressedPerformed(ClickEvent event) {
                fireAction(ARROW_RIGHT_PRESSED);
            }

            @Override
            public void releasedPerformed(ClickEvent event) {
                fireAction(ARROW_RIGHT_RELEASED);
            }

            @Override
            public void actionPerformed(Event event) {
                fireAction(ARROW_RIGHT_ACTION);
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



        action1Button = new Button(new RectF(
                buttonsArea.left + divx,
                buttonsArea.top,
                buttonsArea.left + divx*2,
                buttonsArea.top + divy),
                null, this.buttonTexture);
        action1Button.getPaint().setAlpha(170);
        action1Button.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                fireAction(ACTION_1);
            }
        });
        action1Button.addAnimationListener(new GoAndBack(action1Button));


        action2Button = new Button(new RectF(
                buttonsArea.left + divx,
                buttonsArea.top + divy*2,
                buttonsArea.left + divx*2,
                buttonsArea.bottom ),
                null, this.buttonTexture);
        action2Button.getPaint().setAlpha(170);
        action2Button.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                fireAction(ACTION_2);
            }
        });
        action2Button.addAnimationListener(new GoAndBack(action2Button));


        action3Button = new Button(new RectF(
                buttonsArea.left,
                buttonsArea.top + divy,
                buttonsArea.left + divx,
                buttonsArea.top + divy*2 ),
                null, this.buttonTexture);
        action3Button.getPaint().setAlpha(170);
        action3Button.addEventListener(new ClickListener() {
            @Override
            public void pressedPerformed(ClickEvent event) {
                fireAction(ACTION_3_PRESSED);
            }

            @Override
            public void releasedPerformed(ClickEvent event) {
                fireAction(ACTION_3_RELEASED);
            }

            @Override
            public void actionPerformed(Event event) {
                fireAction(ACTION_3);
            }
        });
        action3Button.addAnimationListener(new GoAndBack(action3Button));


        action4Button = new Button(new RectF(
                buttonsArea.left + divx*2,
                buttonsArea.top + divy,
                buttonsArea.right,
                buttonsArea.top + divy*2 ),
                null, this.buttonTexture);
        action4Button.getPaint().setAlpha(170);
        action4Button.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                fireAction(ACTION_4);
            }
        });
        action4Button.addAnimationListener(new GoAndBack(action4Button));



        buttons.add(direcUpButton);
        buttons.add(direcDownButton);
        buttons.add(direcLeftButton);
        buttons.add(direcRightButton);
        buttons.add(noAction);

        buttons.add(action1Button);
        buttons.add(action2Button);
        buttons.add(action3Button);
        //buttons.add(action4Button);
    }

    public Controller(ControllerListener listener){
        this.listener = listener;
    }

    public void fireAction(int action){
        switch (action){
            case ARROW_UP_ACTION:
                listener.arrowUpPerformed();
                break;
            case ARROW_UP_PRESSED:
                listener.arrowUpPressed();
                break;
            case ARROW_UP_RELEASED:
                listener.arrowUpReleased();
                break;
            case ARROW_DOWN_ACTION:
                listener.arrowDownPerformed();
                break;
            case ARROW_DOWN_PRESSED:
                listener.arrowDownPressed();
                break;
            case ARROW_DOWN_RELEASED:
                listener.arrowDownReleased();
                break;
            case ARROW_LEFT_ACTION:
                listener.arrowLeftPerformed();
                break;
            case ARROW_LEFT_PRESSED:
                listener.arrowLeftPressed();
                break;
            case ARROW_LEFT_RELEASED:
                listener.arrowLeftReleased();
                break;
            case ARROW_RIGHT_ACTION:
                listener.arrowRightPerformed();
                break;
            case ARROW_RIGHT_PRESSED:
                listener.arrowRightPressed();
                break;
            case ARROW_RIGHT_RELEASED:
                listener.arrowRightReleased();
                break;
            case ACTION_1:
                listener.action1Performed();
                break;
            case ACTION_2:
                listener.action2Performed();
                break;
            case ACTION_3:
                listener.action3Performed();
                break;
            case ACTION_3_PRESSED:
                listener.action3Pressed();
                break;
            case ACTION_3_RELEASED:
                listener.action3Released();
                break;
            case ACTION_4:
                listener.action4Performed();
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

        action1Button.onTouchEvent(evt);
        action2Button.onTouchEvent(evt);
        action3Button.onTouchEvent(evt);
        action4Button.onTouchEvent(evt);

        direcUpButton.onTouchEvent(evt);
        direcDownButton.onTouchEvent(evt);
        direcLeftButton.onTouchEvent(evt);
        direcRightButton.onTouchEvent(evt);

        /*
        for (Component cmp: buttons)
            cmp.onTouchEvent(evt);
            */
        return true;
    }
}
