package phdev.com.br.metafighter.cmp.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.GameEntity;
import phdev.com.br.metafighter.cmp.event.Event;
import phdev.com.br.metafighter.cmp.event.listeners.ControllerListener;
import phdev.com.br.metafighter.cmp.graphics.Sprite;
import phdev.com.br.metafighter.cmp.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Player implements ControllerListener {

    public static final int ANDAR_FRENTE = 0;
    public static final int ANDAR_TRAS = 1;
    public static final int PULAR = 3;
    public static final int ACTION1 = 4;

    private String name;
    private Sprite[] view;

    public Player() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sprite[] getView() {
        return view;
    }

    public void setView(Sprite[] view) {
        this.view = view;
    }

    @Override
    public void upPerformed(Event evt) {
        log("Pra cima");
    }

    @Override
    public void downPerformed(Event evt) {
        log("Pra baixo");
    }

    @Override
    public void leftPerformed(Event evt) {
        log("Pra esquerda");
    }

    @Override
    public void rightPerformed(Event evt) {
        log("Pra direita");
    }

    @Override
    public void action1Performed(Event evt) {
        log("");
    }

    @Override
    public void action2Performed(Event evt) {
        log("");
    }

    @Override
    public void action3Performed(Event evt) {
        log("");
    }

    @Override
    public void action4Performed(Event evt) {
        log("");
    }

    @Deprecated
    protected static void log(String msg){
        if(!GameParameters.getInstance().debug)
            return;
        GameParameters.getInstance().log(msg);
    }
}
