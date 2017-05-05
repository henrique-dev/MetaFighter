package phdev.com.br.metafighter.cmp.event.listeners;

import phdev.com.br.metafighter.cmp.event.Event;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface ControllerListener extends EventListener{

    void upPerformed(Event evt);
    void downPerformed(Event evt);
    void leftPerformed(Event evt);
    void rightPerformed(Event evt);
    void action1Performed(Event evt);
    void action2Performed(Event evt);
    void action3Performed(Event evt);
    void action4Performed(Event evt);

}
