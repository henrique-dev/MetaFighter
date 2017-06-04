package com.br.phdev.metafighter.cmp.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.br.phdev.metafighter.cmp.GameEntity;
import com.br.phdev.metafighter.cmp.graphics.Texture;
import com.br.phdev.metafighter.cmp.window.Text;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class LifeHud extends GameEntity {

    private RectF bar;
    private Texture texture;
    private float HP;
    private Text text;

    private float percent;

    public LifeHud(RectF area, Texture hud, String text) {
        super(area, new Paint(), null);
        this.texture = hud;
        paint.setColor(Color.RED);

        HP = 100;

        RectF textArea = new RectF(area.left, area.bottom, area.right, area.bottom + area.height());
        this.text = new Text(text);
        this.text.setDrawableArea(textArea);
        this.text.setTextSize(area.height());

        this.texture.scaleImage((int)area.width(), (int)area.height());
        this.bar = new RectF(this.area.left, this.area.top, this.area.right, this.area.bottom);

        percent = bar.width() / HP;

    }

    public float getHP() {
        return HP;
    }

    public void setHP(float HP) {
        this.HP = HP;
        bar.right = bar.left + HP * percent;
    }

    public void decrementHP(float damage){
        HP -= damage;
        bar.right = bar.left + HP * percent;

        if (bar.right < bar.left)
            bar.right = bar.left;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    @Override
    public void draw(Canvas canvas) {
        if (texture != null){
            canvas.drawRect(this.bar, this.paint);
            canvas.drawBitmap(this.texture.getImage(), this.area.left, this.area.top, this.paint);
            if (text != null)
                text.draw(canvas);
        }
    }
}
