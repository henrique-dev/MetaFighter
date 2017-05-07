package phdev.com.br.metafighter.cmp.window;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.cmp.Component;
import phdev.com.br.metafighter.cmp.Entity;
import phdev.com.br.metafighter.cmp.event.listeners.PacketListener;
import phdev.com.br.metafighter.connections.packets.Move;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Scene implements Component {

    private PacketListener listener;

    private List<Component> components;

    private List<Move> packetsToRead = new ArrayList<>();
    private int currentIndexRead = -1;

    private List<Move> packetsToWrite = new ArrayList<>();
    private int currentIndexWrite = -1;

    public Scene(){
        components = new ArrayList<>();

        listener = new PacketListener() {
            @Override
            public void send(Move object) {
                if (object != null){
                    packetsToRead.add(object);
                }
            }

            @Override
            public Move receive() {
                return getCurrentPacketToWrite();
            }
        };
    }

    public PacketListener getListener(){
        return this.listener;
    }

    public void add(Component cmp){
        this.components.add(cmp);
    }

    private Move getCurrentPacketToRead(){
        if (packetsToRead.size() > 0){
            if (packetsToRead.size() > currentIndexRead){
                return packetsToRead.get(++currentIndexRead);
            }
        }
        return null;
    }

    private Move getCurrentPacketToWrite(){
        if (packetsToWrite.size() > 0){
            if (packetsToWrite.size() > currentIndexWrite){
                return packetsToWrite.get(currentIndexWrite);
            }
        }
        return null;
    }

    private void processPacket(Move move){
        if (move != null)
            for (Component cmp : components){
                if (cmp instanceof Entity){
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
                if (cmp instanceof Entity){
                    if (cmp instanceof LabelMove){
                        float x = ((Entity)cmp).getArea().left;
                        float y = ((Entity)cmp).getArea().top;
                        int id = ((Entity)cmp).getId();
                        packetsToWrite.add(new Move(id, x, y));
                    }
                }
            }
            processPacket(getCurrentPacketToRead());
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
