package phdev.com.br.metafighter.cmp.window;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.LinkedList;
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

    private LinkedList<Packet> packets;

    private boolean active;

    public Scene(){
        components = new ArrayList<>();
        packets = new LinkedList<>();
        init();
    }

    public abstract void init();

    public Scene start(){
        active = true;
        return this;
    }

    public void add(Component cmp){
        this.components.add(cmp);
    }

    public void remove(Component cmp){
        this.components.remove(cmp);
    }

    public Packet getCurrentPacketToRead(){
        if (packets == null)
            return null;
        if (packets.size() > 0){
            return packets.pop();
        }
        return null;
    }

    public void processPacket(Packet packet){

        if (packet != null)
            packets.add(packet);

        /*
        if (packet != null)
            for (Component cmp : components){
                if (cmp instanceof Entity){
                    Move move = (Move)packet;
                    if (((Entity)cmp).getId() == move.getValue1()){
                        ((Entity)cmp).processPacket(move);
                    }
                }
            }
            */

    }

    @Override
    public void draw(Canvas canvas) {
        if (active)
            for (Component cmp : components)
                if (cmp != null)
                    cmp.draw(canvas);
    }

    @Override
    public void update() {
        if (active)
            for (Component cmp : components)
                if (cmp != null) {
                    cmp.update();
                }
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        if (active)
            for (Component cmp : components)
                if (cmp != null) {
                    cmp.onTouchEvent(evt);
                }
        return true;
    }
}
