package phdev.com.br.metafighter.cmp.connections.packets;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Request implements Packet {

    private int request;
    private float value1;
    private float value2;
    private float value3;

    public Request(int code, float value1, float value2, float value3){
        this.request = code;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }

    public float getValue1() {
        return value1;
    }

    public void setValue1(float value1) {
        this.value1 = value1;
    }

    public float getValue2() {
        return value2;
    }

    public void setValue2(float value2) {
        this.value2 = value2;
    }

    public float getValue3() {
        return value3;
    }

    public void setValue3(float value3) {
        this.value3 = value3;
    }
}
