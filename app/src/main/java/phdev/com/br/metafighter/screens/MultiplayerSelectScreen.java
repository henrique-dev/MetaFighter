package phdev.com.br.metafighter.screens;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.Log;

import phdev.com.br.metafighter.BluetoothManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.event.ClickEvent;
import phdev.com.br.metafighter.cmp.event.ClickListener;
import phdev.com.br.metafighter.cmp.event.animation.GoAndBack;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.Button;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.window.Text;
import phdev.com.br.metafighter.cmp.window.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MultiplayerSelectScreen extends Screen {

    private BluetoothManager bluetoothManager;

    private Texture buttonTexture;
    private Texture mainBackgroundTexture;

    private BackGround mainBackGround;
    private Button hostButton;
    private Button joinButton;
    private Button backButton;

    public MultiplayerSelectScreen(){
        super();

        BluetoothManager.getInstance().activate();
        bluetoothManager = BluetoothManager.getInstance();

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

        this.mainBackGround = new BackGround(screenSize, Color.rgb(76, 0, 153));

        RectF buttonSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);

        float fontSize = Text.adaptText(new String[]{"Entrar em uma partida"}, buttonSize);

        ButtonHandler buttonHandler = new ButtonHandler();

        this.hostButton = new Button(
                new RectF( screenSize.centerX() - buttonSize.width()/2,
                        screenSize.centerY() - buttonSize.height()/2 - marginY - buttonSize.height(),
                        screenSize.centerX() + buttonSize.width()/2,
                        screenSize.centerY() - buttonSize.height()/2 - marginY),
                "Criar partida", this.buttonTexture);
        this.hostButton.getText().setTextSize(fontSize);
        this.hostButton.addEventListener(buttonHandler);
        this.hostButton.addAnimationListener(new GoAndBack(this.hostButton));
        this.hostButton.setId(0);

        this.joinButton = new Button(
                new RectF( screenSize.centerX() - buttonSize.width()/2,
                        screenSize.centerY() - buttonSize.height()/2,
                        screenSize.centerX() + buttonSize.width()/2,
                        screenSize.centerY() + buttonSize.height()/2),
                "Entrar em uma partida", this.buttonTexture);
        this.joinButton.getText().setTextSize(fontSize);
        this.joinButton.addEventListener(buttonHandler);
        this.joinButton.addAnimationListener(new GoAndBack(this.joinButton));
        this.joinButton.setId(1);

        this.backButton = new Button(
                new RectF( screenSize.centerX() - buttonSize.width()/2,
                        screenSize.centerY() + buttonSize.height()/2 + marginY,
                        screenSize.centerX() + buttonSize.width()/2,
                        screenSize.centerY() + buttonSize.height()/2 + marginY + buttonSize.height()),
                "Voltar", this.buttonTexture);
        this.backButton.getText().setTextSize(fontSize);
        this.backButton.addEventListener(buttonHandler);
        this.backButton.addAnimationListener(new GoAndBack(this.backButton));
        this.backButton.setId(2);

        add(mainBackGround);
        add(hostButton);
        add(joinButton);
        add(backButton);

        return true;
    }

    public class ButtonHandler implements ClickListener {

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
                    if (!bluetoothManager.isEnabled())
                        Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": O bluetooth deve estar ativado para proseeguir.");
                    else
                        new MultiplayerHostScreen();
                    break;
                case 1:
                    if (!bluetoothManager.isEnabled()) {
                            Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": O bluetooth deve estar ativado para proseeguir.");
                    }
                    else {
                        new MultiplayerJoinScreen();
                    }

                    break;
                case 2:
                    new MainScreen();
                    break;
            }

            return true;
        }
    }
}
