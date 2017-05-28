package phdev.com.br.metafighter.cmp;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.event.listeners.EventListener;
import phdev.com.br.metafighter.cmp.connections.packets.Move;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public abstract class Entity implements Component {

    protected int id;
    //protected int[] users = new int[GameParameters.getInstance().maxPlayers];
    protected int user;
    private int counterUsers = 0;

    protected List<EventListener> listeners;
    protected RectF area;
    protected boolean active;
    protected boolean clicked = false;

    public Entity(RectF area){
        this.area = area;
        this.active = true;
        logMessages(this, null);

    }

    public void addUser(int id){
        user = id;
    }

    public int getUser(){
        return user;
    }

    public List<EventListener> getListener() {
        return listeners;
    }

    protected void addEventListener(EventListener listener) {
        if (listeners == null)
            listeners = new ArrayList<>();
        this.listeners.add(listener);
    }

    protected void removeEventListener(EventListener listener){
        if (listeners == null)
            return;
        this.listeners.remove(listener);
    }

    public RectF getArea() {
        return area;
    }

    public void setArea(RectF area) {
        this.area = area;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected void setX(float x){
        float width = this.area.width();
        this.area.left = x;
        this.area.right = x + width;
    }

    protected float getX(){
        return this.area.left;
    }

    protected void setY(float y){
        float height = this.area.height();
        this.area.top = y;
        this.area.bottom = y + height;
    }

    protected float getY(){
        return this.area.top;
    }

    public void processPacket(Move move){
        if (this.id == move.getValue1()){
            this.setX(move.getX());
            this.setY(move.getY());
        }
    }

    public void move(float x, float y){
        this.setX( this.getX() + x);
        this.setY( this.getY() + y);
    }

    @Override
    public void draw(Canvas canvas){

    }

    @Override
    public void update(){

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        return true;
    }


    protected static void logMessages(Object obj, String msg){
        if(!GameParameters.getInstance().debug)
            return;
        if (GameParameters.getInstance().debugCreateObjects)
            GameParameters.getInstance().log("      Objeto criado: " + obj.getClass().getSimpleName() + (msg != null ? msg : ""));
    }


    protected static void log(String msg){
        //if(!GameParameters.getInstance().debug)
          //  return;
        GameParameters.getInstance().log(msg);
    }


    public static boolean checkCollision(RectF A, RectF B){
        if(((A.left >= B.left && A.left <= B.right) && (A.right >= B.left && A.right <= B.right)) &&
                ((A.top >= B.top && A.top <= B.bottom) && (A.bottom >= B.top && A.bottom <= B.bottom)))
            return true;
        return false;
    }
}
