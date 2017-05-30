package com.br.phdev.metafighter.cmp.misc;
import android.content.Context;
import android.content.Intent;

import com.br.phdev.metafighter.ConnectionManager;
import com.br.phdev.metafighter.SoundManager;
import com.br.phdev.metafighter.cmp.window.LoadingScreen;
import com.br.phdev.metafighter.cmp.window.Screen;

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
