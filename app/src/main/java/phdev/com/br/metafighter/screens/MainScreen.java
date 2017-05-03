package phdev.com.br.metafighter.screens;

import android.graphics.Color;
import android.graphics.RectF;

import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.event.ClickEvent;
import phdev.com.br.metafighter.cmp.event.Event;
import phdev.com.br.metafighter.cmp.event.listeners.ActionListener;
import phdev.com.br.metafighter.cmp.event.listeners.EventListener;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.Button;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.window.Text;
import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.cmp.event.animation.GoAndBack;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MainScreen extends Screen {

    private Texture buttonTexture;
    private Texture mainBackgroundTexture;

    private BackGround mainBackGround;
    private Button singleplayerButton;
    private Button multiplayerButton;
    private Button optionsButton;

    public MainScreen(EventListener listener) {
        super(listener);
    }

    @Override
    protected boolean loadTextures() {

        this.buttonTexture = new Texture("images/buttons/button1.png");

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
        float marginX = (screenSize.width()/4)/8;
        float marginY = (screenSize.height()/8)/2;

        this.mainBackGround = new BackGround(screenSize, Color.rgb(0, 153, 0));

        RectF buttonSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);

        float fontSize = Text.adaptText(new String[]{"Multijogador"}, buttonSize);

        ButtonHandler buttonHandler = new ButtonHandler();

        this.singleplayerButton = new Button(
                new RectF( screenSize.centerX() - marginX - buttonSize.width(),
                        screenSize.centerY() - marginY - buttonSize.height(),
                        screenSize.centerX() - marginX,
                        screenSize.centerY() - marginY),
                "Um jogador", this.buttonTexture);
        this.singleplayerButton.getText().setTextSize(fontSize);
        this.singleplayerButton.addEventListener(buttonHandler);
        this.singleplayerButton.addAnimationListener(new GoAndBack(this.singleplayerButton));
        this.singleplayerButton.setId(0);

        this.multiplayerButton = new Button(
                new RectF( screenSize.centerX() + marginX,
                        screenSize.centerY() - marginY - buttonSize.height(),
                        screenSize.centerX() + marginX + buttonSize.width(),
                        screenSize.centerY() - marginY),
                "Multijogador", this.buttonTexture);
        this.multiplayerButton.getText().setTextSize(fontSize);
        this.multiplayerButton.addEventListener(buttonHandler);
        this.multiplayerButton.addAnimationListener(new GoAndBack(this.multiplayerButton));
        this.multiplayerButton.setId(1);

        this.optionsButton = new Button(
                new RectF( screenSize.centerX() - (buttonSize.width()/2),
                        screenSize.centerY() + marginY,
                        screenSize.centerX() + (buttonSize.width()/2),
                        screenSize.centerY() + marginY + buttonSize.height()),
                "Opções", this.buttonTexture);
        this.optionsButton.getText().setTextSize(fontSize);
        this.optionsButton.addEventListener(buttonHandler);
        this.optionsButton.addAnimationListener(new GoAndBack(this.optionsButton));
        this.optionsButton.setId(2);

        add(mainBackGround);
        add(singleplayerButton);
        add(multiplayerButton);
        add(optionsButton);

        return true;
    }

    public class ButtonHandler implements ActionListener{

        @Override
        public void actionPerformed(Event event) {
            switch (((ClickEvent)event).id){
                case 0:
                    new SelectCharacterScreen(listener);
                    break;
                case 1:
                    new MultiplayerSelectScreen(listener);
                    break;
                case 2:
                    sendMessageToScreen("Ainda não implementado");
                    break;
            }
        }
    }
}
