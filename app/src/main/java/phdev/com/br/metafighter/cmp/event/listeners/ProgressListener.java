package phdev.com.br.metafighter.cmp.event.listeners;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface ProgressListener extends EventListener{

    void progressUpdate(int value);
    void progressPrepare();
    void progressFinalize();

}
