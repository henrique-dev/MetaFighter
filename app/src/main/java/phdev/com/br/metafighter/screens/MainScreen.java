package phdev.com.br.metafighter.screens;

import phdev.com.br.metafighter.models.Screen;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MainScreen extends Screen {

    public MainScreen(){
        super();
    }

    @Override
    protected boolean loadTextures() {
        return true;
    }

    @Override
    protected boolean loadFonts() {
        return true;
    }

    @Override
    protected boolean loadSounds() {
        return true;
    }
}
