package com.br.phdev.metafighter.cmp.event.listeners;

import com.br.phdev.metafighter.cmp.event.ClickEvent;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface PressingListener extends ClickListener {

    void pressingPerformed(ClickEvent evt);

}
