package com.br.phdev.metafighter.cmp.game;

import com.br.phdev.metafighter.cmp.graphics.Sprite;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class PlayerAction {

    public final static int MAX_COLLISION_BOX_ROW = 15;
    public final static int MAX_COLLISION_BOX_COLUMN = 15;

    private Collision[] collisions;

    private Sprite[] sprites;

    private int currentSprite;
    private int counter;
    private int totalSprites;

    private int type;

    private int divFrame;

    public PlayerAction(Sprite[] sprites, int divFrame, int type, boolean collisionOn){
        this.sprites = sprites;
        counter = 0;
        currentSprite = 0;
        this.divFrame = divFrame;
        totalSprites = sprites.length-1;
        this.type = type;

        if (collisionOn){
            collisions = new Collision[sprites.length];
            for (int i=0; i<=totalSprites; i++){
                //collisions[i] = new Collision(Collision.detectCollisionFromTexture(sprites[i].getTexture(), 20, 20, 25));
                collisions[i] = new Collision().detectCollisionFromTexture(sprites[i].getTexture(), MAX_COLLISION_BOX_ROW, MAX_COLLISION_BOX_COLUMN, 25);
            }
        }
    }

    public PlayerAction execute(){
        counter = 0;
        currentSprite = 0;
        return this;
    }

    public PlayerAction execute(int counter){
        this.counter = divFrame+counter;
        currentSprite = 0;
        return this;
    }

    public Collision[] getCollisions() {
        return collisions;
    }

    public Collision getCurrentCollision(){
        if (collisions == null)
            return null;
        return collisions[currentSprite];
    }

    public boolean isTheLastSprite(){
        return currentSprite == totalSprites;
    }

    public int getType(){
        return type;
    }

    public Sprite getSprite(){

        currentSprite = counter / divFrame;

        if (currentSprite > totalSprites) {
            //counter = 0;
        } else {
            counter++;
            return sprites[currentSprite];
        }
        return null;
    }

    public Sprite getLastSprite(){
        return sprites[totalSprites];
    }

}
