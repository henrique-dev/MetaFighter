package phdev.com.br.metafighter.cmp.window;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.cmp.Component;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Scene implements Component {

    private List<Component> components;

    public Scene(){
        components = new ArrayList<>();
    }

    public void add(Component cmp){
        this.components.add(cmp);
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
            if (cmp != null)
                cmp.update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        for (Component cmp : components)
            if (cmp != null)
                cmp.onTouchEvent(evt);
        return true;
    }
}
