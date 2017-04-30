package phdev.com.br.metafighter.screens;

import android.graphics.RectF;
import android.util.Log;

import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.listeners.ActionListener;
import phdev.com.br.metafighter.listeners.Event;
import phdev.com.br.metafighter.models.BackGround;
import phdev.com.br.metafighter.models.Label;
import phdev.com.br.metafighter.models.Screen;
import phdev.com.br.metafighter.models.Text;
import phdev.com.br.metafighter.models.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MainScreen extends Screen {

    public MainScreen(){
        super();

        Texture textureBackground = new Texture("images/backgrounds/background1.png");
        Texture textureLabel = new Texture("images/labels/label2.png");
        BackGround backGround = new BackGround(GameParameters.getInstance().screenSize, textureBackground);
        Label label = new Label(new RectF(20,20,500,200), "Novo label", textureLabel);
        label.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(Event event) {
                Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": O label foi clicado");
            }
        });



        add(backGround);
        add(label);


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
}
