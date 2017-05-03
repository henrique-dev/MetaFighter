package phdev.com.br.metafighter.cmp.window;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.WindowEntity;
import phdev.com.br.metafighter.cmp.event.ClickEvent;
import phdev.com.br.metafighter.cmp.event.Event;
import phdev.com.br.metafighter.cmp.event.listeners.ClickListener;
import phdev.com.br.metafighter.cmp.event.animation.GoAndBack;
import phdev.com.br.metafighter.cmp.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Table extends WindowEntity{

    private Text textHead;
    private RectF areaHead;
    protected RectF areaShow;
    private RectF areaBody;
    private RectF areaItem;
    private Texture textureHead;
    private Texture textureBody;
    private Texture textureShow;
    protected Texture textureItem;
    private List<TableItem> items;

    public Table(RectF area, Paint paint, Texture textureBody, Texture textureHead, Texture textureShow, Texture textureItem, String tableName) {
        super(area, paint, null);
        this.areaBody = super.getArea();
        this.textureBody = textureBody;
        this.textureHead = textureHead;
        this.textureShow = textureShow;
        this.textureItem = textureItem;
        this.textHead = new Text(tableName);

        float marginHead = this.areaBody.width()/8;
        this.areaHead = new RectF( this.areaBody.left + marginHead,
                this.areaBody.top - marginHead,
                this.areaBody.right - marginHead,
                this.areaBody.top + marginHead);

        marginHead /= 3;
        this.areaShow = new RectF( this.areaBody.left + marginHead,
                this.areaHead.bottom + marginHead,
                this.areaBody.right - marginHead,
                this.areaBody.bottom - marginHead);

        this.areaItem = new RectF( this.areaShow.left,
                this.areaShow.top,
                this.areaShow.right,
                this.areaShow.top + this.areaShow.height()/3);

        this.textHead.setDrawableArea(this.areaHead);

        this.textureBody.scaleImage((int)this.areaBody.width(), (int)this.areaBody.height());
        this.textureHead.scaleImage((int)this.areaHead.width(), (int)this.areaHead.height());
        this.textureShow.scaleImage((int)this.areaShow.width(), (int)this.areaShow.height());
        this.textureItem.scaleImage((int)this.areaItem.width(), (int)this.areaItem.height());

        this.items = new ArrayList<>();
    }

    public RectF getAreaHead() {
        return areaHead;
    }

    public void setAreaHead(RectF areaHead) {
        this.areaHead = areaHead;
    }

    public RectF getAreaShow() {
        return areaShow;
    }

    public void setAreaShow(RectF areaShow) {
        this.areaShow = areaShow;
    }

    public RectF getAreaBody() {
        return areaBody;
    }

    public void setAreaBody(RectF areaBody) {
        this.areaBody = areaBody;
    }

    public RectF getAreaItem() {
        return areaItem;
    }

    public void setAreaItem(RectF areaItem) {
        this.areaItem = areaItem;
    }

    public Text getTextHead() {
        return textHead;
    }

    public void setTextHead(Text textHead) {
        this.textHead = textHead;
    }

    public void addItem(String text){
        final TableItem item = new TableItem(text);

        item.setTexture(this.textureItem);
        item.setArea( new RectF(this.areaItem.left,
                this.areaShow.top + (this.areaItem.height() * this.items.size()),
                this.areaItem.right,
                this.areaShow.top + (this.areaItem.height() * (this.items.size()+1))));

        item.addEventListener(new ClickListener() {
            @Override
            public void actionPerformed(Event event) {
                Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": executou o " + item.getText());
            }

            @Override
            public boolean pressedPerformed(ClickEvent event) {
                Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Clicou.");
                return true;
            }

            @Override
            public boolean releasedPerformed(ClickEvent event) {
                Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Soltou.");
                return true;
            }
        });
        item.addAnimationListener(new GoAndBack(item));
        this.items.add(item);
    }

    public void addItem(TableItem item){
        //TableItem tableItem = new TableItem(item.getText());
        item.setArea( new RectF(this.areaItem.left,
                        this.areaShow.top + (this.areaItem.height() * this.items.size()),
                        this.areaItem.right,
                        this.areaShow.top + (this.areaItem.height() * (this.items.size()+1))));
        item.setTexture(this.textureItem);
        this.items.add(item);
    }

    public void removerItem(TableItem item){
        this.items.remove(item);
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(this.textureBody.getImage(), this.areaBody.left, this.areaBody.top, super.paint);
        canvas.drawBitmap(this.textureHead.getImage(), this.areaHead.left, this.areaHead.top, super.paint);
        canvas.drawBitmap(this.textureShow.getImage(), this.areaShow.left, this.areaShow.top, super.paint);
        this.textHead.draw(canvas);

        canvas.clipRect(this.areaShow);

        for (TableItem item : items){
            item.draw(canvas);
        }

        canvas.restore();
    }

    private float startY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getActionMasked();
        float x = event.getX();
        float y = event.getY();

        if (checkCollision(new RectF(x,y,x,y), this.areaShow)){
            if (items != null){
                if (items.size() > 3) {

                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            this.startY = y;
                            break;
                        case MotionEvent.ACTION_UP:
                            this.startY = -1;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (!(items.get(0).getArea().top + (y - startY) > this.areaShow.top)
                                    && !(items.get(items.size() - 1).getArea().bottom + (y - startY) < this.areaShow.bottom))
                                for (TableItem item : items) {
                                    item.move(0, y - startY);
                                }
                            this.startY = y;
                            break;
                    }
                }
                for (TableItem item : items){
                    item.onTouchEvent(event);
                }
            }
        }

        return true;
    }

}
