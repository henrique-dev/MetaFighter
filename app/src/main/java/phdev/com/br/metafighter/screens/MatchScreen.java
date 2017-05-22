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
import phdev.com.br.metafighter.cmp.game.Collision;
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

                //RectF player1Area = new RectF(screenSize.centerX() - divx*7, screenSize.bottom - divy*10, screenSize.centerX() + divx*1, screenSize.bottom);

                RectF playerArea = new RectF(0,0, divx*7, divy*10);

                //RectF player1Area = new RectF(screenSize.centerX() - divx*7, screenSize.bottom - divy*10, screenSize.centerX() + divx*1, screenSize.bottom);


                //Carregamento dos personagens dos jogadores

                player1 = loadPlayer(charIDplayer1,
                        new RectF(screenSize.centerX() - playerArea.width(), screenSize.bottom - playerArea.height(), screenSize.centerX(), screenSize.bottom), false);

                player2 = loadPlayer(charIDplayer2,
                        new RectF(screenSize.centerX() - playerArea.width(), screenSize.bottom - playerArea.height(), screenSize.centerX(), screenSize.bottom), true);

                //player1 = loadPlayer(charIDplayer1, player1Area, false);
                //player2 = loadPlayer(charIDplayer2, player2Area, true);

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
                super.add(player2);
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

    private Player loadPlayer(int charID, RectF size, boolean invert){
        Character character = null;
        Sprite[] tmpSpriteAction;
        Texture tmpTexture;
        switch (charID){
            case Character.KAILA:
                //tmpTexture = new Texture("images/characters/kaila/action/sprites.png");
                //tmpSpriteAction = Sprite.getSpriteFromTexture(tmpTexture, 5, 5);
                tmpSpriteAction = loadTextureChar("kaila");
                for (Sprite aTmpSpriteAction : tmpSpriteAction)
                    aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
                character = new Character(tmpSpriteAction, null, "Kaila");
                break;
            case Character.GUEDES:
                //tmpTexture = new Texture("images/characters/guedes/action/sprites.png");
                //tmpSpriteAction = Sprite.getSpriteFromTexture(tmpTexture, 5, 5);
                tmpSpriteAction = loadTextureChar("guedes");
                for (Sprite aTmpSpriteAction : tmpSpriteAction)
                    aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
                character = new Character(tmpSpriteAction, null, "Carlos Guedes");
                break;
            case Character.LUIZ:
                //tmpTexture = new Texture("images/characters/luiz/action/sprites.png");
                //tmpSpriteAction = Sprite.getSpriteFromTexture(tmpTexture, 5, 5);
                tmpSpriteAction = loadTextureChar("luiz");
                for (Sprite aTmpSpriteAction : tmpSpriteAction)
                    aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
                character = new Character(tmpSpriteAction, null, "Luiz Silva");
                break;
            case Character.PATRICIA:
                //tmpTexture = new Texture("images/characters/patricia/action/sprites.png");
                //tmpSpriteAction = Sprite.getSpriteFromTexture(tmpTexture, 5, 5);
                tmpSpriteAction = loadTextureChar("patricia");
                for (Sprite aTmpSpriteAction : tmpSpriteAction)
                    aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
                character = new Character(tmpSpriteAction, null, "Patricia");
                break;
            case Character.QUELE:
                //tmpTexture = new Texture("images/characters/quele/action/sprites.png");
                //tmpSpriteAction = Sprite.getSpriteFromTexture(tmpTexture, 5, 5);
                tmpSpriteAction = loadTextureChar("quele");
                for (Sprite aTmpSpriteAction : tmpSpriteAction)
                    aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
                character = new Character(tmpSpriteAction, null, "Quele");
                break;
            case Character.ROMULO:
                //tmpTexture = new Texture("images/characters/romulo/action/sprites.png");
                //tmpSpriteAction = Sprite.getSpriteFromTexture(tmpTexture, 5, 5);
                tmpSpriteAction = loadTextureChar("romulo");
                for (Sprite aTmpSpriteAction : tmpSpriteAction)
                    aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
                character = new Character(tmpSpriteAction, null, "Romulo");
                break;
            case Character.TESTE:

                tmpSpriteAction = loadTextureChar("teste");

                for (Sprite aTmpSpriteAction : tmpSpriteAction)
                    aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
                log("Ajustou o tamanho de todos os sprites");
                character = new Character(tmpSpriteAction, null, "Teste");
                log("Criou o character");
        }
        return new Player(character, size, invert);
    }

    private Sprite[] loadTextureChar(String name){

        Sprite[] tmpSpriteAction = null;

        try {
            String paths[] = GameParameters.getInstance().assetManager.list("images/characters/" + name + "/action");

            int numeroSpritesAchados = 0;

            for (int i=0; i<paths.length; i++){
                numeroSpritesAchados += GameParameters.getInstance().assetManager.list("images/characters/" + name + "/action/" + paths[i]).length;
            }

            log("Numero de sprites: " + numeroSpritesAchados);

            int contador = 0;
            tmpSpriteAction = new Sprite[numeroSpritesAchados];

            for (int i=0; i<paths.length; i++){
                log(paths[i] + "");
                String arqs[] = GameParameters.getInstance().assetManager.list("images/characters/" + name + "/action/" + paths[i]);
                for (int j=0; j < arqs.length; j++){
                    tmpSpriteAction[contador++] = new Sprite(new Texture("images/characters/" + name + "/action/" + paths[i] + "/" + arqs[j]));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpSpriteAction;
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);



        RectF[][] player1Collision = null;
        RectF[][] player2Collision = null;

        player2Collision = player2.getCurrentCollision();
        player1Collision = player1.getCurrentCollision();

        /*
        for (int i=0; i<currentCollision.length; i++){
            canvas.drawRect(currentCollision[i].left+ player2.getX(), currentCollision[i].top + player2.getY(), currentCollision[i].right+player2.getX(), currentCollision[i].bottom+player2.getY(), new Paint());
        }*/




        for (RectF[] aCurrentCollision : player2Collision)
            for (RectF bCurrentCollision : aCurrentCollision)
                if (bCurrentCollision != null) {
                    canvas.drawRect(bCurrentCollision.left + player2.getX(), bCurrentCollision.top + player2.getY(), bCurrentCollision.right + player2.getX(), bCurrentCollision.bottom + player2.getY(), new Paint());
                }

        for (RectF[] aCurrentCollision : player1Collision)
            for (RectF bCurrentCollision : aCurrentCollision)
                if (bCurrentCollision != null) {
                    canvas.drawRect(bCurrentCollision.left + player1.getX(), bCurrentCollision.top + player1.getY(), bCurrentCollision.right + player1.getX(), bCurrentCollision.bottom + player1.getY(), new Paint());
                }



    }

    protected boolean checkCollision(RectF[][] A, float AX, float AY, RectF[][] B, float BX, float BY){

        for (int i=0; i<15; i++){
            for (int j=0; j<15; j++){
                if (A[i][j] != null) {
                    for (int k = 0; k < 15; k++) {
                        for (int l = 0; l < 15; l++) {
                            if (B[k][l] != null) {
                                /*
                                if (((AX + A[i][j].left >= BX + B[k][l].left && AX + A[i][j].left <= BX + B[k][l].right) && (AX + A[i][j].right >= BX + B[k][l].left && AX + A[i][j].right <= BX + B[k][l].right)) &&
                                        ((AY + A[i][j].top >= BY + B[k][l].top && AY + A[i][j].top <= BY + B[k][l].bottom) && (AY + A[i][j].bottom >= BY + B[k][l].top && AY + A[i][j].bottom <= BY + B[k][l].bottom))) {
                                    log("Colidiu");
                                    log("AX: " + AX + " -  " + "A: (" + (AX + A[i][j].left) + ", " + (AY + A[i][j].top) + ", " + (AX + A[i][j].right) + ", " + (AY + A[i][j].bottom));
                                    log("BX: " + BX + " -  " + "B: (" + (BX + B[k][l].left) + ", " + (BY + B[k][l].top) + ", " + (BX + B[k][l].right) + ", " + (BY + B[k][l].bottom));
                                    return true;
                                }
                                */
                                if (RectF.intersects(new RectF(AX + A[i][j].left, AY + A[i][j].top, AX + A[i][j].right, AY + A[i][j].bottom),
                                        new RectF(BX + B[k][l].left, BY + B[k][l].top, BX + B[k][l].right, BY + B[k][l].bottom))){
                                    log("Colidiu");
                                    /*
                                    log("AX: " + AX + " -  " + "A: (" + (AX + A[i][j].left) + ", " + (AY + A[i][j].top) + ", " + (AX + A[i][j].right) + ", " + (AY + A[i][j].bottom));
                                    log("BX: " + BX + " -  " + "B: (" + (BX + B[k][l].left) + ", " + (BY + B[k][l].top) + ", " + (BX + B[k][l].right) + ", " + (BY + B[k][l].bottom));
                                    */
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        /*
        for (RectF[] aCurrentCollision : A)
            for (RectF tmpA : aCurrentCollision)
                if (tmpA != null) {
                    for (RectF[] cCurrentCollision : B)
                        for (RectF tmpB : cCurrentCollision)
                            if (tmpB != null) {

                                if(((AX + tmpA.left >= BX + tmpB.left && AX+ tmpA.left <= BX + tmpB.right) && (AX + tmpA.right >= BX + tmpB.left && AX + tmpA.right <= BX + tmpB.right)) &&
                                        ((AY + tmpA.top >= BY + tmpB.top && AY + tmpA.top <= BY + tmpB.bottom) && (AY + tmpA.bottom >= BY + tmpB.top && AY + tmpA.bottom <= BY + tmpB.bottom))) {
                                    log("Colidiu");
                                    log("AX: " + AX + " -  " + "A: (" + (AX + tmpA.left) + ", " + (AY + tmpA.top) + ", " + (AX + tmpA.right) + ", " + (AY + tmpA.bottom));
                                    log("BX: " + BX + " -  " + "B: (" + (BX + tmpB.left) + ", " + (BY + tmpB.top) + ", " + (BX + tmpB.right) + ", " + (BY + tmpB.bottom));
                                    return true;
                                }
                            }
                }
                */
        return false;
    }

    @Override
    public void update(){
        super.update();

        checkCollision(player1.getCurrentCollision(), player1.getX(), player1.getY(),
                player2.getCurrentCollision(), player2.getX(), player2.getY());

        /*
        if (currentTime > 99)
            new MainScreen(context);
            */

        //Player.checkCollision(player1)

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        super.onTouchEvent(event);


        return true;
    }
}
