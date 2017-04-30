package phdev.com.br.metafighter.models;

import android.graphics.Typeface;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class TyperfaceFont extends Font {

    private Typeface font;

    public TyperfaceFont(String name, Typeface font){
        super(name);
        this.font = font;
    }

    public Typeface getFont() {
        return font;
    }

}
