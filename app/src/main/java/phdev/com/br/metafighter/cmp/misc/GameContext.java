package phdev.com.br.metafighter.cmp.misc;

import android.content.Context;
import android.content.Intent;

import phdev.com.br.metafighter.ConnectionManager;
import phdev.com.br.metafighter.SoundManager;
import phdev.com.br.metafighter.cmp.window.LoadingScreen;
import phdev.com.br.metafighter.cmp.window.Screen;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public abstract class GameContext {

    public void sendMessage(String msg, int duration) {}

    public LoadingScreen getProgressCmp(){
        return null;
    }

    public void sendIntentRequest(Intent intent) {}

    public ConnectionManager getConnectionType() {
        return null;
    }

    public void screenUpdate(Screen screen) {}

    public SoundManager getSoundManager(){
        return null;
    }

    public Context getAppContetxt(){
        return null;
    }
}
