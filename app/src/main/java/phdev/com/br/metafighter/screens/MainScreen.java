package phdev.com.br.metafighter.screens;

import android.graphics.RectF;
import android.util.Log;

import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.event.AnimationListener;
import phdev.com.br.metafighter.cmp.event.ClickEvent;
import phdev.com.br.metafighter.cmp.event.ClickListener;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.Label;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.window.graphics.Texture;
import phdev.com.br.metafighter.cmp.event.animation.GoAndBack;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MainScreen extends Screen {

    private BackGround backGround;
    private Label label;

    public MainScreen(){
        super();

        Texture textureBackground = new Texture("images/backgrounds/background1.png");
        Texture textureLabel = new Texture("images/labels/label2.png");
        backGround = new BackGround(GameParameters.getInstance().screenSize, textureBackground);
        label = new Label(new RectF(20,20,500,200), "Novo label", textureLabel);
        label.addListener(new ClickListener() {
            @Override
            public boolean pressedPerformed(ClickEvent event) {
                Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Clicou");
                //label.move(5,5);
                return true;
            }
            @Override
            public boolean releasedPerformed(ClickEvent event) {

                Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Soltou");
                //label.move(-5, -5);

                return true;
            }

            @Override
            public boolean executePerformed(ClickEvent event) {
                Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": executando ação");
                return true;
            }
        });

        label.addAnimationListener(new GoAndBack(label));

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
