package com.br.phdev.metafighter.cmp.game;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.br.phdev.metafighter.GameParameters;
import com.br.phdev.metafighter.cmp.Component;
import com.br.phdev.metafighter.cmp.event.listeners.ControllerListener;
import com.br.phdev.metafighter.cmp.game.Collision;
import com.br.phdev.metafighter.cmp.game.PlayerAction;
import com.br.phdev.metafighter.cmp.graphics.Sprite;
import com.br.phdev.metafighter.cmp.misc.Timer;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Player implements Component {

    public static final float NORMAL_DISTANCE = 2;
    public static final float VERYCLOSE_DISTANCE = 3;
    public static final float PUNCH_DISTANCE = -20f;
    public static final float KICK_DISTANCE = 4.5f;

    public static final int WALKING_LEFT_ACTION = 0;
    public static final int WALKING_RIGHT_ACTION = 1;
    public static final int WALKING_FRONT = 19;
    public static final int WALKING_BACK = 20;
    public static final int MOVE1_ACTION = 2;
    public static final int MOVE2_ACTION = 3;
    public static final int JUMP2_ACTION = 4;
    public static final int JUMP3_ACTION = 5;
    public static final int JUMP1_ACTION = 6;
    public static final int CROUCH_ACTION = 7;
    public static final int CROUCHED_ACTION = 8;
    public static final int PUNCH1_ACTION = 9;
    public static final int PUNCH2_ACTION = 10;
    public static final int KICK1_ACTION = 11;
    public static final int KICK2_ACTION = 12;
    public static final int GUARD_ACTION = 13;
    public static final int DEFEAT1_ACTION = 14;
    public static final int DEFEAT2_ACTION = 15;
    public static final int VICTORY1_ACTION = 16;
    public static final int VICTORY2_ACTION = 17;
    public static final int DAMAGED_ACTION = 18;

    private boolean actionPerformed;

    // Componentes para depuração

    private Paint debugPaint;

    private Paint paint;

    private PlayerAction moving1Action;
    private PlayerAction moving2Action;
    private PlayerAction walkingActionLeft;
    private PlayerAction walkingActionRight;
    private PlayerAction jump1Action;
    private PlayerAction jump2Action;
    private PlayerAction jump3Action;
    private PlayerAction crouchAction;
    private PlayerAction crouchedAction;

    private PlayerAction punch1Action;
    private PlayerAction punch2Action;
    // cotovelada.....
    // gancho.....

    private PlayerAction kick1Action;
    private PlayerAction kick2Action;
    // joelhada....
    // defesa....

    private PlayerAction guardAction;

    private PlayerAction victory1Action;
    private PlayerAction victory2Action;
    private PlayerAction defeat1Action;
    private PlayerAction defeat2Action;

    private PlayerAction damageAction;
    // cambaleando

    private PlayerAction currentAction;

    private Sprite currentSprite;

    private Collision currentCollision;

    private RectF mainArea;
    private float y;
    private float x;

    private Timer jumpTimer;
    private Double firstTouchGuard;
    private Double secondTouchGuard;

    private boolean crouchState;
    private boolean crouching;

    private boolean jumpState;
    private boolean jumping;

    private boolean guardState;
    private boolean guarding;

    private Timer damagedTimer;
    private boolean damaged;

    private boolean stop;

    private boolean movingstate;
    private int directionX = 0;
    private float velocityX;
    private float velocityY;
    private float acceleration;

    private String name;
    private int charID;
    private Sprite[] view;

    //PARA TESTES

    private LifeHud lifeHud;

    private Matrix matrix;
    private boolean invert;

    private Bot botIA;

    //

    public Player() {
        log("Criando um player");
    }

    public Player(Character character, RectF size, boolean invert){
        log("Criando um player");

        // Componentes de depuração


        //debugPaint = new Paint();
        //debugPaint.setColor(Color.RED);

        //

        // PARA TESTES

        matrix = new Matrix();
        matrix.postScale(-1,1);
        this.invert = invert;

        //

        log("Definindo os sprites de cada ação");

        Sprite[] sprites = character.getSprites();

        walkingActionLeft = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 0, 5, true), 8, WALKING_LEFT_ACTION, true);
        walkingActionRight = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 0, 5, false), 8, WALKING_RIGHT_ACTION, true);
        moving1Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 6, 13, false), 10, MOVE1_ACTION, true);
        moving2Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 6, 13, true), 10, MOVE2_ACTION, true);
        jump1Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 14, 19, false), 3, JUMP1_ACTION, true);
        jump2Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 14, 19, true), 6, JUMP2_ACTION, true);
        jump3Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 19,19, false), 1, JUMP3_ACTION, true);
        crouchAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 20, 25, false), 3, CROUCH_ACTION, true);
        crouchedAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 25, 25, false), 1, CROUCHED_ACTION, true);


        punch1Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 26, 31, false), 2, PUNCH1_ACTION, true);
        punch2Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 26, 31, true), 2, PUNCH2_ACTION, true);


        kick1Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 32, 37, false), 3, KICK1_ACTION, true);
        kick2Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 32, 37, true), 3, KICK2_ACTION, true);

        guardAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 38, 38, false), 1, GUARD_ACTION, true);

        defeat1Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites , 39 ,46, false), 8, DEFEAT1_ACTION, false);
        defeat2Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites , 46 ,46, false), 1, DEFEAT2_ACTION, false);

        victory1Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites , 47 ,61, false), 8, VICTORY1_ACTION, false);
        victory2Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites , 61 ,61, false), 1, VICTORY2_ACTION, false);


        damageAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites , 0 ,1, false), 8, DAMAGED_ACTION, true);

        log("Terminou de definir os sprites de cada ação");

        changeCurrentAction(moving1Action);

        x = size.left;
        y = size.top;

        velocityX = GameParameters.getInstance().screenSize.width() / 150;
        log(velocityX + "");
        velocityY = GameParameters.getInstance().screenSize.height() / 40;
        acceleration = velocityY / 0.5f;

        mainArea = size;

        name = character.getName();

        paint = new Paint();

        log("Criou o player");
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public boolean isActionPerformed() {
        return actionPerformed;
    }

    public LifeHud getLifeHud() {
        return lifeHud;
    }

    public void setLifeHud(LifeHud lifeHud){
        this.lifeHud = lifeHud;
    }

    public ControllerListener getControllerListener(){
        return controllerListener;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RectF getMainArea() {
        return mainArea;
    }

    public void setMainArea(RectF mainArea) {
        this.mainArea = mainArea;
    }

    public int getDirectionX() {
        return directionX;
    }

    public void setDirectionX(int directionX) {
        this.directionX = directionX;
    }

    public Sprite[] getView() {
        return view;
    }

    public void setView(Sprite[] view) {
        this.view = view;
    }

    public int getCharID() {
        return charID;
    }

    public void setCharID(int charID) {
        this.charID = charID;
    }

    public RectF[][] getCurrentCollision(){
        if (currentCollision == null)
            return null;
        if (invert)
            return currentCollision.getCollisionI();
        return currentCollision.getCollision();
    }

    public int getCurrentAction(){
        return currentAction.getType();
    }

    public void setX(float x){
        if (invert)
            this.x = GameParameters.getInstance().screenSize.width() - x - mainArea.width();
        else
            this.x = x;
    }

    public float getX(){
        if (invert)
            return GameParameters.getInstance().screenSize.width() - x - mainArea.width();
        return x;
    }

    public void setY(float y){
        this.y = y;
    }

    public float getY(){
        return y;
    }

    public void setCurrentPlayerAction(int playerActionID){
        switch (playerActionID){
            case WALKING_LEFT_ACTION:
                changeCurrentAction(walkingActionLeft);
                break;
            case WALKING_RIGHT_ACTION:
                changeCurrentAction(walkingActionRight);
                break;
            case MOVE1_ACTION:
                changeCurrentAction(moving1Action.execute());
                break;
            case MOVE2_ACTION:
                changeCurrentAction(moving2Action.execute());
                break;
            case PUNCH1_ACTION:
                changeCurrentAction(punch1Action.execute());
                break;
            case PUNCH2_ACTION:
                changeCurrentAction(punch2Action.execute());
                break;
            case KICK1_ACTION:
                changeCurrentAction(kick1Action.execute());
                break;
            case KICK2_ACTION:
                changeCurrentAction(kick2Action.execute());
                break;
            case GUARD_ACTION:
                changeCurrentAction(guardAction.execute());
                break;
            case JUMP1_ACTION:
                changeCurrentAction(jump1Action.execute());
                break;
            case JUMP2_ACTION:
                changeCurrentAction(jump2Action.execute());
                break;
            case JUMP3_ACTION:
                changeCurrentAction(jump3Action.execute());
                break;
        }
    }

    public void setInvert(boolean invert){
        x = GameParameters.getInstance().screenSize.width() - x - mainArea.width()/2;
        directionX *= -1;
        this.invert = invert;
    }

    public boolean isInvert(){
        return invert;
    }

    public void damaged(float damage){
        if (currentAction.getType() == GUARD_ACTION) {
            lifeHud.decrementHP((0.3f * damage));
            log("Defendeu");
        }
        else{
            lifeHud.decrementHP(damage);
            damaged = true;
            damagedTimer = new Timer().start();
        }

    }

    public void loser(){
        changeCurrentAction(defeat1Action.execute());
    }

    public void winner(){
        changeCurrentAction(victory1Action.execute());
    }


    private void jump(){
        if (!jumpState){
            jumpState = true;
            jumpTimer = new Timer();
            jumpTimer.start();
            changeCurrentAction(jump1Action.execute());
            log("Pulou");
        }
    }

    private void move(){

    }

    private void moveAhead(){

    }

    private void moveBack(){

    }

    private void crouch(){
        if (!crouchState){
            crouchState = true;
            changeCurrentAction(crouchAction.execute());
        }
        log("Se abaixou");
    }

    @Override
    public void draw(Canvas canvas) {

        int saveCount = canvas.save();

        if (debugPaint != null)
            drawDebug(canvas);

        if (invert) {
            canvas.setMatrix(matrix);
            canvas.translate(-GameParameters.getInstance().screenSize.width(), 0);
        }


        if (currentSprite != null){
            /*
            for (RectF[] aCurrentCollision : currentCollision.getCollision()) {
                for (RectF bCurrentCollision : aCurrentCollision)
                    if (bCurrentCollision != null) {
                        canvas.drawRect(bCurrentCollision.left + x, bCurrentCollision.top + y, bCurrentCollision.right + x, bCurrentCollision.bottom + y, paint);
                    }
            }
            */
            canvas.drawBitmap(currentSprite.getTexture().getImage(), x, y, paint);
        }


        canvas.restoreToCount(saveCount);

    }

    @Override
    public void update() {

        if (damagedTimer != null){
            if (damagedTimer.getTicks()/100000000 > 10) {
                damaged = false;
                damagedTimer = null;
            }
        }

        Sprite tmpSprite = currentAction.getSprite();
        actionPerformed = currentAction.isTheLastSprite();

        if (tmpSprite != null){
            currentSprite = tmpSprite;
            if (currentAction.getCurrentCollision() != null)
                currentCollision = currentAction.getCurrentCollision();
        }
        else {
            if (crouching || crouchState){
                changeCurrentAction(crouchedAction.execute());
            }
            else {
                if (currentAction.equals(punch1Action))
                    changeCurrentAction(punch2Action.execute());
                else
                    if (currentAction.equals(punch2Action))
                        changeCurrentAction(moving1Action);
                    else
                        if (currentAction.equals(kick1Action))
                            changeCurrentAction(kick2Action.execute());
                        else
                            if (currentAction.equals(kick2Action))
                                changeCurrentAction(moving1Action);
                            else
                                if (currentAction.equals(guardAction))
                                    changeCurrentAction(guardAction.execute());
                                else
                                    if (currentAction.equals(jump1Action) || currentAction.equals(jump3Action))
                                        changeCurrentAction(jump3Action.execute());
                                    else
                                        if (currentAction.equals(defeat1Action) || currentAction.equals(defeat2Action))
                                            changeCurrentAction(defeat2Action);
                                        else
                                            if (currentAction.equals(victory1Action) || currentAction.equals(victory2Action))
                                                changeCurrentAction(victory2Action);
                                            else
                                                if (currentAction.equals(moving1Action))
                                                    changeCurrentAction(moving2Action.execute(1));
                                                else
                                                    if (currentAction.equals(moving2Action))
                                                        changeCurrentAction(moving1Action.execute(1));
                                                    else
                                                        changeCurrentAction(currentAction.execute());
            }
        }


        if (jumping){
            jump();
        }

        if (crouching){
            crouch();
        }

        if (jumpState){
            float velocidade = velocityY + (-acceleration * jumpTimer.getTicks()/1000000000);

            if (y > mainArea.top) {
                jumpState = false;
                y = mainArea.top;
                if (movingstate)
                    if (directionX > 0)
                        changeCurrentAction(walkingActionRight);
                    else
                        changeCurrentAction(walkingActionLeft);
                else
                    changeCurrentAction(moving1Action);
            }
            else
                y += -velocidade;
        }


        if (movingstate && !stop){
            x += (velocityX * directionX);
       }

    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        return true;
    }

    public boolean onCollision(){

        return false;
    }

    private void changeCurrentAction(PlayerAction playerAction){
        currentAction = playerAction;
    }

    private final ControllerListener controllerListener = new ControllerListener() {

        @Override
        public void arrowUpPressed(){
            jumping = true;
        }

        @Override
        public void arrowUpReleased(){
            jumping = false;
        }

        @Override
        public void arrowDownPressed(){
            crouching = true;
        }

        @Override
        public void arrowDownReleased(){

            crouching = false;
            crouchState = false;
            changeCurrentAction(moving1Action);
        }


        @Override
        public void arrowLeftPressed(){

            if (!damaged){
                if (!jumpState)
                    if (!currentAction.equals(walkingActionLeft))
                        changeCurrentAction(walkingActionLeft);
                movingstate = true;

                if (invert)
                    directionX = 1;
                else
                    directionX = -1;
            }

        }

        @Override
        public void arrowLeftReleased(){
            if (!jumpState)
                currentAction = moving1Action;
            movingstate = false;
        }

        @Override
        public void arrowRightPressed(){

            if (!damaged){
                if (!jumpState)
                    if (!currentAction.equals(walkingActionRight))
                        currentAction = walkingActionRight;

                movingstate = true;

                if (invert) {
                    directionX = -1;
                }
                else {
                    directionX = 1;
                }
            }

        }

        @Override
        public void arrowRightReleased(){
            if (!jumpState)
                currentAction = moving1Action;
            movingstate = false;
        }

        @Override
        public void action1Performed(){
            if (!damaged)
                if (currentAction.equals(moving1Action) || currentAction.equals(moving2Action))
                    changeCurrentAction(punch1Action.execute());
        }

        @Override
        public void action2Performed(){
            if (!damaged)
                if (currentAction.equals(moving1Action) || currentAction.equals(moving2Action))
                    changeCurrentAction(kick1Action.execute());
        }

        @Override
        public void action3Pressed(){
            changeCurrentAction(guardAction.execute());
        }

        @Override
        public void action3Released(){
            //if (currentAction.equals(moving1Action) || currentAction.equals(moving2Action))
            changeCurrentAction(moving1Action);
        }


    };

    public static boolean checkCollision(Collision A, Collision B){

        //RectF[] cA = A.getCollision();
        //RectF[] cB = B.getCollision();

        return false;
    }

    protected void drawDebug(Canvas canvas){

        float newx = GameParameters.getInstance().screenSize.width() - x - mainArea.width();

        if (invert) {
            canvas.drawRect(newx, y, newx + mainArea.width(), y + mainArea.height(), debugPaint);
            canvas.drawText("X: " + newx + " - Y: " + y, newx, y, paint);
        }
        else {
            canvas.drawRect(x, y, x + mainArea.width(), y+ mainArea.height(), debugPaint);
            canvas.drawText("X: " + x + " - Y: " + y, x, y, paint);
        }

    }

    protected static void log(String msg){
        //if(!GameParameters.getInstance().debug)
          //  return;
        GameParameters.getInstance().log(msg);
    }
}
