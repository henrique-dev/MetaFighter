package phdev.com.br.metafighter.screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.util.Log;
import android.view.MotionEvent;

import phdev.com.br.metafighter.BluetoothManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.SoundManager;
import phdev.com.br.metafighter.cmp.event.listeners.EventListener;
import phdev.com.br.metafighter.cmp.game.Character;
import phdev.com.br.metafighter.cmp.game.Player;
import phdev.com.br.metafighter.cmp.graphics.Sprite;
import phdev.com.br.metafighter.cmp.misc.Controller;
import phdev.com.br.metafighter.cmp.game.LifeHud;
import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.cmp.misc.GameContext;
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

    private int charIDplayer1;
    private int charIDplayer2;

    private Controller controller;

    public MatchScreen(GameContext context, BluetoothManager manager, int charIDplayer1, int charIDplayer2) {
        super(context);

        this.charIDplayer1 = charIDplayer1;
        this.charIDplayer2 = charIDplayer2;

        init();

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

        RectF playerArea = new RectF(screenSize.centerX() - divx*4, screenSize.bottom - divy*8, screenSize.centerX() + divx*4, screenSize.bottom);

        //Carregamento dos personagens dos jogadores
        //player1 = new Player();
        //player1.setName("Teste");
        player1 = loadPlayer(Character.TESTE, playerArea);
        //player2 = loadPlayer(Character.TESTE, playerArea);

        RectF lifeHudArea = new RectF(0,0,divx*13, divy);

        lifeHudPlayer1 = new LifeHud( new RectF(divx, divy, divx + lifeHudArea.width(), divy + lifeHudArea.height()), lifeHudTexture, "Player 1");
        lifeHudPlayer1.getText().setHorizontalAlignment(Text.LEFT);
        lifeHudPlayer1.getText().getPaint().setColor(Color.BLUE);

        lifeHudPlayer2 = new LifeHud( new RectF(screenSize.right - divx - lifeHudArea.width(),
                divy, screenSize.right - divx, divy + lifeHudArea.height()), lifeHudTexture, "Player 2");
        lifeHudPlayer2.getText().setHorizontalAlignment(Text.RIGHT);
        lifeHudPlayer2.getText().getPaint().setColor(Color.BLUE);


        timerLabel = new Label(
                new RectF(lifeHudPlayer1.getArea().right, 0, lifeHudPlayer2.getArea().left, lifeHudPlayer1.getArea().bottom + divy),
                "99", null);
        timerLabel.getText().setTextSize( Text.adaptText(new String[]{"99"}, timerLabel.getArea()) );
        timerLabel.getText().getPaint().setColor(Color.WHITE);
        timerLabel.getPaint().setAlpha(0);

        RectF areaController = new RectF(0,0, 120, 120);


        controller = new Controller(
                new RectF(10, screenSize.bottom - 10 - areaController.height(), 10 + areaController.width(), screenSize.bottom-10 ) , controllerDirTexture,
                new RectF(screenSize.right - 10 - areaController.width(), screenSize.bottom - 10 - areaController.height(),
                        screenSize.right - 10, screenSize.bottom-10), controllerDirTexture, player1.getControllerListener());





        mainScene = new Scene();
        mainScene.add(backGround);
        mainScene.add(lifeHudPlayer1);
        mainScene.add(lifeHudPlayer2);
        mainScene.add(timerLabel);
        mainScene.add(player1);
        //mainScene.add(player2);
        //mainScene.add(controller);


        currentScene = mainScene;

        return true;
    }

    private Player loadPlayer(int charID, RectF size){
        Character character = null;
        Sprite[] tmpSpriteAction;
        Texture tmpTexture;
        switch (charID){
            case Character.KAILA:
                tmpTexture = new Texture("images/characters/kaila/action/sprites.png");
                tmpSpriteAction = Sprite.getSpriteFromTexture(tmpTexture, 5, 5);
                for (Sprite aTmpSpriteAction : tmpSpriteAction)
                    aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
                character = new Character(tmpSpriteAction, null, "Kaila");
                break;
            case Character.GUEDES:
                tmpTexture = new Texture("images/characters/guedes/action/sprites.png");
                tmpSpriteAction = Sprite.getSpriteFromTexture(tmpTexture, 5, 5);
                for (Sprite aTmpSpriteAction : tmpSpriteAction)
                    aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
                character = new Character(tmpSpriteAction, null, "Carlos Guedes");
                break;
            case Character.LUIZ:
                tmpTexture = new Texture("images/characters/luiz/action/sprites.png");
                tmpSpriteAction = Sprite.getSpriteFromTexture(tmpTexture, 5, 5);
                for (Sprite aTmpSpriteAction : tmpSpriteAction)
                    aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
                character = new Character(tmpSpriteAction, null, "Luiz Silva");
                break;
            case Character.PATRICIA:
                tmpTexture = new Texture("images/characters/patricia/action/sprites.png");
                tmpSpriteAction = Sprite.getSpriteFromTexture(tmpTexture, 5, 5);
                for (Sprite aTmpSpriteAction : tmpSpriteAction)
                    aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
                character = new Character(tmpSpriteAction, null, "Patricia");
                break;
            case Character.QUELE:
                tmpTexture = new Texture("images/characters/quele/action/sprites.png");
                tmpSpriteAction = Sprite.getSpriteFromTexture(tmpTexture, 5, 5);
                for (Sprite aTmpSpriteAction : tmpSpriteAction)
                    aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
                character = new Character(tmpSpriteAction, null, "Quele");
                break;
            case Character.ROMULO:
                tmpTexture = new Texture("images/characters/romulo/action/sprites.png");
                tmpSpriteAction = Sprite.getSpriteFromTexture(tmpTexture, 5, 5);
                for (Sprite aTmpSpriteAction : tmpSpriteAction)
                    aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
                character = new Character(tmpSpriteAction, null, "Romulo");
                break;
            case Character.TESTE:
                tmpTexture = new Texture("images/characters/teste/action/sprites.png");
                log("Carregou a textura");
                tmpSpriteAction = Sprite.getSpriteFromTexture(tmpTexture, 4, 4);
                log("Dividiu a textura em sprites");
                for (Sprite aTmpSpriteAction : tmpSpriteAction)
                    aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
                log("Ajustou o tamanho de todos os sprites");
                character = new Character(tmpSpriteAction, null, "Teste");
                log("Criou o character");
                break;
        }
        return new Player(character, size);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        if (controller != null)
            controller.draw(canvas);
    }

    @Override
    public void update(){
        super.update();

        if (controller != null)
            controller.update();


        if (currentTime > 99)
            new MainScreen(context);

        int tempTime = (int)(matchTimer.getTicks()/1000000000);
        if (tempTime != currentTime) {
            if (timerLabel != null) {
                timerLabel.getText().setText((99 - tempTime) + "");
                currentTime = tempTime;
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        super.onTouchEvent(event);

        if (controller != null)
            controller.onTouchEvent(event);


        return true;
    }
}
