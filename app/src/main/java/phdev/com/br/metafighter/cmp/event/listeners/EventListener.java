package phdev.com.br.metafighter.cmp.event.listeners;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */

public interface EventListener {

    int ACTION = 0;
    int CLICK = 1;

    int ANIMATION = 2;
    int ANIMATION_GB_GO = 9;
    int ANIMATION_GB_BACK = 10;
    int ANIMATION_GB_GO_AND_BACK = 11;
    int ANIMATION_SL_SELECT = 12;
    int ANIMATION_SL_DISSELECT = 13;

    int INTENT = 3;
    int CONTROLLER = 4;
    int MESSAGE = 5;
    int PROGRESS = 6;
    int SCREEN_UPDATE = 7;
    int AUTO_DESTROYABLE = 8;

}
