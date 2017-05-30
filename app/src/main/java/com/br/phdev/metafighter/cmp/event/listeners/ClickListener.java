package com.br.phdev.metafighter.cmp.event.listeners;

import com.br.phdev.metafighter.cmp.event.ClickEvent;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface ClickListener extends ActionListener {

    void pressedPerformed(ClickEvent event);
    void releasedPerformed(ClickEvent event);

}
