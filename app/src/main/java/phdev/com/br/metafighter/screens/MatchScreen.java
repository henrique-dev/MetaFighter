package phdev.com.br.metafighter.screens;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.Random;

import phdev.com.br.metafighter.BluetoothManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.window.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MatchScreen extends Screen {

    private BackGround backGround;

    public MatchScreen(){
        super();
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
        this.backGround = new BackGround(screenSize, Color.YELLOW);

        add(backGround);

        return true;
    }

    @Override
    public void update(){
        if (BluetoothManager.getInstance().data != -1){
            Random rand = new Random();
            backGround.getPaint().setColor(Color.rgb( rand.nextInt(254), rand.nextInt(254), rand.nextInt(254) ));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = event.getActionMasked();

        if (action == MotionEvent.ACTION_DOWN){
            //Random rand = new Random();
            //backGround.getPaint().setColor(Color.rgb( rand.nextInt(254), rand.nextInt(254), rand.nextInt(254) ));

            String teste = "Teste";

            BluetoothManager.getInstance().write(teste.getBytes());

        }

        return true;
    }
}
