package phdev.com.br.metafighter.cmp.window;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import phdev.com.br.metafighter.cmp.Component;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class LoadingScreen implements Component {

    private boolean visible;
    private BackGround backGround;
    private ProgressHud progressHud;

    public LoadingScreen(BackGround backGround, ProgressHud progressHud){
        this.backGround = backGround;
        this.progressHud = progressHud;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void increase(int value){
        this.progressHud.increase(value);
    }

    public void start(){
        visible = true;
    }

    public void stop(){
        visible = false;
        progressHud.reset();
    }

    @Override
    public void draw(Canvas canvas) {
        this.backGround.draw(canvas);
        this.progressHud.draw(canvas);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        return false;
    }
}
