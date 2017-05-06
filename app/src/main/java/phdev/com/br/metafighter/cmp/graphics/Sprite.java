package phdev.com.br.metafighter.cmp.graphics;

import android.graphics.Bitmap;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Sprite {

    private Texture texture;

    public Sprite(Texture texture){
        this.texture = texture;
    }

    protected Sprite(Bitmap bitmap){
        this.texture = new Texture(bitmap);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public static Sprite[] getSpriteFromTexture(Texture texture, int numberSpritesLines, int numberSpritesColumns){

        Texture tmpTexture = texture;

        int spriteWidth = texture.getImage().getWidth() / numberSpritesColumns;
        int spriteHeight = texture.getImage().getHeight() / numberSpritesLines;
        Sprite sprites[] = new Sprite[numberSpritesLines*numberSpritesColumns];
        int cont = 0;
        for(int i=0; i<numberSpritesLines; i++){
            for(int j=0; j<numberSpritesColumns; j++){
                //sprites[cont++] = new Sprite(new Texture(Bitmap.createBitmap(texture.getImage())), j*spriteWidth, i*spriteHeight, spriteWidth, spriteHeight);
                sprites[cont++] = new Sprite( new Texture(Bitmap.createBitmap(tmpTexture.getImage())).clipImage(j*spriteWidth, i*spriteHeight, spriteWidth, spriteHeight));
            }
        }

        tmpTexture = null;

        return sprites;
    }
}
