package phdev.com.br.metafighter.cmp.window;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.Component;
import phdev.com.br.metafighter.cmp.Entity;
import phdev.com.br.metafighter.cmp.WindowEntity;
import phdev.com.br.metafighter.cmp.graphics.Sprite;
import phdev.com.br.metafighter.cmp.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class ProgressHud extends WindowEntity {

    private RectF bar;
    private int progress = 0;
    private float percent;

    public ProgressHud(Texture hud, RectF area){
        super(area, new Paint(), hud);

        super.texture.scaleImage((int)this.area.width(), (int)this.area.height());

        this.percent = this.area.width() / 100;

        super.paint.setColor(Color.BLUE);
        this.bar = new RectF(this.area.left, this.area.top, this.area.left, this.area.bottom);

    }

    public void increase(int value){
        this.bar.right = this.bar.left +  value * percent;
    }

    public void reset(){
        this.bar.right = this.bar.left;
        this.visible = false;
    }

    @Override
    public void draw(Canvas canvas) {
        if (texture != null){
            canvas.drawRect(this.bar, this.paint);
            canvas.drawBitmap(this.texture.getImage(), this.area.left, this.area.top, this.paint);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        return true;
    }
}
