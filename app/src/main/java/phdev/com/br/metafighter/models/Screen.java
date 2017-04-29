package phdev.com.br.metafighter.models;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.List;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Screen implements Component{

    private List<Component> entities;

    public Screen(){

    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        return false;
    }
}
