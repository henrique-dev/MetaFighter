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

    private String name;
    private Sprite[] view;

    public Character(RectF area, Paint paint, Sprite[] sprites, Sprite[] view, String name) {
        super(area, paint, sprites);
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
