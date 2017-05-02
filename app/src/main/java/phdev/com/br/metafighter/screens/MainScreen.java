package phdev.com.br.metafighter.screens;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.UUID;

import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.Entity;
import phdev.com.br.metafighter.cmp.event.ClickEvent;
import phdev.com.br.metafighter.cmp.event.ClickListener;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.Button;
import phdev.com.br.metafighter.cmp.window.Label;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.window.Table;
import phdev.com.br.metafighter.cmp.window.TableItem;
import phdev.com.br.metafighter.cmp.window.Text;
import phdev.com.br.metafighter.cmp.window.graphics.Texture;
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

    public MainScreen(){
        super();

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

    public class ButtonHandler implements ClickListener{

        @Override
        public boolean pressedPerformed(ClickEvent event) {
            return true;
        }

        @Override
        public boolean releasedPerformed(ClickEvent event) {
            return true;
        }

        @Override
        public boolean executePerformed(ClickEvent event) {

            switch (event.id){
                case 0:
                    break;
                case 1:
                    new MultiplayerSelectScreen();
                    break;
                case 2:
                    break;
            }

            return true;
        }
    }
}
