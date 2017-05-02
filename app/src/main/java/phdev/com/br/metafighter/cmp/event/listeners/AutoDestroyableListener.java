package phdev.com.br.metafighter.cmp.event.listeners;

import phdev.com.br.metafighter.cmp.WindowEntity;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface AutoDestroyableListener extends EventListener{
    void autoDestroy(WindowEntity entity);
}
