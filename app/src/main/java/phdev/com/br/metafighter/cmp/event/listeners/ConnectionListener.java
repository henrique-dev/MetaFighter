package phdev.com.br.metafighter.cmp.event.listeners;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface ConnectionListener {

    void read(byte[] bytes);
    byte[] write();

}
