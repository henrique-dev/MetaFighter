package phdev.com.br.metafighter.screens;

import android.graphics.Color;
import android.graphics.RectF;

import phdev.com.br.metafighter.BluetoothManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.event.ClickEvent;
import phdev.com.br.metafighter.cmp.event.listeners.ClickListener;
import phdev.com.br.metafighter.cmp.event.listeners.EventListener;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.Button;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.window.Text;
import phdev.com.br.metafighter.cmp.window.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MultiplayerHostScreen extends Screen {

    private BluetoothManager manager;
    private BackGround backGround;
    private Button backButton;
    private Texture backButtonTexture;

    public MultiplayerHostScreen(EventListener listener, BluetoothManager manager) {
        super(listener);
        this.manager = manager;
        manager.start();
    }

    @Override
    protected boolean loadTextures() {
        this.backButtonTexture = new Texture("images/buttons/button1.png");
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
        this.backGround = new BackGround(screenSize, Color.rgb(204,102,0));

        RectF buttonSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);

        float fontSize = Text.adaptText(new String[]{"Voltar"}, buttonSize);

        this.backButton = new Button(
                new RectF(screenSize.right - buttonSize.width(),
                        screenSize.bottom - buttonSize.height(),
                        screenSize.right, screenSize.bottom),
                "Voltar", backButtonTexture);
        this.backButton.addEventListener(new ClickListener() {
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
                manager.stop();
                new MultiplayerSelectScreen(listener);
                return true;
            }
        });
        this.backButton.getText().setTextSize(fontSize);

        add(backGround);
        add(backButton);

        return true;
    }
}
