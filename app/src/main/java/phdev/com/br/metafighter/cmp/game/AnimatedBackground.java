package phdev.com.br.metafighter.cmp.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import phdev.com.br.metafighter.cmp.GameEntity;
import phdev.com.br.metafighter.cmp.graphics.Sprite;
import phdev.com.br.metafighter.cmp.window.BackGround;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class AnimatedBackground extends BackGround {

    public static final int LEFT = -1, RIGHT = 1, NO_MOVEMENT = 0, UP = -1, DOWN = 1;

    private float velocity;

    private int directionX, directionY;

    private boolean moving;

    private boolean autoMoving;

    public AnimatedBackground(BackGround backGround, float velocity, int directionX, int directionY, boolean moving, boolean autoMoving){
        super(backGround);
        this.velocity = velocity;
        this.directionX = directionX;
        this.directionY = directionY;
        this.moving = moving;
        this.autoMoving = autoMoving;
    }

    public void move(){
        super.setX( super.getX() + velocity * directionX );
        super.setY( super.getY() + velocity * directionY );

        if(directionX != 0)
            if(directionX == -1 && super.getX() + super.drawableArea.width() <= 0){
                super.setX(super.drawableArea.width());
                logMessages(this, "masoq");
            }
            else
            if(directionX == 1 && super.getX() >= super.drawableArea.width()){
                super.setX(-super.drawableArea.width());
                logMessages(this, "masoq");
            }
    }

    @Override
    public void move(float x, float y){
        super.setX(super.getX() + x);
        super.setY(super.getY() + y);

        if(super.getX() + super.drawableArea.width() < 0){
            super.setX(super.drawableArea.width());

        }
        else
        if(super.getX() > super.drawableArea.width()){
            super.setX(-super.drawableArea.width());
        }
    }

    @Override
    public void update() {
        super.update();

        if(moving)
            if(autoMoving)
                move();

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(texture.getImage(), super.getX() + super.drawableArea.width(), super.getY(), paint);
        canvas.drawBitmap(texture.getImage(), super.getX() - super.drawableArea.width(), super.getY(), paint);
    }

}
