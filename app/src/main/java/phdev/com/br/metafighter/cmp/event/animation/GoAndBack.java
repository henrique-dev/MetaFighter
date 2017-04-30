package phdev.com.br.metafighter.cmp.event.animation;

import phdev.com.br.metafighter.cmp.Entity;
import phdev.com.br.metafighter.cmp.event.AnimationListener;
import phdev.com.br.metafighter.cmp.event.EventListener;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class GoAndBack implements AnimationListener {

    private Entity entity;

    public GoAndBack(Entity entity){
        this.entity = entity;
    }

    public void go(){
        this.entity.move(5,5);
    }

    public void back(){
        this.entity.move(-5,-5);
    }


}
