package phdev.com.br.metafighter.cmp.window;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.cmp.Component;
import phdev.com.br.metafighter.cmp.Entity;
import phdev.com.br.metafighter.cmp.connections.packets.Move;
import phdev.com.br.metafighter.cmp.connections.packets.Packet;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public abstract class Scene implements Component {

    private List<Component> components;

    public Scene(){
        components = new ArrayList<>();
        init();
    }

    public abstract void init();

    public Scene start(){

        return this;
    }

    public void add(Component cmp){
        this.components.add(cmp);
    }

    public void processPacket(Packet packet){
        if (packet != null)
            for (Component cmp : components){
                if (cmp instanceof Entity){
                    Move move = (Move)packet;
                    if (((Entity)cmp).getId() == move.getSendtoid()){
                        ((Entity)cmp).processPacket(move);
                    }
                }
            }
    }

    @Override
    public void draw(Canvas canvas) {
        for (Component cmp : components)
            if (cmp != null)
                cmp.draw(canvas);
    }

    @Override
    public void update() {
        for (Component cmp : components)
            if (cmp != null) {
                cmp.update();
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        for (Component cmp : components)
            if (cmp != null) {
                cmp.onTouchEvent(evt);
            }
        return true;
    }
}
