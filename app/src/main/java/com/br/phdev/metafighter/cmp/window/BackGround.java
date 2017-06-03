package com.br.phdev.metafighter.cmp.window;

import android.graphics.Paint;
import android.graphics.RectF;

import com.br.phdev.metafighter.cmp.WindowEntity;
import com.br.phdev.metafighter.cmp.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class BackGround extends WindowEntity {

    public BackGround(RectF rect, int color){
        super(rect, new Paint(), true);
        paint.setColor(color);
    }

    public BackGround(RectF rect, Paint paint, boolean showArea){
        super(rect, paint, showArea);
    }

    public BackGround(RectF rect, Texture texture){
        super(rect, new Paint(), texture);
        super.texture.scaleImage((int)rect.width(), (int)rect.height());
    }

    protected BackGround(BackGround backGround){
        super(backGround.getArea(), backGround.getPaint(), backGround.getTexture());
    }

}
