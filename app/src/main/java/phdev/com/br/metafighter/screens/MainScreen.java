package phdev.com.br.metafighter.screens;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.event.ClickEvent;
import phdev.com.br.metafighter.cmp.event.ClickListener;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.Button;
import phdev.com.br.metafighter.cmp.window.Label;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.window.Table;
import phdev.com.br.metafighter.cmp.window.TableItem;
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

        this.mainBackgroundTexture = new Texture("images/backgrounds/background1.png");
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

        this.singleplayerButton = new Button(
                new RectF( screenSize.centerX() - marginX - buttonSize.width(),
                        screenSize.centerY() - marginY - buttonSize.height(),
                        screenSize.centerX() - marginX,
                        screenSize.centerY() - marginY),
                "Um jogador", this.buttonTexture);

        this.multiplayerButton = new Button(
                new RectF( screenSize.centerX() + marginX,
                        screenSize.centerY() - marginY - buttonSize.height(),
                        screenSize.centerX() + marginX + buttonSize.width(),
                        screenSize.centerY() - marginY),
                "VS", this.buttonTexture);

        this.optionsButton = new Button(
                new RectF( screenSize.centerX() - (buttonSize.width()/2),
                        screenSize.centerY() + marginY,
                        screenSize.centerX() + (buttonSize.width()/2),
                        screenSize.centerY() + marginY + buttonSize.height()),
                "Opções", this.buttonTexture);

        add(mainBackGround);
        add(singleplayerButton);
        add(multiplayerButton);
        add(optionsButton);

        return true;
    }
}
