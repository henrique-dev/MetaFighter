package phdev.com.br.metafighter.cmp.event;

import android.content.Intent;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface MessageListener extends EventListener {

    void sendToast(String msg, int duration);

}
