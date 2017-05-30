package com.br.phdev.metafighter.cmp.event.listeners;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface AutoDestroyableListener extends EventListener{
    void autoDestroy(Object entity);
}
