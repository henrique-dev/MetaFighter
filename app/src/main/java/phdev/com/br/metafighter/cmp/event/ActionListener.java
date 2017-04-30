package phdev.com.br.metafighter.cmp.event;

import phdev.com.br.metafighter.cmp.event.Event;
import phdev.com.br.metafighter.cmp.event.EventListener;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface ActionListener extends EventListener {

    void actionPerformed(Event event);

}
