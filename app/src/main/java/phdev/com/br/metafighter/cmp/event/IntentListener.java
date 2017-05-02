package phdev.com.br.metafighter.cmp.event;

import android.content.Intent;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface IntentListener extends EventListener {

    void sendIntentRequest(Intent intent);

}
