package phdev.com.br.metafighter.screens;

import android.graphics.Color;
import android.graphics.RectF;

import phdev.com.br.metafighter.BluetoothManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.Screen;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MultiplayerHostScreen extends Screen {

    private BackGround backGround;

    public MultiplayerHostScreen(){
        BluetoothManager.getInstance().start();
    }

    @Override
    protected boolean loadTextures() {

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

        add(backGround);

        return true;
    }
}
