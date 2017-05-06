package phdev.com.br.metafighter.screens;

import android.graphics.Color;
import android.graphics.RectF;
import android.view.MotionEvent;

import phdev.com.br.metafighter.BluetoothManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.event.listeners.EventListener;
import phdev.com.br.metafighter.cmp.game.Player;
import phdev.com.br.metafighter.cmp.misc.Controller;
import phdev.com.br.metafighter.cmp.game.LifeHud;
import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.cmp.misc.Timer;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.Label;
import phdev.com.br.metafighter.cmp.window.Scene;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.window.Text;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MatchScreen extends Screen {

    private Scene mainScene;

    private BluetoothManager manager;

    private BackGround backGround;

    private Label timerLabel;

    private Timer matchTimer;
    private int currentTime = 0;

    private LifeHud lifeHudPlayer1;
    private LifeHud lifeHudPlayer2;

    private Texture backgroundTexture;
    private Texture lifeHudTexture;
    private Texture controllerDirTexture;
    private Texture controllerButTexture;

    private Player player1;
    private Player player2;

    private Controller controller;

    public MatchScreen(EventListener listener, BluetoothManager manager) {
        super(listener);
        this.manager = manager;

        matchTimer = new Timer();
        matchTimer.start();
    }

    @Override
    protected boolean loadTextures() {

        backgroundTexture = new Texture("images/backgrounds/background9.png");
        lifeHudTexture = new Texture("images/labels/label5.png");
        controllerDirTexture = new Texture("cmp/controller/directionalBase.png");

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

        RectF screenSize = GameParameters.getInstance().screenSize;

        this.backGround = new BackGround(screenSize, this.backgroundTexture);

        float divx = screenSize.width()/32;
        float divy = screenSize.height()/16;

        RectF lifeHudArea = new RectF(0,0,divx*13, divy);

        lifeHudPlayer1 = new LifeHud( new RectF(divx, divy, divx + lifeHudArea.width(), divy + lifeHudArea.height()), lifeHudTexture, "Player 1");
        lifeHudPlayer1.getText().setHorizontalAlignment(Text.LEFT);
        lifeHudPlayer1.getText().getPaint().setColor(Color.YELLOW);

        lifeHudPlayer2 = new LifeHud( new RectF(screenSize.right - divx - lifeHudArea.width(),
                divy, screenSize.right - divx, divy + lifeHudArea.height()), lifeHudTexture, "Player 2");
        lifeHudPlayer2.getText().setHorizontalAlignment(Text.RIGHT);
        lifeHudPlayer2.getText().getPaint().setColor(Color.YELLOW);


        timerLabel = new Label(
                new RectF(lifeHudPlayer1.getArea().right, 0, lifeHudPlayer2.getArea().left, lifeHudPlayer1.getArea().bottom + divy),
                "99", null);
        timerLabel.getText().setTextSize( Text.adaptText(new String[]{"99"}, timerLabel.getArea()) );
        timerLabel.getText().getPaint().setColor(Color.WHITE);
        timerLabel.getPaint().setAlpha(0);

        RectF areaController = new RectF(0,0, 120, 120);

        controller = new Controller(
                new RectF(10, screenSize.bottom - 10 - areaController.height(), 10 + areaController.width(),screenSize.bottom-10 )
                , controllerDirTexture, null, null, new Player());




        mainScene = new Scene();
        mainScene.add(backGround);
        mainScene.add(lifeHudPlayer1);
        mainScene.add(lifeHudPlayer2);
        mainScene.add(timerLabel);
        mainScene.add(controller);



        currentScene = mainScene;

        //add(backGround);
        //add(lifeHudPlayer1);
        //add(lifeHudPlayer2);
        //add(timerLabel);

        return true;
    }

    @Override
    public void update(){

        if (currentTime > 99)
            new MainScreen(listener);

        int tempTime = (int)(matchTimer.getTicks()/1000000000);
        if (tempTime != currentTime) {
            timerLabel.getText().setText((99 - tempTime) + "");
            currentTime = tempTime;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        super.onTouchEvent(event);

        return true;
    }
}
