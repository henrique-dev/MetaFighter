package phdev.com.br.metafighter.cmp.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import phdev.com.br.metafighter.cmp.GameEntity;
import phdev.com.br.metafighter.cmp.event.ClickEvent;
import phdev.com.br.metafighter.cmp.event.listeners.AnimationListener;
import phdev.com.br.metafighter.cmp.event.listeners.EventListener;
import phdev.com.br.metafighter.cmp.window.Label;
import phdev.com.br.metafighter.cmp.window.Text;
import phdev.com.br.metafighter.cmp.graphics.Sprite;
import phdev.com.br.metafighter.cmp.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class GameLabel extends GameEntity {

    private String name;
    private Texture texture;
    private Text text;

    public GameLabel(RectF area, Texture texture, Sprite sprites[], String name) {
        super(area, new Paint(), sprites);
        this.name = name;
        this.texture = texture;
        this.texture.scaleImage((int)area.width(), (int)area.height());
    }

    public void addText(String text, RectF textArea, float textSize, int color){
        this.text = new Text(text);
        this.text.setColor(color);
        this.text.setDrawableArea(textArea);
        this.text.setTextSize(textSize);
    }

    public Text getText() {
        return text;
    }

    public Sprite[] getSprites() {
        return sprites;
    }

    public void setSprites(Sprite[] sprites) {
        for (int i = 0; i < sprites.length; i++){
            sprites[i].getTexture().scaleImage((int)area.width(), (int)area.height());
        }
        this.sprites = sprites;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void addEventListener(EventListener listener){
        super.addEventListener(listener);
    }

    @Override
    public void removeEventListener(EventListener listener){
        super.removeEventListener(listener);
    }

    @Override
    public void addAnimationListener(AnimationListener listener){
        super.addAnimationListener(listener);
    }

    @Override
    public void addAnimationListener(){
        super.addAnimationListener();
    }

    @Override
    public void removeAnimationListener(){
        super.removeAnimationListener();
    }

    @Override
    public void setX(float x){
        super.setX(x);
    }

    @Override
    public void setY(float y){
        super.setY(y);
    }

    @Override
    public void move(float x, float y){
        super.move(x, y);
    }

    @Override
    public void draw(Canvas canvas){
        if (super.sprites != null){
            for (int i = 0; i < super.sprites.length; i++){
                canvas.drawBitmap(super.sprites[i].getTexture().getImage(), getX(), getY(), super.paint);
            }
        }
        canvas.drawBitmap(texture.getImage(), getX(), getY(), paint);
        if (this.text != null){
            this.text.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getActionMasked();
        float x = event.getX();
        float y = event.getY();

        if (checkCollision(new RectF(x,y,x,y), this.area)){
            if (listeners != null) {
                return this.processListeners(new ClickEvent(action, x, y, true, this.id, this.name));
            }
        }
        else {
            if (clicked)
                this.processListeners(new ClickEvent(MotionEvent.ACTION_UP, x, y, false, this.id, this.name));
        }

        return true;
    }
}
