package phdev.com.br.metafighter.screens;

import android.graphics.RectF;
import android.view.MotionEvent;

import phdev.com.br.metafighter.ConnectionManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.Entity;
import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.cmp.misc.Constant;
import phdev.com.br.metafighter.cmp.misc.GameContext;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.LabelMove;
import phdev.com.br.metafighter.cmp.window.Scene;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.connections.packets.Move;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class TesteScreen extends Screen {

    private final int myID;

    private ConnectionManager manager;

    private Scene mainscene;

    private Texture backgroundTexture;
    private Texture labelTexture;

    private LabelMove player1;

    private LabelMove player2;

    private BackGround backGround;


    public TesteScreen(GameContext context, int type) {
        super(context);

        switch (type){
            case Constant.GAMEMODE_SINGLEPLAYER:
                myID = type;

                // Pra teste, retirar****************
                context.getConnectionType().init();
                manager = context.getConnectionType();
                //***********************************

                break;
            case Constant.GAMEMODE_MULTIPLAYER_HOST:
                myID = type;
                context.getConnectionType().init();
                manager = context.getConnectionType();
                break;
            case Constant.GAMEMODE_MULTIPLAYER_JOIN:
                myID = type;
                context.getConnectionType().init();
                manager = context.getConnectionType();
                break;
            default:
                myID = type;
                break;
        }

        init();

    }

    @Override
    protected boolean loadTextures() {

        backgroundTexture = new Texture("images/backgrounds/background1.png");
        labelTexture = new Texture("images/labels/label2.png");

        return true;
    }

    @Override
    protected boolean loadFonts() {
        return true;
    }

    @Override
    protected boolean loadSounds() {
        return true;
    }

    @Override
    protected boolean loadComponents() {

        RectF screenSize = new RectF(GameParameters.getInstance().screenSize);

        backGround = new BackGround(new RectF(screenSize), backgroundTexture);
        backGround.setId(0);

        RectF labelSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);

        player1 = new LabelMove(labelSize, null, labelTexture);
        player1.setId(1);

        player2 = new LabelMove(new RectF(labelSize), null, labelTexture);
        player2.setX(screenSize.centerX());
        player2.setY(screenSize.centerY());
        player2.setId(2);

        if (myID == Constant.GAMEMODE_MULTIPLAYER_HOST){
            player1.addUser(myID);
            player2.addUser(10);
        }
        else
        {
            player1.addUser(10);
            player2.addUser(myID);
        }

        mainscene = new Scene();
        mainscene.add(backGround);
        mainscene.add(player1);
        mainscene.add(player2);

        currentScene = mainscene;

        return true;
    }

    @Override
    public void update(){
        super.update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        //super.onTouchEvent(event);

        if (Entity.checkCollision(new RectF(event.getX(), event.getY(), event.getX(), event.getY()), player1.getArea())){
            if (myID == player1.getUser()) {
                player1.onTouchEvent(event);
                manager.addPacketsToWrite(new Move(1, player1.getArea().left, player1.getArea().top));
            }
            else
                log("Vc não é dono deste objeto");
        }

        if (Entity.checkCollision(new RectF(event.getX(), event.getY(), event.getX(), event.getY()), player2.getArea())){
            if (myID == player2.getUser()){
                player2.onTouchEvent(event);
                manager.addPacketsToWrite(new Move(1, player2.getArea().left, player2.getArea().top));
            }
            else
                log("Vc não é dono deste objeto");
        }

        return true;
    }

}
