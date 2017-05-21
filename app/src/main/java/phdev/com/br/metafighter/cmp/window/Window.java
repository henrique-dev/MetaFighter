package phdev.com.br.metafighter.cmp.window;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.cmp.Component;
import phdev.com.br.metafighter.cmp.WindowEntity;
import phdev.com.br.metafighter.cmp.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Window extends WindowEntity{

    private List<Component> components;

    private Text textHead;

    private Texture textureHead;
    private Texture textureBody;

    private RectF areaHead;

    public Window(RectF area, Texture textureHead, Texture textureBody, String textHead) {
        super(area, new Paint(), null);
        components = new ArrayList<>();
        this.textureHead = textureHead;
        this.textureBody = textureBody;

        this.textHead = new Text(textHead);
    }

    public void add(Component cmp){
        components.add(cmp);
    }

    public void remove(Component cmp){
        components.remove(cmp);
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(textureBody.getImage(), area.left, area.top, paint);
        canvas.drawBitmap(textureHead.getImage(), areaHead.left, areaHead.top, paint);

        for (Component cmp : components)
            cmp.draw(canvas);
    }

    @Override
    public void update(){
        super.update();
        for (Component cmp : components)
            cmp.update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        super.onTouchEvent(event);
        for (Component cmp : components)
            if (!cmp.onTouchEvent(event))
                return false;
        return true;
    }




}
