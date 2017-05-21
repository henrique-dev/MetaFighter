package phdev.com.br.metafighter.cmp.game;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;

import phdev.com.br.metafighter.cmp.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Collision {

    private boolean active;
    private RectF[] collision;

    public Collision(RectF[] collision){
        this.collision = collision;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public RectF[] getCollision() {
        return collision;
    }

    public void setCollision(RectF[] collision) {
        this.collision = collision;
    }

    public static RectF[] detectCollisionFromTexture(Texture texture, int numberLines, int numberColumns, final int percent){

        class Box {

            private int numberPixels;
            private int counter = 0;

            public Box(int numberPixels){
                this.numberPixels = numberPixels;
            }

            public void set(int pixel){
                if (pixel != 0)
                    counter++;
            }

            public boolean getResult(){
                return counter > ((percent * numberPixels) / 100);
            }

            public void reset(){
                counter = 0;
            }
        }

        Bitmap tmpBitmap = texture.getImage();
        int imageHeight = tmpBitmap.getHeight();
        int imageWidth = tmpBitmap.getWidth();

        int divWidth = imageWidth / numberColumns;
        int divHeight = imageHeight / numberLines;

        RectF[] boxs = new RectF[numberLines * numberColumns];
        int counter = 0;

        Box box = new Box(divWidth * divHeight);

        for (int i=0; i<numberColumns; i++){
            for (int j=0; j<numberLines; j++){
                for (int k=i*divWidth; k<((i+1)*divWidth); k++){
                    for (int l=j*divHeight; l<((j+1)*divHeight); l++){

                        try{
                            box.set(tmpBitmap.getPixel(k,l));
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
                //boxs[++counter] = new RectF(i*divWidth,j*divHeight,(i+1)*divWidth, (j+1)*divHeight);

                if (box.getResult()) {
                    boxs[counter++] = new RectF(i*divWidth,j*divHeight,(i+1)*divWidth, (j+1)*divHeight);
                }
                box.reset();
            }
        }

        //Log.d("GameEngine", "detectCollisionFromTexture: " + counter);

        RectF[] copyBoxs = new RectF[counter];
        System.arraycopy(boxs, 0, copyBoxs, 0, counter);
        return copyBoxs;

    }

}
