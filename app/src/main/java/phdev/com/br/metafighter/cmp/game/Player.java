package phdev.com.br.metafighter.cmp.game;

import android.graphics.Canvas;
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

    private final int STOPACTION = 0;
    private final int MOVEACTION = 1;
    private final int JUMPACTION = 2;
    private final int CROUCHACTION = 3;
    private final int PUNCHACTION = 4;
    private final int KICKACTION = 5;

    private Paint paint;

    private PlayerAction stopAction;
    private PlayerAction moveActionLeft;
    private PlayerAction moveActionRight;
    private PlayerAction jumpAction;
    private PlayerAction crouchAction;
    private PlayerAction punchAction;
    private PlayerAction kickAction;
    private PlayerAction currentAction;

    private Sprite currentSprite;

    private RectF[] currentCollision;

    private RectF size;
    private float y;
    private float x;

    private Timer jumpTimer;

    private boolean crouchState;

    private boolean jumpState;
    private boolean jumping;

    private boolean movingstate;
    private int directionX = 0;
    private int velocityX = 5;

    private String name;
    private int charID;
    private Sprite[] view;

    public Player() {
    }

    public Player(Character character, RectF size){
        log("Criando um player");
        Sprite[] sprites = character.getSprites();

        stopAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 0, 3));
        moveActionLeft = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 4, 7));
        moveActionRight = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 8, 11));
        //jumpAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 0, 5));
        //crouchAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 0, 5));
        //punchAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 0, 5));
        //kickAction = new PlayerAction(Sprite.getSpritesFromSprites(sprites, 0, 5));

        changeCurrentAction(stopAction);
        //currentAction = stopAction;

        x = size.left;
        y = size.top;
        this.size = size;

        paint = new Paint();

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

    private void jump(){
        if (!jumpState){
            jumpState = true;
            jumpTimer = new Timer();
            jumpTimer.start();
            log("Pulou");
        }
    }

    private void move(int x, int y){
        log("Se movendo");
    }

    private void crouch(){
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

    protected static void log(String msg){
        if(!GameParameters.getInstance().debug)
            return;
        GameParameters.getInstance().log(msg);
    }

    @Override
    public void draw(Canvas canvas) {

        if (currentSprite != null){
            canvas.drawBitmap(currentSprite.getTexture().getImage(), x, y, paint);

            for (int i=0; i<currentCollision.length; i++){
                canvas.drawRect(currentCollision[i], paint);
            }
        }



    }

    @Override
    public void update() {

        Sprite tmpSprite = currentAction.getSprite();

        if (tmpSprite != null){
            currentSprite = tmpSprite;
            currentCollision = currentAction.getCurrentCollision();
        }
        else
            changeCurrentAction(currentAction.execute());
            //currentAction = currentAction.execute();


        if (jumping){
            jump();
        }

        if (jumpState){
            float velocidade = 8 + (-20 * jumpTimer.getTicks()/1000000000);
            if (y > size.top) {
                jumpState = false;
                y = size.top;
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
            currentAction = stopAction;
        }

        @Override
        public void arrowLeftPressed(){
            currentAction = moveActionLeft;
            movingstate = true;
            directionX = -1;
        }

        @Override
        public void arrowLeftReleased(){
            movingstate = false;
            currentAction = stopAction;
        }

        @Override
        public void arrowRightPressed(){
            currentAction = moveActionRight;
            movingstate = true;
            directionX = 1;
        }

        @Override
        public void arrowRightReleased(){
            movingstate = false;
            currentAction = stopAction;
        }

        @Override
        public void action1Performed(){
            armAct();
        }

    };
}
