package phdev.com.br.metafighter.screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;

import java.io.IOException;

import phdev.com.br.metafighter.ConnectionManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.Entity;
import phdev.com.br.metafighter.cmp.connections.packets.Action;
import phdev.com.br.metafighter.cmp.connections.packets.Packet;
import phdev.com.br.metafighter.cmp.event.Event;
import phdev.com.br.metafighter.cmp.event.animation.Fade;
import phdev.com.br.metafighter.cmp.event.animation.Flash;
import phdev.com.br.metafighter.cmp.event.animation.Selected;
import phdev.com.br.metafighter.cmp.event.listeners.ActionListener;
import phdev.com.br.metafighter.cmp.event.listeners.AutoDestroyableListener;
import phdev.com.br.metafighter.cmp.game.Character;
import phdev.com.br.metafighter.cmp.game.GameLabel;
import phdev.com.br.metafighter.cmp.game.Player;
import phdev.com.br.metafighter.cmp.game.PlayerAction;
import phdev.com.br.metafighter.cmp.graphics.Sprite;
import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.cmp.misc.Constant;
import phdev.com.br.metafighter.cmp.misc.GameContext;
import phdev.com.br.metafighter.cmp.misc.Timer;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.Button;
import phdev.com.br.metafighter.cmp.window.Popup;
import phdev.com.br.metafighter.cmp.window.Scene;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.window.Text;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MultiplayerSelectCharacterScreen extends Screen {

    private final int SELECT = 0;
    private final int SELECTED = 1;

    private Scene mainScene;

    private ConnectionManager manager;
    private final int myID;
    private final int otherID;

    private GameLabel gameLabelLuiz;
    private GameLabel gameLabelQuele;
    private GameLabel gameLabelRomulo;
    private GameLabel gameLabelPatricia;
    private GameLabel gameLabelKaila;
    private GameLabel gameLabelGuedes;

    private Character[] characters;

    private Sprite[] spriteViewKaila;
    private Sprite[] spriteViewGuedes;
    private Sprite[] spriteViewQuele;
    private Sprite[] spriteViewRomulo;
    private Sprite[] spriteViewPatricia;
    private Sprite[] spriteViewLuiz;

    private PlayerAction[] charactersMoveAction;
    private PlayerAction[] charactersVictoryAction;

    /*
    private PlayerAction kailaMoveAction;
    private PlayerAction guedesMoveAction;
    private PlayerAction queleMoveAction;
    private PlayerAction romuloMoveAction;
    private PlayerAction patriciaMoveAction;
    private PlayerAction luizMoveAction;
    */

    private PlayerAction currentPlayerAction1;
    private PlayerAction currentPlayerAction2;

    private Sprite[] spriteActionKaila;
    private Sprite[] spriteActionGuedes;
    private Sprite[] spriteActionQuele;
    private Sprite[] spriteActionRomulo;
    private Sprite[] spriteActionPatricia;
    private Sprite[] spriteActionLuiz;

    private BackGround mainBackground;

    private Texture backButtonTexture;
    private Texture gameLabelPlayerTexture;
    private Texture gameLabelCharTexture;
    private Texture mainBackgroundTexture;

    private GameLabel gameLabelPlayer1;
    private GameLabel gameLabelPlayer2;

    private Selected selectedPlayer1;
    private Selected selectedPlayer2;

    private Player player1;
    private Player player2;

    private Player currentPlayer;

    private int currentSelect = -1;


    private Flash flash;
    private int flashDuration = 1;

    final DisableFlashHandler handler = new DisableFlashHandler();

    private Button backButton;

    public MultiplayerSelectCharacterScreen(GameContext context, int myID) {
        super(context);

        context.getConnectionType().init();
        manager = context.getConnectionType();

        if (myID == Constant.GAMEMODE_MULTIPLAYER_HOST) {
            this.myID = myID;
            otherID = Constant.GAMEMODE_MULTIPLAYER_JOIN;
        }
        else {
            this.myID = myID;
            otherID = Constant.GAMEMODE_MULTIPLAYER_HOST;
        }

        init();

    }

    @Override
    protected boolean loadTextures() {

        backButtonTexture = new Texture("images/buttons/3.png");

        log("HERE 1");

        gameLabelPlayerTexture = new Texture("images/labels/label3.png");
        gameLabelCharTexture = new Texture("images/labels/label4.png");

        log("HERE 2");

        mainBackgroundTexture = new Texture("images/backgrounds/2.png");

        spriteViewKaila = Sprite.getSpriteFromTexture(new Texture("images/characters/kaila/view.png"), 1, 1);
        spriteViewGuedes = Sprite.getSpriteFromTexture(new Texture("images/characters/guedes/view.png"), 1, 1);
        spriteViewPatricia = Sprite.getSpriteFromTexture(new Texture("images/characters/patricia/view.png"), 1, 1);
        spriteViewLuiz = Sprite.getSpriteFromTexture(new Texture("images/characters/luiz/view.png"), 1, 1);
        spriteViewQuele = Sprite.getSpriteFromTexture(new Texture("images/characters/quele/view.png"), 1, 1);
        spriteViewRomulo = Sprite.getSpriteFromTexture(new Texture("images/characters/romulo/view.png"), 1, 1);

        spriteActionKaila = loadTextureChar("kaila");
        spriteActionGuedes = loadTextureChar("guedes");
        spriteActionQuele = loadTextureChar("quele");
        spriteActionRomulo = loadTextureChar("romulo");
        spriteActionPatricia = loadTextureChar("patricia");
        spriteActionLuiz = loadTextureChar("luiz");

        log("HERE 3");

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

        characters = new Character[6];

        selectedPlayer1 = new Selected(Color.BLUE);
        selectedPlayer2 = new Selected(Color.RED);

        characters[Character.KAILA] = new Character(null, spriteViewKaila, "Kaila");
        characters[Character.GUEDES] = new Character(null, spriteViewGuedes, "Carlos Guedes");
        characters[Character.LUIZ] = new Character(null, spriteViewLuiz, "Luiz Silva");
        characters[Character.PATRICIA] = new Character(null, spriteViewPatricia, "Patricia");
        characters[Character.QUELE] = new Character(null, spriteViewQuele, "Quele");
        characters[Character.ROMULO] = new Character(null, spriteViewRomulo, "Romulo");

        charactersMoveAction = new PlayerAction[6];
        charactersVictoryAction = new PlayerAction[6];

        RectF buttonSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);
        float fontSize = Text.adaptText(new String[]{"Voltar"}, buttonSize);
        this.backButton = new Button(
                new RectF(screenSize.centerX() - buttonSize.width()/2,
                        screenSize.bottom - buttonSize.height(),
                        screenSize.centerX() + buttonSize.width()/2,
                        screenSize.bottom),
                "Voltar", backButtonTexture);
        this.backButton.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                manager.getBluetoothManager().stop();
                new MainScreen(context);
            }
        });
        this.backButton.getText().setTextSize(fontSize);

        this.mainBackground = new BackGround(screenSize, mainBackgroundTexture);

        RectF gameLabelPlayerArea = new RectF(0,0, (screenSize.width()/16)*3, (screenSize.height()/8)*4);

        float marginX = (screenSize.width()/16)/2;
        float marginY = (screenSize.height()/8)/2;

        RectF gameLabelCharArea = new RectF(0,0,marginX*4, marginY*4);

        this.gameLabelPlayer1 = new GameLabel(
                new RectF( marginX*2,
                        marginY*6,
                        marginX*2 + gameLabelPlayerArea.width(),
                        marginY*6 + gameLabelPlayerArea.height()),
                null, null);
        this.gameLabelPlayer1.addText("Jogador 1",
                new RectF( marginX*2, marginY*4, gameLabelPlayer1.getArea().right, gameLabelPlayer1.getArea().top),
                20, Color.WHITE);


        this.gameLabelPlayer2 = new GameLabel(
                new RectF( screenSize.right - gameLabelPlayerArea.width() - marginX*2,
                        marginY*6,
                        screenSize.right - marginX*2,
                        marginY*6 + gameLabelPlayerArea.height()),
                null, null);
        this.gameLabelPlayer2.addText("Jogador 2",
                new RectF( gameLabelPlayer2.getArea().left, marginY*4, gameLabelPlayer2.getArea().right, gameLabelPlayer2.getArea().top),
                20, Color.WHITE);

        RectF size = gameLabelPlayer1.getArea();

        charactersMoveAction[Character.KAILA] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionKaila, 0, 7, false), 10, -1, false);
        charactersVictoryAction[Character.KAILA] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionKaila, 8, 22, false), 6, -1, false);
        for (Sprite aTmpSpriteAction : spriteActionKaila)
            aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
        charactersMoveAction[Character.GUEDES] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionGuedes, 0, 7, false), 10, -1, false);
        charactersVictoryAction[Character.GUEDES] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionKaila, 8, 22, false), 6, -1, false);
        for (Sprite aTmpSpriteAction : spriteActionGuedes)
            aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
        charactersMoveAction[Character.LUIZ] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionLuiz, 0, 7, false), 10, -1, false);
        charactersVictoryAction[Character.LUIZ] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionKaila, 8, 22, false), 6, -1, false);
        for (Sprite aTmpSpriteAction : spriteActionLuiz)
            aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
        charactersMoveAction[Character.PATRICIA] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionPatricia, 0, 7, false), 10, -1, false);
        charactersVictoryAction[Character.PATRICIA] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionKaila, 8, 22, false), 6, -1, false);
        for (Sprite aTmpSpriteAction : spriteActionPatricia)
            aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
        charactersMoveAction[Character.QUELE] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionQuele, 0, 7, false), 10, -1, false);
        charactersVictoryAction[Character.QUELE] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionKaila, 8, 22, false), 6, -1, false);
        for (Sprite aTmpSpriteAction : spriteActionQuele)
            aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());
        charactersMoveAction[Character.ROMULO] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionRomulo, 0, 7, false), 10, -1, false);
        charactersVictoryAction[Character.ROMULO] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionKaila, 8, 22, false), 6, -1, false);
        for (Sprite aTmpSpriteAction : spriteActionRomulo)
            aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());

        //HandlerChoiceCharacter handler = new HandlerChoiceCharacter();

        gameLabelLuiz = new GameLabel(
                new RectF( gameLabelPlayer1.getArea().right + marginX,
                        marginY,
                        gameLabelPlayer1.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null);
        gameLabelLuiz.setSprites(characters[Character.LUIZ].getView());
        gameLabelLuiz.setId(Character.LUIZ);
        //gameLabelLuiz.addEventListener(handler);
        gameLabelLuiz.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                //changeLabelPlayer(characters[Character.LUIZ]);
                //choseCharacter(gameLabelLuiz, Character.LUIZ, handler);
                selectPlayer(Character.LUIZ, myID);
            }
        });

        gameLabelQuele = new GameLabel(
                new RectF( gameLabelLuiz.getArea().right + marginX,
                        marginY,
                        gameLabelLuiz.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null);
        gameLabelQuele.setSprites(characters[Character.QUELE].getView());
        gameLabelQuele.setId(Character.QUELE);
        //gameLabelQuele.addEventListener(handler);
        gameLabelQuele.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                //changeLabelPlayer(characters[Character.QUELE]);
                //choseCharacter(gameLabelQuele, Character.QUELE, handler);
                selectPlayer(Character.QUELE, myID);
            }
        });

        gameLabelRomulo = new GameLabel(
                new RectF( gameLabelQuele.getArea().right + marginX,
                        marginY,
                        gameLabelQuele.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null);
        gameLabelRomulo.setSprites(characters[Character.ROMULO].getView());
        gameLabelRomulo.setId(Character.ROMULO);
        //gameLabelRomulo.addEventListener(handler);
        gameLabelRomulo.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                //changeLabelPlayer(characters[Character.ROMULO]);
                //choseCharacter(gameLabelRomulo, Character.ROMULO, handler);
                selectPlayer(Character.ROMULO, myID);
            }
        });

        gameLabelPatricia = new GameLabel(
                new RectF( gameLabelPlayer1.getArea().right + marginX,
                        marginY*2 + gameLabelCharArea.height(),
                        gameLabelPlayer1.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY*2 + gameLabelCharArea.height() + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null);
        gameLabelPatricia.setSprites(characters[Character.PATRICIA].getView());
        gameLabelPatricia.setId(Character.PATRICIA);
        //gameLabelPatricia.addEventListener(handler);
        gameLabelPatricia.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                //changeLabelPlayer(characters[Character.PATRICIA]);
                //choseCharacter(gameLabelPatricia, Character.PATRICIA, handler);
                selectPlayer(Character.PATRICIA, myID);
            }
        });

        gameLabelKaila = new GameLabel(
                new RectF( gameLabelPatricia.getArea().right + marginX,
                        marginY*2 + gameLabelCharArea.height(),
                        gameLabelPatricia.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY*2 + gameLabelCharArea.height() + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null);
        gameLabelKaila.setSprites(characters[Character.KAILA].getView());
        gameLabelKaila.setId(Character.KAILA);
        //gameLabelKaila.addEventListener(handler);
        gameLabelKaila.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                //changeLabelPlayer(characters[Character.KAILA]);
                //choseCharacter(gameLabelKaila, Character.KAILA, handler);
                selectPlayer(Character.KAILA, myID);
            }
        });

        gameLabelGuedes = new GameLabel(
                new RectF( gameLabelKaila.getArea().right + marginX,
                        marginY*2 + gameLabelCharArea.height(),
                        gameLabelKaila.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY*2 + gameLabelCharArea.height() + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null);
        gameLabelGuedes.setSprites(characters[Character.GUEDES].getView());
        gameLabelGuedes.setId(Character.GUEDES);
        //gameLabelGuedes.addEventListener(handler);
        gameLabelGuedes.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {

                //changeLabelPlayer(characters[Character.GUEDES]);
                //choseCharacter(gameLabelGuedes, Character.GUEDES, handler);
                selectPlayer(Character.GUEDES, myID);
            }
        });

        mainScene = new Scene() {

            final int READY_FIRST = 0;
            final int ME_TOO = 1;

            private Timer timer;
            private boolean waitingPlayers = true;
            private boolean allReady = false;
            private Popup message;

            private Thread waitingThread;

            private Matrix matrix;

            @Override
            public void init() {
                super.add(mainBackground);
                super.add(gameLabelPlayer1);
                super.add(gameLabelPlayer2);
                super.add(gameLabelGuedes);
                super.add(gameLabelKaila);
                super.add(gameLabelLuiz);
                super.add(gameLabelPatricia);
                super.add(gameLabelRomulo);
                super.add(gameLabelQuele);
                super.add(backButton);

                matrix = new Matrix();
                matrix.postScale(-1,1);

                message = new Popup("Esperando outros jogadores", new AutoDestroyableListener() {
                    @Override
                    public void autoDestroy(Object entity) {}
                }, 1000);

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

            }

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

                    if (selectedPlayer1 != null)
                        if (selectedPlayer1.isActive())
                            selectedPlayer1.update();

                    if (selectedPlayer2 != null)
                        if (selectedPlayer2.isActive())
                            selectedPlayer2.update();

                    if (flash != null)
                        flash.update();

                    if ((player1 != null && player2 != null)){
                        if (flash == null)
                            //new MatchScreen(context, null, player1.getCharID(), player2.getCharID());
                            //new MatchScreen(context, null, Character.GUEDES, Character.GUEDES);
                            new MultiplayerMatchScreen(context, myID, Character.GUEDES, Character.GUEDES);
                    }
                }
            }

            @Override
            public void draw(Canvas canvas){

                if (waitingPlayers)
                    message.draw(canvas);
                else {
                    super.draw(canvas);

                    if (selectedPlayer1 != null)
                        if (selectedPlayer1.isActive())
                            selectedPlayer1.draw(canvas);

                    if (selectedPlayer2 != null)
                        if (selectedPlayer2.isActive())
                            selectedPlayer2.draw(canvas);

                    if (flash != null)
                        flash.draw(canvas);

                    if (currentPlayerAction1 != null) {
                        Sprite tmpSprite;
                        if (player1 == null)
                            tmpSprite = currentPlayerAction1.getSprite();
                        else {
                            tmpSprite = currentPlayerAction1.getSprite();
                            if (tmpSprite == null)
                                tmpSprite = currentPlayerAction1.getLastSprite();
                        }

                        if (tmpSprite == null) {
                            currentPlayerAction1.execute();
                            tmpSprite = currentPlayerAction1.getSprite();
                        }
                        if (tmpSprite != null)
                            canvas.drawBitmap(tmpSprite.getTexture().getImage(), (int) gameLabelPlayer1.getArea().left, (int) gameLabelPlayer1.getArea().top, gameLabelPlayer1.getPaint());
                    }
                    if (currentPlayerAction2 != null) {
                        Sprite tmpSprite;
                        if (player2 == null)
                            tmpSprite = currentPlayerAction2.getSprite();
                        else {
                            tmpSprite = currentPlayerAction2.getSprite();
                            if (tmpSprite == null)
                                tmpSprite = currentPlayerAction2.getLastSprite();
                        }

                        if (tmpSprite == null) {
                            currentPlayerAction2.execute();
                            tmpSprite = currentPlayerAction2.getSprite();
                        }
                        if (tmpSprite != null) {
                            int savedCount = canvas.save();
                            canvas.setMatrix(matrix);
                            canvas.translate(-GameParameters.getInstance().screenSize.width(), 0);

                            int x = (int)GameParameters.getInstance().screenSize.width() - (int)gameLabelPlayer2.getArea().left - (int)gameLabelPlayer2.getArea().width();

                            canvas.drawBitmap(tmpSprite.getTexture().getImage(), x, (int) gameLabelPlayer2.getArea().top, gameLabelPlayer2.getPaint());

                            canvas.restoreToCount(savedCount);
                        }
                    }
                }
            }

            @Override
            public void  processPacket(Packet packet){

                if (waitingPlayers)
                    super.processPacket(packet);
                else
                    if (packet != null){

                        Action action = null;

                        if (currentScene != null)
                            action = (Action)packet;

                        if (action != null){

                            if (action.getValue3() == SELECT){
                                changeLabelPlayer(action.getValue2(), action.getValue1());
                                selectPlayer(action.getValue2(), action.getValue1());
                            }

                        }
                    }
            }

        };

        currentScene = mainScene.start();

        return true;
    }

    private void selectPlayer(int charID, int playerID){

        switch (charID){
            case Character.GUEDES:
                changeLabelPlayer(Character.GUEDES, playerID);
                choseCharacter(gameLabelGuedes, Character.GUEDES, playerID);
                //manager.addPacketsToWrite(new Action(myID, Character.GUEDES, SELECT, -1, -1));
                break;
            case Character.KAILA:
                changeLabelPlayer(Character.KAILA, playerID);
                choseCharacter(gameLabelKaila, Character.KAILA, playerID);
                //manager.addPacketsToWrite(new Action(myID, Character.KAILA, SELECT, -1, -1));
                break;
            case Character.LUIZ:
                changeLabelPlayer(Character.LUIZ, playerID);
                choseCharacter(gameLabelLuiz, Character.LUIZ, playerID);
                //manager.addPacketsToWrite(new Action(myID, Character.LUIZ, SELECT, -1, -1));
                break;
            case Character.PATRICIA:
                changeLabelPlayer(Character.PATRICIA, playerID);
                choseCharacter(gameLabelPatricia, Character.PATRICIA, playerID);
                //manager.addPacketsToWrite(new Action(myID, Character.PATRICIA, SELECT, -1, -1));
                break;
            case Character.ROMULO:
                changeLabelPlayer(Character.ROMULO, playerID);
                choseCharacter(gameLabelRomulo, Character.ROMULO, playerID);
                //manager.addPacketsToWrite(new Action(myID, Character.ROMULO, SELECT, -1, -1));
                break;
            case Character.QUELE:
                changeLabelPlayer(Character.QUELE, playerID);
                choseCharacter(gameLabelQuele, Character.QUELE, playerID);
                //manager.addPacketsToWrite(new Action(myID, Character.QUELE, SELECT, -1, -1));
                break;
            case Character.TESTE:
                changeLabelPlayer(Character.TESTE, playerID);
                choseCharacter(gameLabelGuedes, Character.TESTE, playerID);
                //manager.addPacketsToWrite(new Action(myID, Character.TESTE, SELECT, -1, -1));
                break;
        }

        if (myID == playerID)
            manager.addPacketsToWrite(new Action(myID, charID, SELECT, -1));

    }

    private void changeLabelPlayer(int charID, int playerID){

        log("Executou");

        if (playerID == Constant.GAMEMODE_MULTIPLAYER_HOST)
            if (player1 == null) {
                gameLabelPlayer1.getText().setText(characters[charID].getName());
                gameLabelPlayer1.setSprites(characters[charID].getSprites());
                currentPlayerAction1 = charactersMoveAction[charID];
            }

        if (playerID == Constant.GAMEMODE_MULTIPLAYER_JOIN)
            if (player2 == null) {
                gameLabelPlayer2.getText().setText(characters[charID].getName());
                gameLabelPlayer2.setSprites(characters[charID].getSprites());
                currentPlayerAction2 = charactersMoveAction[charID];
            }
    }

    private void choseCharacter(Entity entity, int charID, int playerID) {
        Selected selected = null;

        if (player1 == null)
            if (playerID == Constant.GAMEMODE_MULTIPLAYER_HOST)
                selected = selectedPlayer1;

        if (player2 == null)
            if (playerID == Constant.GAMEMODE_MULTIPLAYER_JOIN)
                selected = selectedPlayer2;

        if (selected != null) {

            selected.setEntity(entity);

            if (currentSelect == charID) {
                flash = new Flash(new Fade(51, 0), entity, flashDuration, handler);
                selected.setEstatic();
                currentSelect = -1;


                if (selected.equals(selectedPlayer1)) {
                    currentPlayerAction1 = charactersVictoryAction[charID];
                    player1 = new Player();
                    player1.setName(characters[charID].getName());
                    player1.setCharID(charID);
                }
                if (selected.equals(selectedPlayer2)) {
                    currentPlayerAction2 = charactersVictoryAction[charID];
                    player2 = new Player();
                    player2.setName(characters[charID].getName());
                    player2.setCharID(charID);
                }

                if (selected.equals(selectedPlayer1))
                    log("Player 1 selecionado");
                else
                    log("Player 2 selecionado");


            } else{
                currentSelect = charID;
            }
        }

    }

    private Sprite[] loadTextureChar(String name){

        RectF screenSize = GameParameters.getInstance().screenSize;

        Sprite[] tmpSpriteAction = null;

        try {
            //String paths[] = GameParameters.getInstance().assetManager.list("images/characters/" + name + "/action");
            String paths[] = new String[]{
                    "1gingando",
                    "8win"
            };

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

    private class DisableFlashHandler implements AutoDestroyableListener{

        @Override
        public void autoDestroy(Object entity) {
            flash = null;
        }
    }
}
