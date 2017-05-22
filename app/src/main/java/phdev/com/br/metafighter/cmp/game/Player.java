package phdev.com.br.metafighter.cmp.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.Component;
import phdev.com.br.metafighter.cmp.event.listeners.ControllerListener;
import phdev.com.br.metafighter.cmp.graphics.Sprite;
import phdev.com.br.metafighter.cmp.misc.Timer;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Player implements Component {

    // Componentes para depuração

    private Paint debugPaint;

    //

    private final int STOPACTION = 0;
    private final int MOVEACTION = 1;
    private final int JUMPACTION = 2;
    private final int CROUCHACTION = 3;
    private final int PUNCHACTION = 4;
    private final int KICKACTION = 5;

    private Paint paint;

    private PlayerAction movingAction;
    private PlayerAction walkingActionLeft;
    private PlayerAction walkingActionRight;
    private PlayerAction jump1Action;
    private PlayerAction jump2Action;
    private PlayerAction jump3Action;
    private PlayerAction crouchAction;
    private PlayerAction crouchedAction;

    private PlayerAction punchAction;
    private PlayerAction punchAction2;
    // cotovelada.....
    // gancho.....

    private PlayerAction kickAction;
    private PlayerAction kickAction2;
    // joelhada....
    // defesa....

    private PlayerAction guardAction;

    private PlayerAction victoryAction;
    private PlayerAction defeatAction;

    private PlayerAction damageAction;
    // cambaleando

    private PlayerAction currentAction;

    private Sprite currentSprite;

    private Collision currentCollision;

    private RectF size;
    private float y;
    private float x;

    private Timer jumpTimer;
    private Timer guardTimer;
    private Double firstTouchGuard;
    private Double secondTouchGuard;

    private boolean crouchState;
    private boolean crouching;

    private boolean jumpState;
    private boolean jumping;

    private boolean guardState;
    private boolean guarding;

    private boolean movingstate;
    private int directionX = 0;
    private int velocityX = 2;

    private String name;
    private int charID;
    private Sprite[] view;

    //PARA TESTES

    private Matrix matrix;
    private boolean invert;

    //

    public Player() {
        log("Criando um player");
    }

    public Player(Character character, RectF size, boolean invert){
        log("Criando um player");

        // Componentes de depuração

        if (GameParameters.getInstance().debug) {
            debugPaint = new Paint();
            debugPaint.setColor(Color.RED);
        }

        //

        // PARA TESTES

        matrix = new Matrix();
        matrix.postScale(-1,1);
        this.invert = invert;

        //

        Sprite[] sprites = character.getSprites();

        walkingActionLeft = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 0, 5, true), 8);
        walkingActionRight = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 0, 5, false), 8);
        movingAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 6, 13, false), 4);
        jump1Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 14, 19, false), 3);
        jump2Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 14, 19, true), 6);
        jump3Action = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 19,19, false), 1);
        crouchAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 20, 25, false), 3);
        crouchedAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 25, 25, false), 1);


        punchAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 26, 31, false), 2);
        punchAction2 = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 26, 31, true), 2);


        kickAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 32, 37, false), 3);
        kickAction2 = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 32, 37, true), 3);

        guardAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 38, 38, false), 1);

        victoryAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites , 0 ,1, false), 8);
        defeatAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites , 0 ,1, false), 8);


        damageAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites , 0 ,1, false), 8);

        changeCurrentAction(movingAction);

        x = size.left;
        y = size.top;

        this.size = size;

        paint = new Paint();

        log("Criou o player");
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
        if (invert)
            return currentCollision.getCollisionI();
        return currentCollision.getCollision();
    }

    public RectF[][] getCurrentCollisionI(){
        return currentCollision.getCollisionI();
    }

    public float getX(){
        if (invert)
            return GameParameters.getInstance().screenSize.width() - x - size.width();
        return x;
    }

    public float getY(){
        return y;
    }

    public boolean isInvert(){
        return invert;
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


    private void move(int x, int y){
        log("Se movendo");
    }

    private void crouch(){
        if (!crouchState){
            crouchState = true;
            changeCurrentAction(crouchAction.execute());
        }
        log("Se abaixou");
    }

    private void armAct(){
        log("Executou uma ação com o braço");
    }

    private void legAct(){
        log("Executou uma ação com a perna");
    }

    private void defend(){
        log("Defendendo");
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

        /*
        if (invert)
            log((GameParameters.getInstance().screenSize.width() - x - size.width()/2) + "");
        else
            log(x + "");
            */


        if (currentSprite != null){
            for (RectF[] aCurrentCollision : currentCollision.getCollision()) {
                for (RectF bCurrentCollision : aCurrentCollision)
                    if (bCurrentCollision != null) {
                        //canvas.drawRect(bCurrentCollision.left + x, bCurrentCollision.top + y, bCurrentCollision.right + x, bCurrentCollision.bottom + y, paint);
                    }
            }
            canvas.drawBitmap(currentSprite.getTexture().getImage(), x, y, paint);
        }


        canvas.restoreToCount(saveCount);

    }

    @Override
    public void update() {

        Sprite tmpSprite = currentAction.getSprite();

        if (tmpSprite != null){
            currentSprite = tmpSprite;
            currentCollision = currentAction.getCurrentCollision();
        }
        else {
            if (crouching || crouchState){
                changeCurrentAction(crouchedAction.execute());
            }
            else {
                if (currentAction.equals(punchAction))
                    changeCurrentAction(punchAction2);
                else
                    if (currentAction.equals(punchAction2))
                        changeCurrentAction(movingAction);
                    else
                        if (currentAction.equals(kickAction))
                            changeCurrentAction(kickAction2);
                        else
                            if (currentAction.equals(kickAction2))
                                changeCurrentAction(movingAction);
                            else
                                if (currentAction.equals(guardAction))
                                    changeCurrentAction(guardAction.execute());
                                else
                                    if (currentAction.equals(jump1Action) || currentAction.equals(jump3Action))
                                        changeCurrentAction(jump3Action.execute());
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
            float velocidade = 8 + (-20 * jumpTimer.getTicks()/1000000000);

            if (y > size.top) {
                jumpState = false;
                y = size.top;
                changeCurrentAction(movingAction);
            }
            else
                y += -velocidade;
        }


        if (movingstate){
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
            changeCurrentAction(movingAction);
        }


        @Override
        public void arrowLeftPressed(){

            boolean cond = false;

            if (guardTimer == null)
                guardTimer = new Timer().start();
            else {
                if (guardTimer.getTicks()/100000000 < 5)
                    cond = true;
                else
                    if (guardTimer.getTicks()/100000000 > 5)
                        guardTimer.start();
            }

            if (!cond){
                if (!jumpState)
                    changeCurrentAction(walkingActionLeft.execute());
                movingstate = true;
                if (invert)
                    directionX = 1;
                else
                    directionX = -1;
            }
            else {
                guardState = true;
                movingstate = false;
                changeCurrentAction(guardAction.execute());
            }

        }

        @Override
        public void arrowLeftReleased(){
            if (!jumpState)
                currentAction = movingAction;
            movingstate = false;
        }

        @Override
        public void arrowRightPressed(){
            if (!jumpState)
                currentAction = walkingActionRight.execute();
            movingstate = true;

            if (invert)
                directionX = -1;
            else
                directionX = 1;
        }

        @Override
        public void arrowRightReleased(){
            if (!jumpState)
                currentAction = movingAction;
            movingstate = false;
        }

        @Override
        public void action1Performed(){
            //armAct();
            changeCurrentAction(punchAction.execute());
        }

        @Override
        public void action2Performed(){
            changeCurrentAction(kickAction.execute());
        }

        @Override
        public void action3Performed(){

        }

    };

    public static boolean checkCollision(Collision A, Collision B){

        //RectF[] cA = A.getCollision();
        //RectF[] cB = B.getCollision();

        return false;
    }

    protected void drawDebug(Canvas canvas){

        float newx = GameParameters.getInstance().screenSize.width() - x - size.width();

        if (invert) {
            canvas.drawRect(newx, y, newx + size.width(), y + size.height(), debugPaint);
            canvas.drawText("X: " + newx + " - Y: " + y, newx, y, paint);
        }
        else {
            canvas.drawRect(x, y, x + size.width(), y+size.height(), debugPaint);
            canvas.drawText("X: " + x + " - Y: " + y, x, y, paint);
        }

    }

    protected static void log(String msg){
        //if(!GameParameters.getInstance().debug)
          //  return;
        GameParameters.getInstance().log(msg);
    }
}
