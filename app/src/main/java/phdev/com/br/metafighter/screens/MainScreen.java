package phdev.com.br.metafighter.screens;

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
import phdev.com.br.metafighter.cmp.window.graphics.Texture;
import phdev.com.br.metafighter.cmp.event.animation.GoAndBack;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MainScreen extends Screen {

    private BackGround backGround;
    private Button botao;
    private Table tabela;


    public MainScreen(){
        super();

        Texture textureBackground = new Texture("images/backgrounds/background1.png");
        Texture textureLabel = new Texture("images/labels/label2.png");
        backGround = new BackGround(GameParameters.getInstance().screenSize, textureBackground);
        botao = new Button(new RectF(220,20,700,200), "Novo label", textureLabel);
        botao.addEventListener(new ClickListener() {
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

        tabela = new Table(new RectF(20,80, 500, 480), new Paint(),
                new Texture("cmp/table/body.png"),
                new Texture("cmp/table/head.png"),
                new Texture("cmp/table/show.png"),
                new Texture("cmp/table/item.png"),
                "Tabela");
        tabela.addItem("Item 1");
        tabela.addItem("Item 2");
        tabela.addItem("Item 3");
        tabela.addItem("Item 4");

        botao.addAnimationListener(new GoAndBack(botao));

        add(backGround);
        add(tabela);
        //add(botao);




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
