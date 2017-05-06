package phdev.com.br.metafighter.screens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

import java.util.List;

import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.Entity;
import phdev.com.br.metafighter.cmp.event.Event;
import phdev.com.br.metafighter.cmp.event.animation.Fade;
import phdev.com.br.metafighter.cmp.event.animation.Flash;
import phdev.com.br.metafighter.cmp.event.animation.Selected;
import phdev.com.br.metafighter.cmp.event.listeners.ActionListener;
import phdev.com.br.metafighter.cmp.event.listeners.AutoDestroyableListener;
import phdev.com.br.metafighter.cmp.event.listeners.EventListener;
import phdev.com.br.metafighter.cmp.game.Character;
import phdev.com.br.metafighter.cmp.game.GameLabel;
import phdev.com.br.metafighter.cmp.game.Player;
import phdev.com.br.metafighter.cmp.graphics.Sprite;
import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.Button;
import phdev.com.br.metafighter.cmp.window.Scene;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.window.Text;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class SelectCharacterScreen extends Screen {

    private Scene mainScene;

    private final int GUEDES = 0;
    private final int KAILA = 1;
    private final int LUIZ = 2;
    private final int PATRICIA = 3;
    private final int QUELE = 4;
    private final int ROMULO = 5;

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
    private int flashDuration = 2;

    private Button backButton;

    public SelectCharacterScreen(EventListener listener) {
        super(listener);
    }

    @Override
    protected boolean loadTextures() {

        this.backButtonTexture = new Texture("images/buttons/button2.png");

        this.gameLabelPlayerTexture = new Texture("images/labels/label3.png");
        this.gameLabelCharTexture = new Texture("images/labels/label4.png");

        this.mainBackgroundTexture = new Texture("images/backgrounds/background7.png");

        spriteViewKaila = Sprite.getSpriteFromTexture(new Texture("images/characters/kaila/view.png"), 1, 1);
        spriteViewGuedes = Sprite.getSpriteFromTexture(new Texture("images/characters/guedes/view.png"), 1, 1);
        spriteViewPatricia = Sprite.getSpriteFromTexture(new Texture("images/characters/patricia/view.png"), 1, 1);
        spriteViewLuiz = Sprite.getSpriteFromTexture(new Texture("images/characters/luiz/view.png"), 1, 1);
        spriteViewQuele = Sprite.getSpriteFromTexture(new Texture("images/characters/quele/view.png"), 1, 1);
        spriteViewRomulo = Sprite.getSpriteFromTexture(new Texture("images/characters/romulo/view.png"), 1, 1);

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

        characters[KAILA] = new Character(null, null, null, spriteViewKaila, "Kaila");
        characters[GUEDES] = new Character(null, null, null, spriteViewGuedes, "Carlos Guedes");
        characters[LUIZ] = new Character(null, null, null, spriteViewLuiz, "Luiz Silva");
        characters[PATRICIA] = new Character(null, null, null, spriteViewPatricia, "Patricia");
        characters[QUELE] = new Character(null, null, null, spriteViewQuele, "Quele");
        characters[ROMULO] = new Character(null, null, null, spriteViewRomulo, "Romulo");

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
                new MainScreen(listener);
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
                this.gameLabelPlayerTexture, null);
        this.gameLabelPlayer1.addText("Jogador 1",
                new RectF( marginX*2, marginY*4, gameLabelPlayer1.getArea().right, gameLabelPlayer1.getArea().top),
                20, Color.WHITE);


        this.gameLabelPlayer2 = new GameLabel(
                new RectF( screenSize.right - gameLabelPlayerArea.width() - marginX*2,
                        marginY*6,
                        screenSize.right - marginX*2,
                        marginY*6 + gameLabelPlayerArea.height()),
                this.gameLabelPlayerTexture, null);
        this.gameLabelPlayer2.addText("Jogador 2",
                new RectF( gameLabelPlayer2.getArea().left, marginY*4, gameLabelPlayer2.getArea().right, gameLabelPlayer2.getArea().top),
                20, Color.WHITE);

        //HandlerChoiceCharacter handler = new HandlerChoiceCharacter();

        final DisableFlashHandler handler = new DisableFlashHandler();

        gameLabelLuiz = new GameLabel(
                new RectF( gameLabelPlayer1.getArea().right + marginX,
                        marginY,
                        gameLabelPlayer1.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null);
        gameLabelLuiz.setSprites(characters[LUIZ].getView());
        gameLabelLuiz.setId(LUIZ);
        //gameLabelLuiz.addEventListener(handler);
        gameLabelLuiz.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                changeLabelPlayer(characters[LUIZ]);

                choseCharacter(gameLabelLuiz, LUIZ, handler);

                /*
                Selected selected = null;

                if (player1 == null) {
                    selected = selectedPlayer1;
                }
                else
                    if (player2 == null) {
                        selected = selectedPlayer2;
                    }

                if (selected != null){
                    selected.setEntity(gameLabelLuiz);

                    if (currentSelect == LUIZ) {
                        flash = new Flash(new Fade(15, 0), gameLabelLuiz, flashDuration, handler);
                        selected.setEstatic();
                        currentSelect = -1;

                        if (selected.equals(selectedPlayer1)) {
                            player1 = new Player();
                        }
                        if (selected.equals(selectedPlayer2)){
                            player2 = new Player();
                        }

                        if (selected.equals(selectedPlayer1))
                            log("Player 1 selecionado");
                        else
                            log("Player 2 selecionado");


                    }
                    else
                        currentSelect = LUIZ;
                }
                */

            }
        });

        gameLabelQuele = new GameLabel(
                new RectF( gameLabelLuiz.getArea().right + marginX,
                        marginY,
                        gameLabelLuiz.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null);
        gameLabelQuele.setSprites(characters[QUELE].getView());
        gameLabelQuele.setId(QUELE);
        //gameLabelQuele.addEventListener(handler);
        gameLabelQuele.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {

                changeLabelPlayer(characters[QUELE]);

                choseCharacter(gameLabelQuele, QUELE, handler);
            }
        });

        gameLabelRomulo = new GameLabel(
                new RectF( gameLabelQuele.getArea().right + marginX,
                        marginY,
                        gameLabelQuele.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null);
        gameLabelRomulo.setSprites(characters[ROMULO].getView());
        gameLabelRomulo.setId(ROMULO);
        //gameLabelRomulo.addEventListener(handler);
        gameLabelRomulo.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                changeLabelPlayer(characters[ROMULO]);


                choseCharacter(gameLabelRomulo, ROMULO, handler);
            }
        });

        gameLabelPatricia = new GameLabel(
                new RectF( gameLabelPlayer1.getArea().right + marginX,
                        marginY*2 + gameLabelCharArea.height(),
                        gameLabelPlayer1.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY*2 + gameLabelCharArea.height() + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null);
        gameLabelPatricia.setSprites(characters[PATRICIA].getView());
        gameLabelPatricia.setId(PATRICIA);
        //gameLabelPatricia.addEventListener(handler);
        gameLabelPatricia.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                changeLabelPlayer(characters[PATRICIA]);


                choseCharacter(gameLabelPatricia, PATRICIA, handler);
            }
        });

        gameLabelKaila = new GameLabel(
                new RectF( gameLabelPatricia.getArea().right + marginX,
                        marginY*2 + gameLabelCharArea.height(),
                        gameLabelPatricia.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY*2 + gameLabelCharArea.height() + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null);
        gameLabelKaila.setSprites(characters[KAILA].getView());
        gameLabelKaila.setId(KAILA);
        //gameLabelKaila.addEventListener(handler);
        gameLabelKaila.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                changeLabelPlayer(characters[KAILA]);


                choseCharacter(gameLabelKaila, KAILA, handler);
            }
        });

        gameLabelGuedes = new GameLabel(
                new RectF( gameLabelKaila.getArea().right + marginX,
                        marginY*2 + gameLabelCharArea.height(),
                        gameLabelKaila.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY*2 + gameLabelCharArea.height() + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null);
        gameLabelGuedes.setSprites(characters[GUEDES].getView());
        gameLabelGuedes.setId(GUEDES);
        //gameLabelGuedes.addEventListener(handler);
        gameLabelGuedes.addEventListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                changeLabelPlayer(characters[GUEDES]);


                choseCharacter(gameLabelGuedes, GUEDES, handler);
            }
        });

        mainScene = new Scene();

        mainScene.add(mainBackground);

        mainScene.add(gameLabelPlayer1);
        mainScene.add(gameLabelPlayer2);

        mainScene.add(gameLabelGuedes);
        mainScene.add(gameLabelKaila);
        mainScene.add(gameLabelLuiz);
        mainScene.add(gameLabelPatricia);
        mainScene.add(gameLabelRomulo);
        mainScene.add(gameLabelQuele);
        mainScene.add(backButton);

        currentScene = mainScene;

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
            new MatchScreen(listener, null, player1, player2);
        }
    }

    private void changeLabelPlayer(Character character){
        gameLabelPlayer1.getText().setText(character.getName());
        gameLabelPlayer1.setSprites(character.getSprites());
    }

    private void choseCharacter(Entity entity, int charID, DisableFlashHandler handler) {
        Selected selected = null;

        if (player1 == null) {
            selected = selectedPlayer1;
        } else if (player2 == null) {
            selected = selectedPlayer2;
        }

        if (selected != null) {
            selected.setEntity(entity);

            if (currentSelect == charID) {
                flash = new Flash(new Fade(15, 0), entity, flashDuration, handler);
                selected.setEstatic();
                currentSelect = -1;

                if (selected.equals(selectedPlayer1)) {
                    player1 = new Player();
                    player1.setName(characters[charID].getName());
                }
                if (selected.equals(selectedPlayer2)) {
                    player2 = new Player();
                    player2.setName(characters[charID].getName());
                }

                if (selected.equals(selectedPlayer1))
                    log("Player 1 selecionado");
                else
                    log("Player 2 selecionado");


            } else
                currentSelect = charID;
        }

    }

        private class DisableFlashHandler implements AutoDestroyableListener{

            @Override
            public void autoDestroy(Object entity) {
                flash = null;
            }
        }
}
