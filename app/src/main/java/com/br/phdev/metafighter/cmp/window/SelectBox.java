package com.br.phdev.metafighter.cmp.window;

import android.graphics.Paint;
import android.graphics.RectF;

import com.br.phdev.metafighter.cmp.WindowEntity;
import com.br.phdev.metafighter.cmp.graphics.Texture;

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
