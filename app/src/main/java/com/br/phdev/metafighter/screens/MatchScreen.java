package com.br.phdev.metafighter.screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import com.br.phdev.metafighter.ConnectionManager;
import com.br.phdev.metafighter.GameParameters;
import phdev.com.br.metafighter.R;
import com.br.phdev.metafighter.SoundManager;
import com.br.phdev.metafighter.cmp.connections.packets.Action;
import com.br.phdev.metafighter.cmp.connections.packets.Damage;
import com.br.phdev.metafighter.cmp.connections.packets.Move;
import com.br.phdev.metafighter.cmp.connections.packets.Packet;
import com.br.phdev.metafighter.cmp.connections.packets.Request;
import com.br.phdev.metafighter.cmp.event.listeners.AutoDestroyableListener;
import com.br.phdev.metafighter.cmp.game.Character;
import com.br.phdev.metafighter.cmp.game.LifeHud;
import com.br.phdev.metafighter.cmp.game.Player;
import com.br.phdev.metafighter.cmp.graphics.Sprite;
import com.br.phdev.metafighter.cmp.graphics.Texture;
import com.br.phdev.metafighter.cmp.misc.Constant;
import com.br.phdev.metafighter.cmp.misc.Controller;
import com.br.phdev.metafighter.cmp.misc.GameContext;
import com.br.phdev.metafighter.cmp.misc.Timer;
import com.br.phdev.metafighter.cmp.window.BackGround;
import com.br.phdev.metafighter.cmp.window.Popup;
import com.br.phdev.metafighter.cmp.window.Scene;
import com.br.phdev.metafighter.cmp.window.Screen;
import com.br.phdev.metafighter.cmp.window.Text;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MatchScreen extends Screen {

    private Scene mainSceneSinglePlayer;
    private Scene mainSceneMultiPlayer;
    private Scene preBattleMultiPlayerScene;
    private Scene posBattleScene;

    private BackGround backGround;
    private BackGround preBattleBackground;

    private SoundManager soundManager;

    private LifeHud lifeHudPlayer1;
    private LifeHud lifeHudPlayer2;

    private Texture backgroundTexture;
    private Texture preBattleBackgroundTexture;
    private Texture lifeHudTexture;
    private Texture controllerDirTexture;
    private Texture controllerButTexture;

    private Player drawTopPlayer;
    private Player drawBotPlayer;
    private Player player1;
    private Player player2;

    private Paint hitPaint;
    private Texture hitTexture;
    private LinkedList<Hit> hits;

    private int charIDplayer1;
    private int charIDplayer2;

    private boolean gameIn;

    private Controller controller;
    private Controller bot;

    private int gameMode;

    // PARA TESTES

    private Random randBot = new Random();
    private boolean executingSomething;
    private Timer taskBot = new Timer();
    private int duration = 0;

    private boolean botMove;

    //

    private int[] sounds;
    private Random randSound;

    public MatchScreen(GameContext context, int gameMode, int charIDplayer1, int charIDplayer2) {
        super(context);

        this.gameMode = gameMode;

        soundManager = context.getSoundManager();

        hits = new LinkedList<>();
        hitPaint = new Paint();

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

        lifeHudTexture = new Texture("images/labels/label5.png");
        controllerDirTexture = new Texture("cmp/controller/directionalBase.png");
        controllerButTexture = new Texture("cmp/controller/ActionBase.png");

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
        final float divx = screenSize.width()/32;
        final float divy = screenSize.height()/16;

        hitTexture.scaleImage((int)divx, (int)divy);

        backGround = new BackGround(screenSize, backgroundTexture);

        //RectF player1Area = new RectF(screenSize.centerX() - divx*7, screenSize.bottom - divy*10, screenSize.centerX() + divx*1, screenSize.bottom);

        RectF playerArea = new RectF(0,0, divx*7, divy*10);

        //RectF player1Area = new RectF(screenSize.centerX() - divx*7, screenSize.bottom - divy*10, screenSize.centerX() + divx*1, screenSize.bottom);


        //Carregamento dos personagens dos jogadores

        try{
            player1 = loadPlayer(charIDplayer1,
                    new RectF(screenSize.centerX() - playerArea.width(), screenSize.bottom - playerArea.height(), screenSize.centerX(), screenSize.bottom), false);
            player1.setDirectionX(1);
            log("Carregou o jogador 1");

            player2 = loadPlayer(charIDplayer2,
                    new RectF(screenSize.centerX() - playerArea.width(), screenSize.bottom - playerArea.height(), screenSize.centerX(), screenSize.bottom), true);
            player2.setDirectionX(-1);
            log("Carregou o jogador 2");

        }
        catch (Exception e){
            log(e.getMessage());
        }

        //player1 = loadPlayer(charIDplayer1, player1Area, false);
        //player2 = loadPlayer(charIDplayer2, player2Area, true);

        RectF lifeHudArea = new RectF(0,0,divx*13, divy);

        lifeHudPlayer1 = new LifeHud( new RectF(divx, divy, divx + lifeHudArea.width(), divy + lifeHudArea.height()), lifeHudTexture, player1.getName());
        lifeHudPlayer1.getText().setHorizontalAlignment(Text.LEFT);
        lifeHudPlayer1.getText().getPaint().setColor(Color.BLUE);

        lifeHudPlayer2 = new LifeHud( new RectF(screenSize.right - divx - lifeHudArea.width(),
                divy, screenSize.right - divx, divy + lifeHudArea.height()), lifeHudTexture, player2.getName());
        lifeHudPlayer2.getText().setHorizontalAlignment(Text.RIGHT);
        lifeHudPlayer2.getText().getPaint().setColor(Color.BLUE);

        player1.setLifeHud(lifeHudPlayer1);
        player2.setLifeHud(lifeHudPlayer2);


        if (gameMode == Constant.GAMEMODE_SINGLEPLAYER)
            mainSceneSinglePlayer = new Scene(){

            private Scene preBattleSinglePlayerScene;

            @Override
            public void init() {

                preBattleSinglePlayerScene = new Scene(){

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
                            currentScene = mainSceneSinglePlayer.start();
                            gameIn = true;
                            preBattleSinglePlayerScene = null;
                        }

                    }
                };

                RectF areaController = new RectF(0,0, 170, 170);
                controller = new Controller(
                        new RectF(10, screenSize.bottom - 10 - areaController.height(), 10 + areaController.width(), screenSize.bottom-10 ) , controllerDirTexture,
                        new RectF(screenSize.right - 10 - areaController.width(), screenSize.bottom - 10 - areaController.height(),
                                screenSize.right - 10, screenSize.bottom-10), controllerButTexture, player1.getControllerListener());

                bot = new Controller(player2.getControllerListener());

                player1.setLifeHud(lifeHudPlayer1);
                player2.setLifeHud(lifeHudPlayer2);

                drawTopPlayer = player1;
                drawBotPlayer = player2;

                super.add(backGround);
                super.add(lifeHudPlayer1);
                super.add(lifeHudPlayer2);
                super.add(drawBotPlayer);
                super.add(drawTopPlayer);
                super.add(controller);

                currentScene = preBattleSinglePlayerScene.start();

            }

            @Override
            public Scene start(){
                super.start();
                soundManager.playMusic(R.raw.music2);
                return this;
            }

            @Override
            public void update(){
                super.update();

                if (gameIn) {
                    if (checkCollision(player1, player2)) {

                        switch (player1.getCurrentAction()){
                            case Player.KICK1_ACTION:

                                if (player1.isActionPerformed()) {

                                    drawTopPlayer = player1;
                                    drawBotPlayer = player2;

                                    player2.damaged(5f);
                                    soundManager.playSound(sounds[randSound.nextInt(4)]);
                                }
                                break;
                            case Player.PUNCH1_ACTION:
                                if (player1.isActionPerformed()) {

                                    drawTopPlayer = player1;
                                    drawBotPlayer = player2;

                                    player2.damaged(2f);
                                    soundManager.playSound(sounds[randSound.nextInt(4)]);
                                }
                                break;
                            case Player.WALKING_LEFT_ACTION:
                                if (!player1.isInvert())
                                    player1.setStop(false);
                                else
                                    player1.setStop(true);
                                break;
                            case Player.WALKING_RIGHT_ACTION:
                                if (!player1.isInvert())
                                    player1.setStop(true);
                                else
                                    player1.setStop(false);
                                break;
                        }

                    }
                    else {
                        player1.setStop(false);
                    }


                    if (lifeHudPlayer1.getHP() <=0 || lifeHudPlayer2.getHP() <= 0) {

                        bot = null;
                        controller = null;

                        if (lifeHudPlayer1.getHP() <=0){
                            player2.winner();
                            player1.loser();
                        }
                        else
                        if (lifeHudPlayer2.getHP() <= 0){
                            player2.loser();
                            player1.winner();
                        }


                        mainSceneSinglePlayer.remove(controller);
                        gameIn = false;
                        currentScene = posBattleScene.start();
                    }



                    if (!player1.isInvert() && player2.isInvert()) {
                        if (player1.getX() + player1.getMainArea().left > player2.getX() + player2.getMainArea().right) {
                            player2.setInvert(false);
                            player1.setInvert(true);
                        }
                        else
                        if (player1.getX() < 0)
                            player1.setX(0);
                    }
                    else
                    if (!player2.isInvert() && player1.isInvert()){
                        if (player1.getX() + player1.getMainArea().right < player2.getX() + player2.getMainArea().left) {
                            player2.setInvert(true);
                            player1.setInvert(false);
                        }
                    }

                    // PARA TESTES COM O BOT

            /*

            if (player1.getCurrentAction() == Player.KICK_ACTION || player1.getCurrentAction() == Player.PUNCH_ACTION) {
                bot.fireAction(Controller.ACTION_3_PRESSED);
                executingSomething = true;
            }
            else {
                bot.fireAction(Controller.ACTION_3_RELEASED);
                executingSomething = false;
            }

            if (!executingSomething) {
                if (randBot.nextInt(400) < 5) {
                    executingSomething = true;
                    botMove = true;
                    taskBot.start();
                    duration = randBot.nextInt(5);
                }
            }

            if (botMove){
                if (!player1.isInvert())
                    bot.fireAction(Controller.ARROW_LEFT_PRESSED);
                else
                    bot.fireAction(Controller.ARROW_RIGHT_PRESSED);

                if (taskBot.getTicks()/100000000 > duration){
                    botMove = false;
                    executingSomething = false;
                    bot.fireAction(Controller.ARROW_LEFT_RELEASED);
                    bot.fireAction(Controller.ARROW_RIGHT_RELEASED);
                    taskBot.stop();
                }

            }

            */



                    //



                    if (!player1.isInvert()){
                        if (player1.getX() > GameParameters.getInstance().screenSize.width())
                            player1.setX(GameParameters.getInstance().screenSize.width());
                    }
                    else {
                        if (player1.getX() + player1.getMainArea().width() > GameParameters.getInstance().screenSize.width())
                            player1.setX(GameParameters.getInstance().screenSize.width() - player1.getMainArea().width());
                    }

                }

            }

            @Override
            public boolean onTouchEvent(MotionEvent event){
                super.onTouchEvent(event);
                return true;
            }

        };
        else
            mainSceneMultiPlayer = new Scene() {

            private static final int YOUR_HP = 0;
            private static final int MY_HP = 1;
            private static final int CHANGE_HP = 2;

            private ConnectionManager manager;

            private Scene preBattleScene;

            private int myID;

            private Player myPlayer;
            private Player otherPlayer;

            float xo;
            float yo;
            int actiono;

            private RectF otherScreenSize;
            private float factorX;
            private float factorY;


            @Override
            public void init() {

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
                                                    otherScreenSize = new RectF(0,0,action.getValue2(), action.getValue3());
                                                    manager.addPacketsToWrite(new Action(ME_TOO, (int)screenSize.width(), (int)screenSize.height(),-1));
                                                    allReady = true;
                                                    waitingThread = null;
                                                }
                                                if (action.getValue1() == ME_TOO){
                                                    otherScreenSize = new RectF(0,0,action.getValue2(), action.getValue3());
                                                    allReady = true;
                                                    waitingThread = null;
                                                }
                                            }
                                            else
                                                manager.addPacketsToWrite(new Action(READY_FIRST, (int)screenSize.width(),(int)screenSize.height(),-1));

                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        };

                        waitingThread.start();

                    }

                    @Override
                    public void update(){

                        if (waitingPlayers){
                            if (allReady){
                                waitingPlayers = false;
                                timer = new Timer();
                                timer.start();
                                message = null;
                                factorX = screenSize.width() / otherScreenSize.width();
                                factorY = screenSize.height() / otherScreenSize.height();
                            }
                        }
                        else {
                            super.update();
                            if (timer.getTicks()/1000000000 > 3){
                                currentScene = mainSceneMultiPlayer.start();
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

                myID = gameMode;

                manager = context.getConnectionType();
                manager.init();

                if (gameMode == Constant.GAMEMODE_MULTIPLAYER_HOST){
                    myPlayer = player1;
                    otherPlayer = player2;
                }
                else {
                    myPlayer = player2;
                    otherPlayer = player1;
                }

                super.add(backGround);
                super.add(lifeHudPlayer1);
                super.add(lifeHudPlayer2);
                super.add(myPlayer);
                super.add(otherPlayer);
                super.add(controller);

                currentScene = preBattleScene.start();

            }

            @Override
            public Scene start(){
                super.start();
                soundManager.playMusic(R.raw.music2);
                return this;
            }

            @Override
            public void update(){
                super.update();

                if (gameIn) {

                    if (checkCollision(myPlayer, otherPlayer)) {
                        if (myPlayer.getCurrentAction() == Player.KICK1_ACTION) {
                            manager.addPacketsToWrite(new Damage(0.5f));
                            otherPlayer.damaged(0.5f);

                            soundManager.playSound(sounds[randSound.nextInt(4)]);

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

                        mainSceneMultiPlayer.remove(controller);
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
                            float x = (((Move) packet).getX() * factorX);
                            float y = (((Move) packet).getY() * factorY);
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

        posBattleScene = new Scene() {
            private Timer timer;

            @Override
            public void init() {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAlpha(150);

                super.add(mainSceneSinglePlayer != null ? mainSceneSinglePlayer : mainSceneMultiPlayer);
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

                        new MainScreen(context);
                    }
            }
        };

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
        log("Terminou de carregar os asprites");
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
                    tmpSpriteAction[contador++] = new Sprite(new Texture("images/characters/" + name + "/action/" + paths[i] + "/" + ((j+1) + ".png"), (int)screenSize.width()/10, (int)screenSize.height()/10));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpSpriteAction;
    }

    protected boolean checkCollision(Object objA, Object objB){

        if (objA instanceof Player && objB instanceof  Player){

            Player A = (Player)objA;
            Player B = (Player)objB;

            float AX = A.getX();
            float AY = A.getY();
            float BX = B.getX();
            float BY = B.getY();

            RectF[][] colA = A.getCurrentCollision();
            RectF[][] colB = B.getCurrentCollision();

            for (int i=0; i<15; i++){
                for (int j=0; j<15; j++){
                    if (colA[i][j] != null) {
                        for (int k = 0; k < 15; k++) {
                            for (int l = 0; l < 15; l++) {
                                if (colB[k][l] != null) {
                                    if (RectF.intersects(new RectF(AX + colA[i][j].left, AY + colA[i][j].top, AX + colA[i][j].right, AY + colA[i][j].bottom),
                                            new RectF(BX + colB[k][l].left, BY + colB[k][l].top, BX + colB[k][l].right, BY + colB[k][l].bottom))){
                                        hits.add(new Hit(AX + colB[k][l].centerX(), AY + colB[k][l].centerY()));
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        Hit hit = null;
        if (hits.size() > 1)
            hit = hits.pop();
        if (hit != null)
            canvas.drawBitmap(hitTexture.getImage(), hit.x, hit.y, hitPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (gameIn)
            super.onTouchEvent(event);

        return true;
    }
}
