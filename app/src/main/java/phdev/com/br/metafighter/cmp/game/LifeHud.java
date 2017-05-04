package phdev.com.br.metafighter.cmp.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import phdev.com.br.metafighter.cmp.GameEntity;
import phdev.com.br.metafighter.cmp.graphics.Sprite;
import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.cmp.window.Text;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class LifeHud extends GameEntity{

    private RectF bar;
    private Texture texture;
    private int HP;
    private Text text;

    public LifeHud(RectF area, Texture hud, String text) {
        super(area, new Paint(), null);
        this.texture = hud;
        paint.setColor(Color.RED);

        RectF textArea = new RectF(area.left, area.bottom, area.right, area.bottom + area.height());
        this.text = new Text(text);
        this.text.setDrawableArea(textArea);
        this.text.setTextSize(area.height());

        this.texture.scaleImage((int)area.width(), (int)area.height());
        this.bar = new RectF(this.area.left, this.area.top, this.area.right, this.area.bottom);

    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
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
