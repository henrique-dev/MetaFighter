package phdev.com.br.metafighter.models;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Label extends WindowEntity{

    private Text text;

    public Label(RectF area, String text, Texture texture){
        super(area, null, texture);
        super.texture.scaleImage((int)super.getArea().width(), (int)super.getArea().height());
        this.text = new Text(text);
        this.text.setDrawableArea(super.getArea());
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    @Override
    public void setX(float x){
        super.setX(x);
        this.text.setDrawableArea(super.getArea());
    }

    @Override
    public void setY(float y){
        super.setY(y);
        this.text.setDrawableArea(super.getArea());
    }

    @Override
    public void move(float x, float y){
        super.move(x, y);
        if(text != null){
            this.text.move(x, y);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(this.text != null)
            this.text.draw(canvas);
    }

}
