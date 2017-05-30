package com.br.phdev.metafighter.cmp.connections.packets;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Move implements Packet {

    private int value1;
    private float x;
    private float y;

    public Move(int sendtoid, float x, float y){
        this.value1 = sendtoid;
        this.x = x;
        this.y = y;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
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
