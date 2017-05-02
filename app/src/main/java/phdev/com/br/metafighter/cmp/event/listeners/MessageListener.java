package phdev.com.br.metafighter.cmp.event.listeners;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface MessageListener extends EventListener {

    void sendMessage(String msg, int duration);

}
