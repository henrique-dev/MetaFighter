package phdev.com.br.metafighter.cmp.window;

import android.graphics.Paint;
import android.graphics.RectF;

import phdev.com.br.metafighter.cmp.WindowEntity;
import phdev.com.br.metafighter.cmp.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class SelectBox extends WindowEntity {

    private Texture selectedTexture;

    public SelectBox(RectF area, Texture texture) {
        super(area, new Paint(), texture);
    }


}
