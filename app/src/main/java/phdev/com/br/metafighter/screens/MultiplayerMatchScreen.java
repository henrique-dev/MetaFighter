package phdev.com.br.metafighter.screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import phdev.com.br.metafighter.BluetoothManager;
import phdev.com.br.metafighter.ConnectionManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.R;
import phdev.com.br.metafighter.SoundManager;
import phdev.com.br.metafighter.cmp.connections.packets.Action;
import phdev.com.br.metafighter.cmp.connections.packets.Damage;
import phdev.com.br.metafighter.cmp.connections.packets.Move;
import phdev.com.br.metafighter.cmp.connections.packets.Packet;
import phdev.com.br.metafighter.cmp.connections.packets.Request;
import phdev.com.br.metafighter.cmp.event.listeners.AutoDestroyableListener;
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
import phdev.com.br.metafighter.cmp.window.Popup;
import phdev.com.br.metafighter.cmp.window.Scene;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.window.Text;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MultiplayerMatchScreen extends Screen {

    private static final int YOUR_HP = 0;
    private static final int MY_HP = 1;
    private static final int CHANGE_HP = 2;

    private Scene mainScene;
    private Scene preBattleScene;
    private Scene posBattleScene;

    private ConnectionManager manager;

    private SoundManager soundManager;
    //private SoundPool soundPool;
    //private MediaPlayer mediaPlayer;

    private BackGround backGround;
    private BackGround preBattleBackground;

    //private Label timerLabel;
    //private Timer matchTimer;
    //private int currentTime = 0;

    private LifeHud lifeHudPlayer1;
    private LifeHud lifeHudPlayer2;

    private Paint hitPaint;
    private Texture hitTexture;
    private LinkedList<Hit> hits;

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
    private boolean waitingPlayers = true;

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

    private int[] sounds;
    private Random randSound;

    //

    public MultiplayerMatchScreen(GameContext context, int myID, int charIDplayer1, int charIDplayer2) {
        super(context);

        context.getConnectionType().init();
        manager = context.getConnectionType();

        soundManager = context.getSoundManager();

        hits = new LinkedList<>();
        hitPaint = new Paint();

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

    private class Hit{

        public float x;
        public float y;

        public Hit(float x, float y){
            this.x = x;
            this.y = y;
        }



    }

    @Override
    protected boolean loadTextures() {

        backgroundTexture = new Texture("images/backgrounds/4.png");
        preBattleBackgroundTexture = new Texture("images/backgrounds/3.png");

        context.getProgressCmp().increase(15);

        lifeHudTexture = new Texture("images/labels/label5.png");
        controllerDirTexture = new Texture("cmp/controller/directionalBase.png");

        hitTexture = new Texture("images/animations/hit.png");

        return true;
    }

    @Override
    protected boolean loadFonts() {
        return true;
    }

    @Override
    protected boolean loadSounds() {

        sounds = new int[4];
        randSound = new Random();

        sounds[0] = soundManager.getSoundPool().load(context.getAppContetxt(), R.raw.p1, 1);
        sounds[1] = soundManager.getSoundPool().load(context.getAppContetxt(), R.raw.p2, 1);

        context.getProgressCmp().increase(65);

        sounds[2] = soundManager.getSoundPool().load(context.getAppContetxt(), R.raw.p3, 1);
        sounds[3] = soundManager.getSoundPool().load(context.getAppContetxt(), R.raw.p4, 1);

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

                hitTexture.scaleImage((int)divx, (int)divy);


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

                context.getProgressCmp().increase(80);

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


                context.getProgressCmp().increase(90);

                /*
                timerLabel = new Label(
                        new RectF(lifeHudPlayer1.getArea().right, 0, lifeHudPlayer2.getArea().left, lifeHudPlayer1.getArea().bottom + divy),
                        "99", null);
                timerLabel.getText().setTextSize( Text.adaptText(new String[]{"99"}, timerLabel.getArea()) );
                timerLabel.getText().getPaint().setColor(Color.WHITE);
                timerLabel.getPaint().setAlpha(0);
                */

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

                context.getProgressCmp().increase(95);


                super.add(backGround);
                super.add(lifeHudPlayer1);
                super.add(lifeHudPlayer2);
                //super.add(timerLabel);
                super.add(myPlayer);
                super.add(otherPlayer);
                super.add(controller);
            }

            @Override
            public Scene start(){
                super.start();
                //matchTimer = new Timer();
                //matchTimer.start();
                soundManager.playMusic(R.raw.music2);
                return this;
            }

            int myCont = 0;
            int otherCon = 0;

            @Override
            public void update(){
                super.update();

                if (gameIn) {

                    /*
                    int tempTime = (int) (matchTimer.getTicks() / 1000000000);
                    if (tempTime != currentTime) {
                        if (timerLabel != null) {
                            timerLabel.getText().setText((99 - tempTime) + "");
                            currentTime = tempTime;
                        }
                    }
                    */

                    if (checkCollision(myPlayer.getCurrentCollision(), myPlayer.getX(), myPlayer.getY(),
                            otherPlayer.getCurrentCollision(), otherPlayer.getX(), otherPlayer.getY())) {
                        if (myPlayer.getCurrentAction() == Player.KICK1_ACTION) {
                            manager.addPacketsToWrite(new Damage(0.5f));
                            otherPlayer.damaged(0.5f);

                            soundManager.playSound(sounds[randSound.nextInt(4)]);


                            //if (myID == Constant.GAMEMODE_MULTIPLAYER_HOST)
                            manager.addPacketsToWrite(new Request(YOUR_HP, -1, -1, -1));
                        }
                        else
                            if (myPlayer.getCurrentAction() == Player.PUNCH1_ACTION) {
                                manager.addPacketsToWrite(new Damage(0.3f));
                                otherPlayer.damaged(0.3f);

                                soundManager.playSound(sounds[randSound.nextInt(4)]);

                                manager.addPacketsToWrite(new Request(YOUR_HP, myPlayer.getLifeHud().getHP(), -1, -1));
                            }
                            else
                                hits.pop();

                    }


                    if (lifeHudPlayer1.getHP() <= 0 || lifeHudPlayer2.getHP() <= 0) {
                        //matchTimer.pause();

                        bot = null;
                        controller = null;

                        if (lifeHudPlayer1.getHP() <= 0) {
                            if (myID == Constant.GAMEMODE_MULTIPLAYER_HOST) {
                                myPlayer.loser();
                                otherPlayer.winner();
                            } else {
                                myPlayer.winner();
                                otherPlayer.loser();
                            }
                        } else if (lifeHudPlayer2.getHP() <= 0) {
                            if (myID == Constant.GAMEMODE_MULTIPLAYER_HOST) {
                                myPlayer.winner();
                                otherPlayer.loser();
                            } else {
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
                    } else if (!otherPlayer.isInvert() && myPlayer.isInvert()) {
                        if (myPlayer.getX() + myPlayer.getMainArea().right < otherPlayer.getX() + otherPlayer.getMainArea().left) {
                            otherPlayer.setInvert(true);
                            myPlayer.setInvert(false);
                        }
                    }


                    if ((myPlayer.getX() != xo || myPlayer.getY() != yo || myPlayer.getCurrentAction() != actiono)) {
                        manager.addPacketsToWrite(new Move(myPlayer.getCurrentAction(), myPlayer.getX(), myPlayer.getY()));
                        //log("Enviou pacote AAAAA " + xo + " - " + myPlayer.getX() + " / " + yo + " - " + myPlayer.getY());
                    }

                    if (!myPlayer.isInvert()){
                        if (myPlayer.getX() > GameParameters.getInstance().screenSize.width())
                            myPlayer.setX(GameParameters.getInstance().screenSize.width());
                        else
                            if (myPlayer.getX() < 0)
                                myPlayer.setX(0);
                    }
                    else {
                        if (myPlayer.getX() + myPlayer.getMainArea().width() > GameParameters.getInstance().screenSize.width())
                            myPlayer.setX(GameParameters.getInstance().screenSize.width() - myPlayer.getMainArea().width());
                        log("Here");
                    }


                    xo = myPlayer.getX();
                    yo = myPlayer.getY();
                    actiono = myPlayer.getCurrentAction();
                }

            }

            @Override
            public void draw(Canvas canvas){
                super.draw(canvas);

                Hit hit = null;
                if (hits.size() > 1)
                    hit = hits.pop();
                if (hit != null)
                    canvas.drawBitmap(hitTexture.getImage(), otherPlayer.getX() + hit.x, otherPlayer.getY() + hit.y, hitPaint);

            }

            @Override
            public void processPacket(Packet packet){
                if (packet != null) {

                    if (currentScene != null) {
                        if (packet instanceof Move) {
                            int action = ((Move) packet).getValue1();
                            float x = ((Move) packet).getX();
                            float y = ((Move) packet).getY();
                            otherPlayer.setCurrentPlayerAction(action);
                            otherPlayer.setX(x);
                            otherPlayer.setY(y);
                        }
                        else {
                            if (packet instanceof Damage) {
                                float damage = ((Damage) packet).getDamage();
                                myPlayer.damaged(damage);
                            } else {
                                if (packet instanceof Request) {

                                    switch (((Request) packet).getRequest()) {
                                        case YOUR_HP:
                                            manager.addPacketsToWrite(new Request(MY_HP, myPlayer.getLifeHud().getHP(),
                                                    otherPlayer.getLifeHud().getHP(), -1));
                                            break;
                                        case MY_HP:
                                            float myHP = myPlayer.getLifeHud().getHP();
                                            float myHPthere = ((Request) packet).getValue1();

                                            float otherHP = otherPlayer.getLifeHud().getHP();
                                            float otherHPthere = ((Request) packet).getValue2();

                                            if (myHP != myHPthere || otherHP != otherHPthere){
                                                myPlayer.getLifeHud().setHP(myHP);
                                                otherPlayer.getLifeHud().setHP(otherHP);
                                                manager.addPacketsToWrite(new Request(CHANGE_HP, myHP, otherHP, -1));
                                            }
                                            break;
                                        case CHANGE_HP:
                                            float myNewHP = ((Request)packet).getValue2();
                                            float otherNewHP =  ((Request)packet).getValue1();

                                            myPlayer.getLifeHud().setHP(myNewHP);
                                            otherPlayer.getLifeHud().setHP(otherNewHP);
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        };

        preBattleScene = new Scene(){

            final int READY_FIRST = 0;
            final int ME_TOO = 1;

            private Timer timer;
            private boolean waitingPlayers = true;
            private boolean allReady = false;
            private Popup message;

            private Thread waitingThread;

            @Override
            public void init() {

                preBattleBackground = new BackGround(screenSize, preBattleBackgroundTexture);

                message = new Popup("Esperando outros jogadores", new AutoDestroyableListener() {
                    @Override
                    public void autoDestroy(Object entity) {}
                }, 1000);

                super.add(new BackGround(screenSize, Color.WHITE));
                super.add(preBattleBackground);

                waitingThread = new Thread(){

                    @Override
                    public void run(){
                        Timer timer = new Timer().start();
                        int currentSecond = (int)(timer.getTicks()/100000000);
                            while (waitingPlayers){
                                int tmpSecond = (int)(timer.getTicks()/100000000);

                                if (tmpSecond > currentSecond){
                                    currentSecond = tmpSecond + 3;
                                    try{

                                        Packet packet = getCurrentPacketToRead();

                                        if (packet != null) {
                                            Action action = (Action)packet;
                                            if (action.getValue1() == READY_FIRST){
                                                manager.addPacketsToWrite(new Action(ME_TOO,-1,-1,-1));
                                                allReady = true;
                                                waitingThread = null;
                                            }
                                            if (action.getValue1() == ME_TOO){
                                                allReady = true;
                                                waitingThread = null;
                                            }
                                        }
                                        else
                                            manager.addPacketsToWrite(new Action(READY_FIRST,-1,-1,-1));

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                    }
                };

                waitingThread.start();

                //timer = new Timer();
                //timer.start();
            }

            /*
            @Override
            public void processPacket(Packet packet){
                if (!waitingPlayers)
                    super.processPacket(packet);
            }
            */

            @Override
            public void update(){

                if (waitingPlayers){
                    if (allReady){
                        waitingPlayers = false;
                        timer = new Timer();
                        timer.start();
                        message = null;
                    }
                }
                else {
                    super.update();
                    if (timer.getTicks()/1000000000 > 3){
                        currentScene = mainScene.start();
                        gameIn = true;
                        preBattleScene = null;
                    }
                }

            }

            @Override
            public void draw(Canvas canvas){
                if (waitingPlayers)
                    message.draw(canvas);
                else
                    super.draw(canvas);
            }
        };

        posBattleScene = new Scene() {

            private Timer timer;

            @Override
            public void init() {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAlpha(150);

                super.add(mainScene);
                super.add(new BackGround(screenSize, paint));
            }

            @Override
            public Scene start(){
                super.start();

                timer = new Timer().start();

                return this;
            }

            @Override
            public void update(){
                super.update();

                if (timer != null)
                    if (timer.getTicks()/1000000000 > 3) {
                        manager.getBluetoothManager().stop();
                        new MainScreen(context);
                    }
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
        return new Player(character, size, invert);
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
                    tmpSpriteAction[contador++] = new Sprite(new Texture("images/characters/" + name + "/action/" + paths[i] + "/" + ((j+1) + ".png"), (int)screenSize.width()/15, (int)screenSize.height()/15));
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

                                    hits.add(new Hit(B[k][l].centerX(), B[k][l].centerY()));

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
