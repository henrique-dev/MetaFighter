package phdev.com.br.metafighter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public final class GameActivity extends Activity{

    private final int LANDSCAPE = 0;
    private final int PORTRAIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.setupParameters();

        /*

        Log.d("GameEngine", "onCreate: " + getPackageName());

        String[] tmp = getAPKExpansionFiles(this, 4, 0);

        File file = new File(tmp[0]);

        if (file.exists())
            Log.e("GameEngine", "O arquivo existe");

        for (String s : tmp)
            Log.e("GameEngine", s);

        try {
            ZipResourceFile zipfile = APKExpansionSupport.getResourceZipFile(tmp);

            if (zipfile!=null)
                Log.e("GameEngine", "onCreate: " + "Arquivo OK");

            InputStream is = zipfile.getInputStream("characters/guedes/view.png");

            Bitmap image = BitmapFactory.decodeStream(is);

            Log.e("GameEngine", "onCreate: " + image.getWidth() + "x" + image.getHeight());

        } catch (IOException e) {
            Log.e("GameEngine", "onCreate: " + e.getMessage());
        }

        */

        super.setContentView(new GameEngine(this));

    }

    // The shared path to all app expansion files
    private final static String EXP_PATH = "/Android/obb/";

    static String[] getAPKExpansionFiles(Context ctx, int mainVersion,
                                         int patchVersion) {
        String packageName = ctx.getPackageName();
        Vector<String> ret = new Vector<String>();
        if (Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)) {
            // Build the full path to the app's expansion files
            File root = Environment.getExternalStorageDirectory();
            File expPath = new File(root.toString() + EXP_PATH + packageName);

            // Check that expansion file path exists
            if (expPath.exists()) {
                if ( mainVersion > 0 ) {
                    String strMainPath = expPath + File.separator + "main." +
                            mainVersion + "." + packageName + ".obb";
                    File main = new File(strMainPath);
                    if ( main.isFile() ) {
                        ret.add(strMainPath);
                    }
                }
                if ( patchVersion > 0 ) {
                    String strPatchPath = expPath + File.separator + "patch." +
                            mainVersion + "." + packageName + ".obb";
                    File main = new File(strPatchPath);
                    if ( main.isFile() ) {
                        ret.add(strPatchPath);
                    }
                }
            }
        }
        String[] retArray = new String[ret.size()];
        ret.toArray(retArray);
        return retArray;
    }

    private void setupParameters() {

        GameParameters.getInstance().orientation = LANDSCAPE;
        GameParameters.getInstance().assetManager = getAssets();

        this.setRequestedOrientation(GameParameters.getInstance().orientation);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        GameParameters.getInstance().screenSize = new RectF(0, 0, size.x, size.y);
        GameParameters.getInstance().screenWidth = size.x;
        GameParameters.getInstance().screenHeight = size.y;

        GameParameters.getInstance().fontSizeButton = 20;

    }

}
