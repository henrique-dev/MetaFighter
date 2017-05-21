package phdev.com.br.metafighter.screens;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.util.Log;
import android.view.MotionEvent;

import java.io.IOException;

import phdev.com.br.metafighter.BluetoothManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.SoundManager;
import phdev.com.br.metafighter.cmp.Component;
import phdev.com.br.metafighter.cmp.Entity;
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
    private Scene preBattleScene;

    private BackGround backGround;
    private BackGround preBattleBackground;

    private Label timerLabel;

    private Timer matchTimer;
    private int currentTime = 0;

    private LifeHud lifeHudPlayer1;
    private LifeHud lifeHudPlayer2;

    private Texture backgroundTexture;
    private Texture preBattleBackgroundTexture;
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
    }

    @Override
    protected boolean loadTextures() {

        backgroundTexture = new Texture("images/backgrounds/background9.png");
        preBattleBackgroundTexture = new Texture("images/backgrounds/background3.png");

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

        final RectF screenSize = GameParameters.getInstance().screenSize;

        mainScene = new Scene(){

            @Override
            public void init() {

                backGround = new BackGround(screenSize, backgroundTexture);

                float divx = screenSize.width()/32;
                float divy = screenSize.height()/16;

                RectF playerArea = new RectF(screenSize.centerX() - divx*4, screenSize.bottom - divy*10, screenSize.centerX() + divx*4, screenSize.bottom);

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


                super.add(backGround);
                super.add(lifeHudPlayer1);
                super.add(lifeHudPlayer2);
                super.add(timerLabel);
                super.add(player1);
                super.add(controller);
            }

            @Override
            public Scene start(){
                matchTimer = new Timer();
                matchTimer.start();
                return this;
            }

            @Override
            public void update(){
                super.update();

                int tempTime = (int)(matchTimer.getTicks()/1000000000);
                if (tempTime != currentTime) {
                    if (timerLabel != null) {
                        timerLabel.getText().setText((99 - tempTime) + "");
                        currentTime = tempTime;
                    }
                }
            }
        };

        preBattleScene = new Scene(){

            private Timer timer;

            @Override
            public void init() {

                preBattleBackground = new BackGround(screenSize, preBattleBackgroundTexture);


                super.add(new BackGround(screenSize, Color.WHITE));
                super.add(preBattleBackground);

                timer = new Timer();
                timer.start();
            }

            @Override
            public void update(){
                super.update();

                if (timer.getTicks()/1000000000 > 3){
                    currentScene = mainScene.start();
                    preBattleScene = null;
                }

            }
        };

        currentScene = preBattleScene;

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
                //tmpTexture = new Texture("images/characters/teste/action/sprites.png");
                log("Carregou a textura");
                //tmpSpriteAction = Sprite.getSpriteFromTexture(tmpTexture, 4, 4);
                log("Dividiu a textura em sprites");

                tmpSpriteAction = new Sprite[1];


                try {
                    String paths[] = GameParameters.getInstance().assetManager.list("images/characters/teste/sprites");

                    int numeroSpritesAchados = 0;

                    for (int i=0; i<paths.length; i++){
                        numeroSpritesAchados += GameParameters.getInstance().assetManager.list("images/characters/teste/sprites/" + paths[i]).length;
                    }

                    log("Numero de sprites: " + numeroSpritesAchados);

                    int contador = 0;
                    tmpSpriteAction = new Sprite[numeroSpritesAchados];

                    for (int i=0; i<paths.length; i++){
                        log(paths[i] + "");
                        String arqs[] = GameParameters.getInstance().assetManager.list("images/characters/teste/sprites/" + paths[i]);
                        for (int j=0; j < arqs.length; j++){
                            tmpSpriteAction[contador++] = new Sprite(new Texture("images/characters/teste/sprites/" + paths[i] + "/" + arqs[j]));
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


                for (Sprite aTmpSpriteAction : tmpSpriteAction)
                    aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
                log("Ajustou o tamanho de todos os sprites");
                character = new Character(tmpSpriteAction, null, "Teste");
                log("Criou o character");
        }
        return new Player(character, size);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
    }

    @Override
    public void update(){
        super.update();


        if (currentTime > 99)
            new MainScreen(context);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        super.onTouchEvent(event);


        return true;
    }
}
