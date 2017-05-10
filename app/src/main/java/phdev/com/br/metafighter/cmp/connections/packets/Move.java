package phdev.com.br.metafighter.cmp.connections.packets;

import java.io.Serializable;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Move implements Serializable, Packet {

    private int sendtoid;
    private float x;
    private float y;

    public Move(int sendtoid, float x, float y){
        this.sendtoid = sendtoid;
        this.x = x;
        this.y = y;
    }

    public int getSendtoid() {
        return sendtoid;
    }

    public void setSendtoid(int sendtoid) {
        this.sendtoid = sendtoid;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
