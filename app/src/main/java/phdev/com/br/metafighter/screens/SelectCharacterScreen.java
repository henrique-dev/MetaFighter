package phdev.com.br.metafighter.screens;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.Entity;
import phdev.com.br.metafighter.cmp.event.ClickEvent;
import phdev.com.br.metafighter.cmp.event.Event;
import phdev.com.br.metafighter.cmp.event.animation.GoAndBack;
import phdev.com.br.metafighter.cmp.event.listeners.ActionListener;
import phdev.com.br.metafighter.cmp.event.listeners.ClickListener;
import phdev.com.br.metafighter.cmp.event.listeners.EventListener;
import phdev.com.br.metafighter.cmp.game.Player;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.Button;
import phdev.com.br.metafighter.cmp.game.GameLabel;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.window.Text;
import phdev.com.br.metafighter.cmp.graphics.Sprite;
import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.repositories.PlayersRepository;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class SelectCharacterScreen extends Screen {

    final int GUEDES = 0;
    final int KAILA = 1;
    final int LUIZ = 2;
    final int PATRICIA = 3;
    final int QUELE = 4;
    final int ROMULO = 5;

    private String[] names = new String[]{"Carlos Guedes", "Kaila", "Luiz Carlos", "Patricia", "Quele", "Romulo"};

    GameLabel gameLabelLuiz;
    GameLabel gameLabelQuele;
    GameLabel gameLabelRomulo;
    GameLabel gameLabelPatricia;
    GameLabel gameLabelKaila;
    GameLabel gameLabelGuedes;

    BackGround mainBackground;

    private Texture backButtonTexture;
    private Texture gameLabelPlayerTexture;
    private Texture gameLabelCharTexture;

    GameLabel gameLabelPlayer1;
    GameLabel gameLabelPlayer2;

    Button backButton;

    public SelectCharacterScreen(EventListener listener) {
        super(listener);
    }

    @Override
    protected boolean loadTextures() {

        this.backButtonTexture = new Texture("images/buttons/button1.png");

        this.gameLabelPlayerTexture = new Texture("images/labels/label3.png");
        this.gameLabelCharTexture = new Texture("images/labels/label4.png");

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

        RectF buttonSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);
        float fontSize = Text.adaptText(new String[]{"Voltar"}, buttonSize);
        this.backButton = new Button(
                new RectF(screenSize.centerX() - buttonSize.width()/2,
                        screenSize.bottom - buttonSize.height(),
                        screenSize.centerX() + buttonSize.width()/2,
                        screenSize.bottom),
                "Voltar", backButtonTexture);
        this.backButton.addEventListener(new ClickListener() {
            @Override
            public void actionPerformed(Event event) {
                new MainScreen(listener);
            }

            @Override
            public boolean pressedPerformed(ClickEvent event) {
                return true;
            }

            @Override
            public boolean releasedPerformed(ClickEvent event) {
                return true;
            }
        });
        this.backButton.getText().setTextSize(fontSize);

        this.mainBackground = new BackGround(screenSize, Color.rgb(255,51,126));

        RectF gameLabelPlayerArea = new RectF(0,0, (screenSize.width()/16)*3, (screenSize.height()/8)*4);

        float marginX = (screenSize.width()/16)/2;
        float marginY = (screenSize.height()/8)/2;

        RectF gameLabelCharArea = new RectF(0,0,marginX*4, marginY*4);

        this.gameLabelPlayer1 = new GameLabel(
                new RectF( marginX*2,
                        marginY*6,
                        marginX*2 + gameLabelPlayerArea.width(),
                        marginY*6 + gameLabelPlayerArea.height()),
                this.gameLabelPlayerTexture, null, "");
        this.gameLabelPlayer1.addText("Jogador 1",
                new RectF( marginX*2, marginY*4, gameLabelPlayer1.getArea().right, gameLabelPlayer1.getArea().top),
                20, Color.WHITE);


        this.gameLabelPlayer2 = new GameLabel(
                new RectF( screenSize.right - gameLabelPlayerArea.width() - marginX*2,
                        marginY*6,
                        screenSize.right - marginX*2,
                        marginY*6 + gameLabelPlayerArea.height()),
                this.gameLabelPlayerTexture, null, "");
        this.gameLabelPlayer2.addText("Jogador 2",
                new RectF( gameLabelPlayer2.getArea().left, marginY*4, gameLabelPlayer2.getArea().right, gameLabelPlayer2.getArea().top),
                20, Color.WHITE);

        HandlerChoiceCharacter handler = new HandlerChoiceCharacter();

        gameLabelLuiz = new GameLabel(
                new RectF( gameLabelPlayer1.getArea().right + marginX,
                        marginY,
                        gameLabelPlayer1.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null, "Luiz Carlos");
        gameLabelLuiz.setSprites(new Sprite[]{new Sprite(new Texture("images/players/luiz.png"))});
        gameLabelLuiz.setId(LUIZ);
        gameLabelLuiz.addEventListener(handler);

        gameLabelQuele = new GameLabel(
                new RectF( gameLabelLuiz.getArea().right + marginX,
                        marginY,
                        gameLabelLuiz.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null, "Quele");
        gameLabelQuele.setSprites(new Sprite[]{new Sprite(new Texture("images/players/quele.png"))});
        gameLabelQuele.setId(QUELE);
        gameLabelQuele.addEventListener(handler);

        gameLabelRomulo = new GameLabel(
                new RectF( gameLabelQuele.getArea().right + marginX,
                        marginY,
                        gameLabelQuele.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null, "Romulo");
        gameLabelRomulo.setSprites(new Sprite[]{new Sprite(new Texture("images/players/romulo.png"))});
        gameLabelRomulo.setId(ROMULO);
        gameLabelRomulo.addEventListener(handler);

        gameLabelPatricia = new GameLabel(
                new RectF( gameLabelPlayer1.getArea().right + marginX,
                        marginY*2 + gameLabelCharArea.height(),
                        gameLabelPlayer1.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY*2 + gameLabelCharArea.height() + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null, "Patricia");
        gameLabelPatricia.setSprites(new Sprite[]{new Sprite(new Texture("images/players/patricia.png"))});
        gameLabelPatricia.setId(PATRICIA);
        gameLabelPatricia.addEventListener(handler);

        gameLabelKaila = new GameLabel(
                new RectF( gameLabelPatricia.getArea().right + marginX,
                        marginY*2 + gameLabelCharArea.height(),
                        gameLabelPatricia.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY*2 + gameLabelCharArea.height() + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null, "Kaila");
        gameLabelKaila.setSprites(new Sprite[]{new Sprite(new Texture("images/players/kaila.png"))});
        gameLabelKaila.setId(KAILA);
        gameLabelKaila.addEventListener(handler);

        gameLabelGuedes = new GameLabel(
                new RectF( gameLabelKaila.getArea().right + marginX,
                        marginY*2 + gameLabelCharArea.height(),
                        gameLabelKaila.getArea().right + marginX + gameLabelCharArea.width(),
                        marginY*2 + gameLabelCharArea.height() + gameLabelCharArea.height()),
                this.gameLabelCharTexture, null, "Carlos Guedes");
        gameLabelGuedes.setSprites(new Sprite[]{new Sprite(new Texture("images/players/guedes.png"))});
        gameLabelGuedes.setId(GUEDES);
        gameLabelGuedes.addEventListener(handler);

        add(mainBackground);

        add(gameLabelPlayer1);
        add(gameLabelPlayer2);

        add(gameLabelGuedes);
        add(gameLabelKaila);
        add(gameLabelLuiz);
        add(gameLabelPatricia);
        add(gameLabelRomulo);
        add(gameLabelQuele);


        add(backButton);

        return true;
    }

    public class HandlerChoiceCharacter implements ActionListener{

        @Override
        public void actionPerformed(Event event) {
            int id = ((ClickEvent)event).id;
            gameLabelPlayer1.getText().setText(names[id]);
        }
    }
}
