package phdev.com.br.metafighter.cmp.event.listeners;

import phdev.com.br.metafighter.cmp.window.Screen;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface ScreenUpdateListener extends EventListener{

    void screenUpdate(Screen screen);

}
