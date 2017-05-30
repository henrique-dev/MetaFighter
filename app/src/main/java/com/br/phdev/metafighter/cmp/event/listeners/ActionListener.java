package com.br.phdev.metafighter.cmp.event.listeners;

import com.br.phdev.metafighter.cmp.event.Event;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface ActionListener extends EventListener {

    void actionPerformed(Event event);

}
