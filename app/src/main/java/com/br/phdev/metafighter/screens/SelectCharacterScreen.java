package com.br.phdev.metafighter.screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.IOException;

import com.br.phdev.metafighter.ConnectionManager;
import com.br.phdev.metafighter.GameParameters;
import com.br.phdev.metafighter.cmp.Entity;
import com.br.phdev.metafighter.cmp.connections.packets.Action;
import com.br.phdev.metafighter.cmp.connections.packets.Packet;
import com.br.phdev.metafighter.cmp.event.Event;
import com.br.phdev.metafighter.cmp.event.animation.Fade;
import com.br.phdev.metafighter.cmp.event.animation.Flash;
import com.br.phdev.metafighter.cmp.event.animation.Selected;
import com.br.phdev.metafighter.cmp.event.listeners.ActionListener;
import com.br.phdev.metafighter.cmp.event.listeners.AutoDestroyableListener;
import com.br.phdev.metafighter.cmp.game.Character;
import com.br.phdev.metafighter.cmp.game.GameLabel;
import com.br.phdev.metafighter.cmp.game.Player;
import com.br.phdev.metafighter.cmp.game.PlayerAction;
import com.br.phdev.metafighter.cmp.graphics.Sprite;
import com.br.phdev.metafighter.cmp.graphics.Texture;
import com.br.phdev.metafighter.cmp.misc.Constant;
import com.br.phdev.metafighter.cmp.misc.GameContext;
import com.br.phdev.metafighter.cmp.misc.Timer;
import com.br.phdev.metafighter.cmp.window.BackGround;
import com.br.phdev.metafighter.cmp.window.Button;
import com.br.phdev.metafighter.cmp.window.Label;
import com.br.phdev.metafighter.cmp.window.Popup;
import com.br.phdev.metafighter.cmp.window.Scene;
import com.br.phdev.metafighter.cmp.window.Screen;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class SelectCharacterScreen extends Screen {

    private Scene mainSceneSinglePlayer;
    private Scene mainSceneMultiplayer;

    private GameLabel[] gameLabel;

    private GameLabel gameLabelLuiz;
    private GameLabel gameLabelQuele;
    private GameLabel gameLabelRomulo;
    private GameLabel gameLabelPatricia;
    private GameLabel gameLabelKaila;
    private GameLabel gameLabelGuedes;

    private Label infoLabel;

    private Character[] characters;

    private Sprite[] spriteViewKaila;
    private Sprite[] spriteViewGuedes;
    private Sprite[] spriteViewQuele;
    private Sprite[] spriteViewRomulo;
    private Sprite[] spriteViewPatricia;
    private Sprite[] spriteViewLuiz;

    private PlayerAction[] charactersMoveAction;
    private PlayerAction[] charactersVictoryAction;

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
    private Texture gameLabelCharVisibleTexture;
    private Texture gameLabelCharNoVisibleTexture;
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

    private Matrix matrix;

    private int gameMode;

    private Button backButton;

    public SelectCharacterScreen(GameContext context, int gameMode) {
        super(context);

        this.gameMode = gameMode;

        init();
    }

    @Override
    protected boolean loadTextures() {

        backButtonTexture = new Texture("images/buttons/3.png");

        //gameLabelPlayerTexture = new Texture("images/labels/label3.png");
        gameLabelCharVisibleTexture = new Texture("images/labels/label4.png");
        gameLabelCharNoVisibleTexture = new Texture("images/labels/label1.png");

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
        float defaultTextSize = GameParameters.getInstance().defaultTextSize;

        matrix = new Matrix();
        matrix.postScale(-1,1);

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

        this.backButton = new Button(
                new RectF(0, 0, buttonSize.width()/2, buttonSize.height()/2),
                "Voltar", backButtonTexture);
        this.backButton.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                new MainScreen(context);
            }
        });
        this.backButton.getText().setTextSize(defaultTextSize);

        this.mainBackground = new BackGround(screenSize, mainBackgroundTexture);

        final RectF gameLabelPlayerArea = new RectF(0,0, (screenSize.width()/16)*3, (screenSize.height()/8)*4);
        final float marginX = (screenSize.width()/16)/2;
        final float marginY = (screenSize.height()/8)/2;
        final RectF gameLabelCharArea = new RectF(0,0,marginX*4, marginY*4);

        gameLabelPlayer1 = new GameLabel(
                new RectF( marginX*2,
                        marginY*6,
                        marginX*2 + gameLabelPlayerArea.width(),
                        marginY*6 + gameLabelPlayerArea.height()),
                gameLabelPlayerTexture, null);
        gameLabelPlayer1.addText("",
                new RectF( marginX*2, marginY*4, gameLabelPlayer1.getArea().right, gameLabelPlayer1.getArea().top),
                20, Color.WHITE);
        gameLabelPlayer1.getText().setTextSize(defaultTextSize);


        gameLabelPlayer2 = new GameLabel(
                new RectF( screenSize.right - gameLabelPlayerArea.width() - marginX*2,
                        marginY*6,
                        screenSize.right - marginX*2,
                        marginY*6 + gameLabelPlayerArea.height()),
                gameLabelPlayerTexture, null);
        gameLabelPlayer2.addText("",
                new RectF( gameLabelPlayer2.getArea().left, marginY*4, gameLabelPlayer2.getArea().right, gameLabelPlayer2.getArea().top),
                20, Color.WHITE);
        gameLabelPlayer2.getText().setTextSize(defaultTextSize);

        Paint tmpPaint = new Paint();
        tmpPaint.setTextSize(defaultTextSize);
        String info = "Toque uma vez para vizualizar o personagem";
        Rect infoLabelRect = new Rect();
        tmpPaint.getTextBounds(info, 0, info.length(), infoLabelRect);

        infoLabel = new Label(new RectF(gameLabelPlayer1.getArea().right, screenSize.centerY() + marginY*4, gameLabelPlayer2.getArea().left, screenSize.centerY() + marginY*4 + infoLabelRect.height() * 2),
                "Toque uma vez para vizualizar o personagem\nE outra vez para seleciona-lo.", true);
        infoLabel.getText().setTextSize(defaultTextSize);
        infoLabel.getPaint().setColor(Color.BLACK);
        infoLabel.getPaint().setAlpha(150);
        infoLabel.getText().setColor(Color.WHITE);
        infoLabel.getText().getPaint().setAntiAlias(true);

        RectF size = gameLabelPlayer1.getArea();

        charactersMoveAction[Character.KAILA] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionKaila, 0, 7, false), 10, -1, false);
        charactersVictoryAction[Character.KAILA] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionKaila, 8, 22, false), 6, -1, false);
        for (Sprite aTmpSpriteAction : spriteActionKaila)
            aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());

        charactersMoveAction[Character.GUEDES] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionGuedes, 0, 7, false), 10, -1, false);
        charactersVictoryAction[Character.GUEDES] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionGuedes, 8, 22, false), 6, -1, false);
        for (Sprite aTmpSpriteAction : spriteActionGuedes)
            aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());

        charactersMoveAction[Character.LUIZ] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionLuiz, 0, 7, false), 10, -1, false);
        charactersVictoryAction[Character.LUIZ] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionLuiz, 8, 22, false), 6, -1, false);
        for (Sprite aTmpSpriteAction : spriteActionLuiz)
            aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());

        charactersMoveAction[Character.PATRICIA] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionPatricia, 0, 7, false), 10, -1, false);
        charactersVictoryAction[Character.PATRICIA] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionPatricia, 8, 22, false), 6, -1, false);
        for (Sprite aTmpSpriteAction : spriteActionPatricia)
            aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());

        charactersMoveAction[Character.QUELE] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionQuele, 0, 7, false), 10, -1, false);
        charactersVictoryAction[Character.QUELE] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionQuele, 8, 22, false), 6, -1, false);
        for (Sprite aTmpSpriteAction : spriteActionQuele)
            aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());

        charactersMoveAction[Character.ROMULO] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionRomulo, 0, 7, false), 10, -1, false);
        charactersVictoryAction[Character.ROMULO] = new PlayerAction(Sprite.getSpritesFromSprites(spriteActionRomulo, 8, 22, false), 6, -1, false);
        for (Sprite aTmpSpriteAction : spriteActionRomulo)
            aTmpSpriteAction.getTexture().scaleImage((int) size.width(), (int) size.height());

        final RectF mainAreaGameLabelsChars = new RectF(screenSize.centerX() - marginX * 10,
                marginY, screenSize.centerX() + marginX * 10, screenSize.centerY() + marginY * 5);

        int rows = 3;
        int columns = 4;
        int counter = 0;
        RectF areaGameLabelsChars[] = new RectF[rows * columns];


        for (int i=0; i<rows; i++){
            for (int j=0; j<columns; j++){
                areaGameLabelsChars[counter] = new RectF(
                        mainAreaGameLabelsChars.left + (mainAreaGameLabelsChars.width() / columns) * j,
                        mainAreaGameLabelsChars.top + (mainAreaGameLabelsChars.height() / rows) * i,
                        mainAreaGameLabelsChars.left + (mainAreaGameLabelsChars.width() / columns) * (j+1),
                        mainAreaGameLabelsChars.top + (mainAreaGameLabelsChars.height() / rows) * (i+1));
                counter++;
            }
        }

        gameLabelLuiz = new GameLabel(
                new RectF( areaGameLabelsChars[0]),
                gameLabelCharVisibleTexture, null);
        gameLabelLuiz.setSprites(characters[Character.LUIZ].getView());

        gameLabelQuele = new GameLabel(
                new RectF( areaGameLabelsChars[1]),
                gameLabelCharVisibleTexture, null);
        gameLabelQuele.setSprites(characters[Character.QUELE].getView());

        gameLabelRomulo = new GameLabel(
                new RectF( areaGameLabelsChars[2]),
                gameLabelCharVisibleTexture, null);
        gameLabelRomulo.setSprites(characters[Character.ROMULO].getView());

        gameLabelPatricia = new GameLabel(
                new RectF( areaGameLabelsChars[3]),
                gameLabelCharVisibleTexture, null);
        gameLabelPatricia.setSprites(characters[Character.PATRICIA].getView());

        gameLabelKaila = new GameLabel(
                new RectF(areaGameLabelsChars[4]),
                gameLabelCharNoVisibleTexture, null);
        gameLabelKaila.setSprites(characters[Character.KAILA].getView());

        gameLabelGuedes = new GameLabel(
                new RectF( areaGameLabelsChars[5]),
                gameLabelCharVisibleTexture, null);
        gameLabelGuedes.setSprites(characters[Character.GUEDES].getView());


        /*
        gameLabelLuiz = new GameLabel(
                new RectF( gameLabelPlayer1.getArea().right + marginX,
                        marginY,
                        gameLabelPlayer1.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY + gameLabelCharArea.height()),
                gameLabelCharVisibleTexture, null);
        gameLabelLuiz.setSprites(characters[Character.LUIZ].getView());
        gameLabelLuiz.setId(Character.LUIZ);

        gameLabelQuele = new GameLabel(
                new RectF( gameLabelLuiz.getArea().right + marginX,
                        marginY,
                        gameLabelLuiz.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY + gameLabelCharArea.height()),
                gameLabelCharVisibleTexture, null);
        gameLabelQuele.setSprites(characters[Character.QUELE].getView());
        gameLabelQuele.setId(Character.QUELE);

        gameLabelRomulo = new GameLabel(
                new RectF( gameLabelQuele.getArea().right + marginX,
                        marginY,
                        gameLabelQuele.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY + gameLabelCharArea.height()),
                gameLabelCharVisibleTexture, null);
        gameLabelRomulo.setSprites(characters[Character.ROMULO].getView());
        gameLabelRomulo.setId(Character.ROMULO);

        gameLabelPatricia = new GameLabel(
                new RectF( gameLabelPlayer1.getArea().right + marginX,
                        marginY*2 + gameLabelCharArea.height(),
                        gameLabelPlayer1.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY*2 + gameLabelCharArea.height() + gameLabelCharArea.height()),
                gameLabelCharVisibleTexture, null);
        gameLabelPatricia.setSprites(characters[Character.PATRICIA].getView());
        gameLabelPatricia.setId(Character.PATRICIA);

        gameLabelKaila = new GameLabel(
                new RectF(gameLabelPatricia.getArea().right + marginX,
                        marginY * 2 + gameLabelCharArea.height(),
                        gameLabelPatricia.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY * 2 + gameLabelCharArea.height() + gameLabelCharArea.height()),
                gameLabelCharVisibleTexture, null);
        gameLabelKaila.setSprites(characters[Character.KAILA].getView());
        gameLabelKaila.setId(Character.KAILA);

        gameLabelGuedes = new GameLabel(
                new RectF( gameLabelKaila.getArea().right + marginX,
                        marginY*2 + gameLabelCharArea.height(),
                        gameLabelKaila.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY*2 + gameLabelCharArea.height() + gameLabelCharArea.height()),
                gameLabelCharVisibleTexture, null);
        gameLabelGuedes.setSprites(characters[Character.GUEDES].getView());
        gameLabelGuedes.setId(Character.GUEDES);
        */

        if (gameMode == Constant.GAMEMODE_SINGLEPLAYER){

            mainSceneSinglePlayer = new Scene() {

                @Override
                public void init() {

                    gameLabelLuiz.addEventListener(new ActionListener() {
                        @Override
                        public void actionPerformed(Event event) {
                            changeLabelPlayer(Character.LUIZ);
                            choseCharacter(gameLabelLuiz, Character.LUIZ);
                        }
                    });

                    gameLabelQuele.addEventListener(new ActionListener() {
                        @Override
                        public void actionPerformed(Event event) {
                            changeLabelPlayer(Character.QUELE);
                            choseCharacter(gameLabelQuele, Character.QUELE);
                        }
                    });

                    gameLabelRomulo.addEventListener(new ActionListener() {
                        @Override
                        public void actionPerformed(Event event) {
                            changeLabelPlayer(Character.ROMULO);
                            choseCharacter(gameLabelRomulo, Character.ROMULO);
                        }
                    });

                    gameLabelPatricia.addEventListener(new ActionListener() {
                        @Override
                        public void actionPerformed(Event event) {
                            changeLabelPlayer(Character.PATRICIA);
                            choseCharacter(gameLabelPatricia, Character.PATRICIA);
                        }
                    });

                    gameLabelKaila.addEventListener(new ActionListener() {
                        @Override
                        public void actionPerformed(Event event) {
                            changeLabelPlayer(Character.KAILA);
                            choseCharacter(gameLabelKaila, Character.KAILA);
                        }
                    });

                    gameLabelGuedes.addEventListener(new ActionListener() {
                        @Override
                        public void actionPerformed(Event event) {
                            changeLabelPlayer(Character.GUEDES);
                            choseCharacter(gameLabelGuedes, Character.GUEDES);
                        }
                    });

                    super.add(mainBackground);
                    super.add(gameLabelPlayer1);
                    super.add(gameLabelPlayer2);
                    super.add(infoLabel);
                    super.add(gameLabelGuedes);
                    super.add(gameLabelKaila);
                    super.add(gameLabelLuiz);
                    super.add(gameLabelPatricia);
                    super.add(gameLabelRomulo);
                    super.add(gameLabelQuele);
                    super.add(backButton);
                }

                @Override
                public void update(){
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
                            new MatchScreen(context, gameMode, player1.getCharID(), player2.getCharID());
                    }
                }

                private void changeLabelPlayer(int charID){
                    if (player1 == null){
                        gameLabelPlayer1.getText().setText(characters[charID].getName());
                        gameLabelPlayer1.setSprites(characters[charID].getSprites());
                        currentPlayerAction1 = charactersMoveAction[charID];
                    }
                    else if (player2 == null){
                        gameLabelPlayer2.getText().setText(characters[charID].getName());
                        gameLabelPlayer2.setSprites(characters[charID].getSprites());
                        currentPlayerAction2 = charactersMoveAction[charID];
                    }

                }

                private void choseCharacter(Entity entity, int charID) {
                    Selected selected = null;

                    if (player1 == null) {
                        selected = selectedPlayer1;
                    } else if (player2 == null) {
                        selected = selectedPlayer2;
                    }

                    if (selected != null) {
                        selected.setEntity(entity);

                        if (currentSelect == charID) {
                            flash = new Flash(new Fade(51, 0), entity, flashDuration, handler);
                            selected.setEstatic();
                            currentSelect = -1;

                            if (selected.equals(selectedPlayer1)) {
                                player1 = new Player();
                                player1.setName(characters[charID].getName());
                                player1.setCharID(charID);
                            }
                            if (selected.equals(selectedPlayer2)) {
                                player2 = new Player();
                                player2.setName(characters[charID].getName());
                                player2.setCharID(charID);
                            }


                        } else
                            currentSelect = charID;
                    }

                }
            };
            currentScene = mainSceneSinglePlayer.start();
        }
        else {
            mainSceneMultiplayer = new Scene() {

                final int READY_FIRST = 0;
                final int ME_TOO = 1;
                private final int SELECT = 3;
                private final int SELECTED = 4;

                private Timer timer;
                private boolean waitingPlayers = true;
                private boolean allReady = false;
                private Popup message;

                private Thread waitingThread;

                private ConnectionManager manager;

                private Matrix matrix;

                private int myID;

                @Override
                public void init() {

                    myID = gameMode;

                    manager = context.getConnectionType();
                    manager.init();

                    //Carrega os labels
                    {
                        gameLabelLuiz = new GameLabel(
                                new RectF( gameLabelPlayer1.getArea().right + marginX,
                                        marginY,
                                        gameLabelPlayer1.getArea().right + marginX + gameLabelCharArea.width(),
                                        marginY + gameLabelCharArea.height()),
                                gameLabelCharVisibleTexture, null);
                        gameLabelLuiz.setSprites(characters[Character.LUIZ].getView());
                        //gameLabelLuiz.addEventListener(handler);
                        gameLabelLuiz.addEventListener(new ActionListener() {
                            @Override
                            public void actionPerformed(Event event) {
                                selectPlayer(Character.LUIZ, myID);
                            }
                        });


                        gameLabelQuele = new GameLabel(
                                new RectF( gameLabelLuiz.getArea().right + marginX,
                                        marginY,
                                        gameLabelLuiz.getArea().right + marginX + gameLabelCharArea.width(),
                                        marginY + gameLabelCharArea.height()),
                                gameLabelCharVisibleTexture, null);
                        gameLabelQuele.setSprites(characters[Character.QUELE].getView());
                        //gameLabelQuele.addEventListener(handler);
                        gameLabelQuele.addEventListener(new ActionListener() {
                            @Override
                            public void actionPerformed(Event event) {
                                selectPlayer(Character.QUELE, myID);
                            }
                        });


                        gameLabelRomulo = new GameLabel(
                                new RectF( gameLabelQuele.getArea().right + marginX,
                                        marginY,
                                        gameLabelQuele.getArea().right + marginX + gameLabelCharArea.width(),
                                        marginY + gameLabelCharArea.height()),
                                gameLabelCharVisibleTexture, null);
                        gameLabelRomulo.setSprites(characters[Character.ROMULO].getView());
                        //gameLabelRomulo.addEventListener(handler);
                        gameLabelRomulo.addEventListener(new ActionListener() {
                            @Override
                            public void actionPerformed(Event event) {
                                selectPlayer(Character.ROMULO, myID);
                            }
                        });


                        gameLabelPatricia = new GameLabel(
                                new RectF( gameLabelPlayer1.getArea().right + marginX,
                                        marginY*2 + gameLabelCharArea.height(),
                                        gameLabelPlayer1.getArea().right + marginX + gameLabelCharArea.width(),
                                        marginY*2 + gameLabelCharArea.height() + gameLabelCharArea.height()),
                                gameLabelCharVisibleTexture, null);
                        gameLabelPatricia.setSprites(characters[Character.PATRICIA].getView());
                        //gameLabelPatricia.addEventListener(handler);
                        gameLabelPatricia.addEventListener(new ActionListener() {
                            @Override
                            public void actionPerformed(Event event) {
                                selectPlayer(Character.PATRICIA, myID);
                            }
                        });


                        gameLabelKaila = new GameLabel(
                                new RectF( gameLabelPatricia.getArea().right + marginX,
                                        marginY*2 + gameLabelCharArea.height(),
                                        gameLabelPatricia.getArea().right + marginX + gameLabelCharArea.width(),
                                        marginY*2 + gameLabelCharArea.height() + gameLabelCharArea.height()),
                                gameLabelCharVisibleTexture, null);
                        gameLabelKaila.setSprites(characters[Character.KAILA].getView());
                        //gameLabelKaila.addEventListener(handler);
                        gameLabelKaila.addEventListener(new ActionListener() {
                            @Override
                            public void actionPerformed(Event event) {
                                selectPlayer(Character.KAILA, myID);
                            }
                        });


                        gameLabelGuedes = new GameLabel(
                                new RectF( gameLabelKaila.getArea().right + marginX,
                                        marginY*2 + gameLabelCharArea.height(),
                                        gameLabelKaila.getArea().right + marginX + gameLabelCharArea.width(),
                                        marginY*2 + gameLabelCharArea.height() + gameLabelCharArea.height()),
                                gameLabelCharVisibleTexture, null);
                        gameLabelGuedes.setSprites(characters[Character.GUEDES].getView());
                        //gameLabelGuedes.addEventListener(handler);
                        gameLabelGuedes.addEventListener(new ActionListener() {
                            @Override
                            public void actionPerformed(Event event) {
                                selectPlayer(Character.GUEDES, myID);
                            }
                        });
                    }

                    super.add(mainBackground);
                    super.add(gameLabelPlayer1);
                    super.add(gameLabelPlayer2);
                    super.add(infoLabel);
                    super.add(gameLabelGuedes);
                    //super.add(gameLabelKaila);
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
                                new MatchScreen(context, gameMode, player1.getCharID(), player2.getCharID());
                        }
                    }
                }

                @Override
                public void draw(Canvas canvas){

                    if (waitingPlayers)
                        message.draw(canvas);
                    else {
                        super.draw(canvas);
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


                        } else{
                            currentSelect = charID;
                        }
                    }

                }
            };
            currentScene = mainSceneMultiplayer.start();
        }

        return true;
    }

    @Override
    public void draw(Canvas canvas){
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

            //log("Numero de sprites: " + numeroSpritesAchados);

            int contador = 0;
            tmpSpriteAction = new Sprite[numeroSpritesAchados];

            for (int i=0; i<paths.length; i++){
                //log(paths[i] + "");
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
