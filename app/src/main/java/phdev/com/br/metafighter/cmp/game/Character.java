package phdev.com.br.metafighter.cmp.game;

import android.graphics.Paint;
import android.graphics.RectF;

import phdev.com.br.metafighter.cmp.GameEntity;
import phdev.com.br.metafighter.cmp.graphics.Sprite;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Character extends GameEntity {

    public static final int TESTE = -1;
    public static final int GUEDES = 0;
    public static final int KAILA = 1;
    public static final int LUIZ = 2;
    public static final int PATRICIA = 3;
    public static final int QUELE = 4;
    public static final int ROMULO = 5;

    private String name;
    private Sprite[] view;

    public Character(Sprite[] sprites, Sprite[] view, String name) {
        super(null, null, sprites);
        this.name = name;
        this.view = view;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sprite[] getView() {
        return view;
    }

    public void setView(Sprite[] view) {
        this.view = view;
    }
}
