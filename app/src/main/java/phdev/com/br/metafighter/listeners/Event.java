package phdev.com.br.metafighter.listeners;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Event {

    private List<Object> objs;

    public Event(){
        objs = new ArrayList<>();
    }

    public Event(List<Object> objs){
        this.objs = objs;
    }

    public List<Object> getObjs() {
        return objs;
    }

    public void setObjs(List<Object> objs) {
        this.objs = objs;
    }

}
