package phdev.com.br.metafighter.screens;

import android.graphics.Color;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.Random;

import phdev.com.br.metafighter.BluetoothManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.event.listeners.EventListener;
import phdev.com.br.metafighter.cmp.game.GameLabel;
import phdev.com.br.metafighter.cmp.game.LifeHud;
import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.cmp.misc.Timer;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.Label;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.window.Text;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MatchScreen extends Screen {

    private BluetoothManager manager;

    private BackGround backGround;

    private Label timerLabel;

    private Timer matchTimer;
    private int currentTime = 0;

    private LifeHud lifeHudPlayer1;
    private LifeHud lifeHudPlayer2;

    private Texture backgroundTexture;
    private Texture lifeHudTexture;

    public MatchScreen(EventListener listener, BluetoothManager manager) {
        super(listener);
        this.manager = manager;

        matchTimer = new Timer();
        matchTimer.start();
    }

    @Override
    protected boolean loadTextures() {

        this.backgroundTexture = new Texture("images/backgrounds/background4.png");
        this.lifeHudTexture = new Texture("images/labels/label5.png");

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

        add(backGround);
        add(lifeHudPlayer1);
        add(lifeHudPlayer2);
        add(timerLabel);

        return true;
    }

    @Override
    public void update(){

        if (currentTime > 100)
            new MainScreen(listener);

        int tempTime = (int)(matchTimer.getTicks()/1000000000);
        if (tempTime != currentTime) {
            timerLabel.getText().setText((99 - tempTime) + "");
            currentTime = tempTime;
            log(currentTime + "");
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
            new MainScreen(listener);
        return true;
    }
}
