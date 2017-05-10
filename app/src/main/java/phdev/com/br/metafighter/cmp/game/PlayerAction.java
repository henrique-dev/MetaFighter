package phdev.com.br.metafighter.cmp.game;

import android.graphics.RectF;
import android.util.Log;

import phdev.com.br.metafighter.cmp.graphics.Sprite;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class PlayerAction {

    private Collision[] collisions;

    private Sprite[] sprites;

    private int currentSprite;
    private int counter;
    private int totalSprites;

    public PlayerAction(Sprite[] sprites){
        this.sprites = sprites;
        counter = 0;
        currentSprite = 0;
        totalSprites = sprites.length-1;

        collisions = new Collision[sprites.length];

        for (int i=0; i<=totalSprites; i++){
            collisions[i] = new Collision(Collision.detectCollisionFromTexture(sprites[i].getTexture(), 10, 10, 30));
        }
    }

    public PlayerAction execute(){
        counter = 0;
        currentSprite = 0;
        return this;
    }

    public Collision[] getCollisions() {
        return collisions;
    }

    public RectF[] getCurrentCollision(){
        return collisions[currentSprite].getCollision();
    }

    public Sprite getSprite(){

        currentSprite = counter / 8;

        if (currentSprite > totalSprites) {
            counter = 0;
        } else {
            counter++;
            return sprites[currentSprite];
        }

        return null;
    }

}
