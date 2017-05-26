package phdev.com.br.metafighter.cmp.connections.packets;

import java.io.Serializable;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Action implements Packet, Serializable {

    private int value1;
    private int value2;
    private int value3;
    private int value4;

    public Action(int value1, int value2, int value3, int value4){
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }

    public int getValue3() {
        return value3;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
    }

    public int getValue4() {
        return value4;
    }

    public void setValue4(int value4) {
        this.value4 = value4;
    }
}
