package phdev.com.br.metafighter.cmp.event;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface ClickListener extends EventListener {

    boolean pressedPerformed(ClickEvent event);
    boolean releasedPerformed(ClickEvent event);
    boolean executePerformed(ClickEvent event);

}
