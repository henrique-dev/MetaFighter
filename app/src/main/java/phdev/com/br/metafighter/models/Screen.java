package phdev.com.br.metafighter.models;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Screen implements Component{

    private List<Component> entities;

    public Screen(){
        this.entities = new ArrayList<>();
    }

    protected void add(Component cmp){
        this.entities.add(cmp);
    }

    protected void remover(Component cmp){
        this.entities.remove(cmp);
    }

    @Override
    public void draw(Canvas canvas) {
        for (Component cmp : entities)
            cmp.draw(canvas);
    }

    @Override
    public void update() {
        for (Component cmp : entities)
            cmp.update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        for (Component cmp : entities)
            cmp.onTouchEvent(evt);
        return true;
    }
}
