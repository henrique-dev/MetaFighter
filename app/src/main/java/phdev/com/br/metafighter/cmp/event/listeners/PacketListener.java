package phdev.com.br.metafighter.cmp.event.listeners;

import phdev.com.br.metafighter.connections.packets.Move;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public interface PacketListener {

    void send(Move object);
    Move receive();

}
