package phdev.com.br.metafighter.screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.io.IOException;
import java.util.Random;

import phdev.com.br.metafighter.BluetoothManager;
import phdev.com.br.metafighter.ConnectionManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.connections.packets.Action;
import phdev.com.br.metafighter.cmp.connections.packets.Move;
import phdev.com.br.metafighter.cmp.connections.packets.Packet;
import phdev.com.br.metafighter.cmp.game.Character;
import phdev.com.br.metafighter.cmp.game.LifeHud;
import phdev.com.br.metafighter.cmp.game.Player;
import phdev.com.br.metafighter.cmp.graphics.Sprite;
import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.cmp.misc.Constant;
import phdev.com.br.metafighter.cmp.misc.Controller;
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
public class MultiplayerMatchScreen extends Screen {

    private Scene mainScene;
    private Scene preBattleScene;
    private Scene posBattleScene;

    private ConnectionManager manager;

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

    private Player myPlayer;
    private Player otherPlayer;

    private int charIDplayer1;
    private int charIDplayer2;

    private boolean gameIn;

    private Controller controller;
    private Controller bot;

    // PARA TESTES

    private Random randBot = new Random();
    private boolean executingSomething;
    private Timer taskBot = new Timer();
    private int duration = 0;

    private boolean botMove;

    private int myID;
    private int otherID;

    //

    public MultiplayerMatchScreen(GameContext context, int myID, int charIDplayer1, int charIDplayer2) {
        super(context);

        context.getConnectionType().init();
        this.manager = context.getConnectionType();

        if (myID == Constant.GAMEMODE_MULTIPLAYER_HOST) {
            this.myID = myID;
            otherID = Constant.GAMEMODE_MULTIPLAYER_JOIN;
        }
        else {
            this.myID = myID;
            otherID = Constant.GAMEMODE_MULTIPLAYER_HOST;
        }

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

                try{
                    if (myID == Constant.GAMEMODE_MULTIPLAYER_HOST){
                        myPlayer = loadPlayer(charIDplayer1,
                                new RectF(screenSize.centerX() - playerArea.width(), screenSize.bottom - playerArea.height(), screenSize.centerX(), screenSize.bottom), false);
                        myPlayer.setDirectionX(1);
                        otherPlayer = loadPlayer(charIDplayer2,
                                new RectF(screenSize.centerX() - playerArea.width(), screenSize.bottom - playerArea.height(), screenSize.centerX(), screenSize.bottom), true);
                        otherPlayer.setDirectionX(-1);
                    }
                    else {
                        otherPlayer = loadPlayer(charIDplayer1,
                                new RectF(screenSize.centerX() - playerArea.width(), screenSize.bottom - playerArea.height(), screenSize.centerX(), screenSize.bottom), false);
                        otherPlayer.setDirectionX(1);
                        myPlayer = loadPlayer(charIDplayer2,
                                new RectF(screenSize.centerX() - playerArea.width(), screenSize.bottom - playerArea.height(), screenSize.centerX(), screenSize.bottom), true);
                        myPlayer.setDirectionX(-1);
                    }
                }
                catch (Exception e){
                    log(e.getMessage());
                }

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

                RectF areaController = new RectF(0,0, 170, 170);
                controller = new Controller(
                        new RectF(10, screenSize.bottom - 10 - areaController.height(), 10 + areaController.width(), screenSize.bottom-10 ) , controllerDirTexture,
                        new RectF(screenSize.right - 10 - areaController.width(), screenSize.bottom - 10 - areaController.height(),
                                screenSize.right - 10, screenSize.bottom-10), controllerDirTexture, myPlayer.getControllerListener());

                bot = new Controller(otherPlayer.getControllerListener());

                if (myID == Constant.GAMEMODE_MULTIPLAYER_HOST){
                    myPlayer.setLifeHud(lifeHudPlayer1);
                    otherPlayer.setLifeHud(lifeHudPlayer2);
                }
                else {
                    otherPlayer.setLifeHud(lifeHudPlayer1);
                    myPlayer.setLifeHud(lifeHudPlayer2);
                }


                super.add(backGround);
                super.add(lifeHudPlayer1);
                super.add(lifeHudPlayer2);
                super.add(timerLabel);
                super.add(myPlayer);
                super.add(otherPlayer);
                super.add(controller);
            }

            @Override
            public Scene start(){
                super.start();
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

            @Override
            public void processPacket(Packet packet){
                if (packet != null) {

                    if (currentScene != null) {
                        log("Pacote recebido");
                        if (packet instanceof Move) {
                            int action = ((Move) packet).getValue1();
                            float x = ((Move) packet).getX();
                            float y = ((Move) packet).getY();
                            otherPlayer.setCurrentPlayerAction(action);
                            otherPlayer.setX(x);
                            otherPlayer.setY(y);
                        }
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
                    gameIn = true;
                    preBattleScene = null;
                }

            }
        };

        posBattleScene = new Scene() {
            @Override
            public void init() {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAlpha(150);

                super.add(mainScene);

                super.add(new BackGround(screenSize, paint));
            }
        };

        currentScene = preBattleScene.start();

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
        return new Player(character, size, invert, context.getConnectionType());
    }

    private Sprite[] loadTextureChar(String name){

        RectF screenSize = GameParameters.getInstance().screenSize;

        Sprite[] tmpSpriteAction = null;

        try {
            String paths[] = GameParameters.getInstance().assetManager.list("images/characters/" + name + "/action");

            int numeroSpritesAchados = 0;

            for (int i=0; i<paths.length; i++){
                numeroSpritesAchados += GameParameters.getInstance().assetManager.list("images/characters/" + name + "/action/" + paths[i]).length;
            }

            //log("Numero de sprites: " + numeroSpritesAchados);

            int contador = 0;
            tmpSpriteAction = new Sprite[numeroSpritesAchados];

            for (int i=0; i<paths.length; i++){
                log(paths[i] + "");
                String arqs[] = GameParameters.getInstance().assetManager.list("images/characters/" + name + "/action/" + paths[i]);

                for (int j=0; j < arqs.length; j++){
                    //tmpSpriteAction[contador++] = new Sprite(new Texture("images/characters/" + name + "/action/" + paths[i] + "/" + arqs[j]));
                    tmpSpriteAction[contador++] = new Sprite(new Texture("images/characters/" + name + "/action/" + paths[i] + "/" + ((j+1) + ".png"), (int)screenSize.width()/5, (int)screenSize.height()/5));
                    //log(paths[i] + " / " + arqs[j]);
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



        /*
        RectF[][] player1Collision = null;
        RectF[][] player2Collision = null;

        player2Collision = player2.getCurrentCollision();
        player1Collision = player1.getCurrentCollision();

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
                */



    }

    protected boolean checkCollision(RectF[][] A, float AX, float AY, RectF[][] B, float BX, float BY){

        for (int i=0; i<15; i++){
            for (int j=0; j<15; j++){
                if (A[i][j] != null) {
                    for (int k = 0; k < 15; k++) {
                        for (int l = 0; l < 15; l++) {
                            if (B[k][l] != null) {
                                if (RectF.intersects(new RectF(AX + A[i][j].left, AY + A[i][j].top, AX + A[i][j].right, AY + A[i][j].bottom),
                                        new RectF(BX + B[k][l].left, BY + B[k][l].top, BX + B[k][l].right, BY + B[k][l].bottom))){
                                    //log("Colidiu");
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
        return false;
    }

    float xo;
    float yo;
    int actiono;

    @Override
    public void update(){
        super.update();

        if (gameIn) {
            if (checkCollision(myPlayer.getCurrentCollision(), myPlayer.getX(), myPlayer.getY(),
                    otherPlayer.getCurrentCollision(), otherPlayer.getX(), otherPlayer.getY())) {
                if (myPlayer.getCurrentAction() == Player.KICK1_ACTION) {
                    otherPlayer.damaged(0.5f);
                }
                if (myPlayer.getCurrentAction() == Player.PUNCH1_ACTION) {
                    otherPlayer.damaged(0.2f);
                    //lifeHudPlayer2.decrementHP(0.2f);
                }
                if (otherPlayer.getCurrentAction() == Player.KICK1_ACTION){
                    myPlayer.damaged(0.5f);
                }
                if (otherPlayer.getCurrentAction() == Player.PUNCH1_ACTION)
                    myPlayer.damaged(0.2f);
            }


            if (lifeHudPlayer1.getHP() <=0 || lifeHudPlayer2.getHP() <= 0) {
                matchTimer.pause();

                bot = null;
                controller = null;

                if (lifeHudPlayer1.getHP() <=0){
                    if (myID == Constant.GAMEMODE_MULTIPLAYER_HOST){
                        myPlayer.loser();
                        otherPlayer.winner();
                    }
                    else {
                        myPlayer.winner();
                        otherPlayer.loser();
                    }
                }
                else
                    if (lifeHudPlayer2.getHP() <= 0){
                        if (myID == Constant.GAMEMODE_MULTIPLAYER_HOST){
                            myPlayer.winner();
                            otherPlayer.loser();
                        }
                        else {
                            myPlayer.loser();
                            otherPlayer.winner();
                        }

                }


                mainScene.remove(controller);
                gameIn = false;
                currentScene = posBattleScene.start();
            }



            if (!myPlayer.isInvert() && otherPlayer.isInvert()) {
                if (myPlayer.getX() + myPlayer.getMainArea().left > otherPlayer.getX() + otherPlayer.getMainArea().right) {
                    otherPlayer.setInvert(false);
                    myPlayer.setInvert(true);
                }
            }
            else
                if (!otherPlayer.isInvert() && myPlayer.isInvert()){
                    if (myPlayer.getX() + myPlayer.getMainArea().right < otherPlayer.getX() + otherPlayer.getMainArea().left) {
                        otherPlayer.setInvert(true);
                        myPlayer.setInvert(false);
                    }
                }



            if ((myPlayer.getX() != xo || myPlayer.getY() != yo || myPlayer.getCurrentAction() != actiono)) {
                manager.addPacketsToWrite(new Move(myPlayer.getCurrentAction(), myPlayer.getX(), myPlayer.getY()));
                //log("Enviou pacote AAAAA " + xo + " - " + myPlayer.getX() + " / " + yo + " - " + myPlayer.getY());
            }

            xo = myPlayer.getX();
            yo = myPlayer.getY();
            actiono = myPlayer.getCurrentAction();


        }


        /*
        if (currentTime > 99)
            new MainScreen(context);
            */

        //Player.checkCollision(player1)

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (gameIn)
            super.onTouchEvent(event);

        return true;
    }
}
