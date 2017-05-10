package phdev.com.br.metafighter;

import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.cmp.Component;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public final class ScreenManager {

    private static final ScreenManager instance = new ScreenManager();
    private List<Component> screens;

    public ScreenManager(){
        screens = new ArrayList<>();
    }

    protected ScreenManager getInstance(){
        return instance;
    }

}
