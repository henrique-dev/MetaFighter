package phdev.com.br.metafighter.models;

import android.graphics.RectF;

import phdev.com.br.metafighter.listeners.EventListener;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public abstract class Entity implements Component {

    private EventListener listener;
    private RectF area;
    private boolean active;

    public Entity(RectF area){
        this.area = area;
        this.active = true;
    }

    public EventListener getListener() {
        return listener;
    }

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public RectF getArea() {
        return area;
    }

    public void setArea(RectF area) {
        this.area = area;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
