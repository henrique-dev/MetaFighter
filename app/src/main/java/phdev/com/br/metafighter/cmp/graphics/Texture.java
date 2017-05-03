package phdev.com.br.metafighter.cmp.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import phdev.com.br.metafighter.GameParameters;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Texture {

    final static int bitmapQuality = 100;

    private Bitmap image;

    public Texture(String path){
        this.image = openImage(path, -1, -1);
        GameParameters.getInstance().log("          Textura criada: Tamanho usado para alocar: " + this.sizeOf()/1000 + "kB");
    }

    public Texture(String path, int recWidth, int recHeight){
        this.image = openImage(path, recWidth, recHeight);
        GameParameters.getInstance().log("          Textura criada: Tamanho usado para alocar: " + this.sizeOf()/1000 + "kB");
    }

    public Texture(Bitmap image){
        this.image = Bitmap.createBitmap(image);
        GameParameters.getInstance().log("          Textura criada: Tamanho usado para alocar: " + this.sizeOf()/1000 + "kB");
    }

    public Texture(Texture texture){
        this.image = Bitmap.createBitmap(texture.getImage());
        GameParameters.getInstance().log("          Textura criada. Tamanho usado para alocar: " + this.sizeOf()/1000 + "kB / Usado construtor de cópia.");
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public static Bitmap openImage(String path, int recWidth, int reqHeight){

        try{
            ByteArrayOutputStream finalBuffer = new ByteArrayOutputStream();
            InputStream buffer = GameParameters.getInstance().assetManager.open(path);
            int data = buffer.read();
            while ( data != -1){
                finalBuffer.write(data);
                data = buffer.read();
            }

            if(recWidth != 1 && reqHeight != -1){
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(finalBuffer.toByteArray(), 0, finalBuffer.size(), options);
                options.inSampleSize = calculateInSampleSize(options, recWidth, reqHeight);
                options.inJustDecodeBounds = false;

                ByteArrayOutputStream imagePosCompress = new ByteArrayOutputStream();

                if((BitmapFactory.decodeByteArray(finalBuffer.toByteArray(), 0, finalBuffer.size(), options)).compress(Bitmap.CompressFormat.JPEG, bitmapQuality, imagePosCompress))
                    return BitmapFactory.decodeByteArray(imagePosCompress.toByteArray(), 0, imagePosCompress.size());
            }
            else
                return BitmapFactory.decodeByteArray(finalBuffer.toByteArray(), 0, finalBuffer.size());


            //return (BitmapFactory.decodeByteArray(finalBuffer.toByteArray(), 0, finalBuffer.size(), options));

        }
        catch (IOException e){
            Log.e("GameEngine", e.getMessage());
            Log.e("GameEngine", e.getCause().toString());
            e.printStackTrace();
        }

        return null;
    }

    public Bitmap clipImage(int x, int y, int width, int height){
        Bitmap imageClip = this.image;
        try{
            return Bitmap.createBitmap(imageClip, x, y, width, height);
        }
        catch (Exception e){
            Log.e("GameEngine", e.getMessage());
            Log.e("GameEngine", e.getCause().toString());
            e.printStackTrace();
        }
        return null;
    }

    public void scaleImage(int width, int height){
        this.image = Bitmap.createScaledBitmap(this.image, width, height, false);
        try{
            Bitmap.createBitmap(null);
        }
        catch (Exception e){

        }

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){

        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if( height > reqHeight || width > reqWidth){

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public int sizeOf(){

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return this.image.getByteCount();
        return this.image.getAllocationByteCount();
    }

}
