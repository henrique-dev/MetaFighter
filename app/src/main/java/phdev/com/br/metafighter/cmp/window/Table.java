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
import phdev.com.br.metafighter.cmp.window.graphics.Texture;

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
    private Texture textureItem;
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

    public void addItem(String text){
        this.items.add(
                new TableItem( new RectF(this.areaItem.left,
                        this.areaShow.top + (this.areaItem.height() * this.items.size()),
                        this.areaItem.right,
                        this.areaShow.top + (this.areaItem.height() * (this.items.size()+1))),
                this.textureItem, text) );
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
            if (items != null)
                if (items.size() > 3){

                    switch (action){
                        case MotionEvent.ACTION_DOWN:
                            this.startY = y;
                            break;
                        case MotionEvent.ACTION_UP:
                            this.startY = -1;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (!(items.get(0).getArea().top + (y-startY) > this.areaShow.top)
                                    && !(items.get(items.size()-1).getArea().bottom + (y-startY) < this.areaShow.bottom))
                                for (TableItem item : items){
                                    item.move(0, y - startY);
                                }
                            Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": " + items.get(0).getArea().top + " - " + this.areaShow.top + " - " + (y - startY));
                            this.startY = y;
                            break;
                    }

                }
        }

        return true;
    }

    public class TableItem extends WindowEntity{

        private Text text;

        public TableItem(RectF area, Texture texture, String text){
            super(area, new Paint(), texture);
            this.text = new Text(text);
            this.text.setDrawableArea(area);
            this.text.setAutosize(true);
        }

        @Override
        public void move(float x, float y){
            super.move(x, y);
            if(text != null){
                this.text.move(x, y);
            }
        }

        @Override
        public void draw(Canvas canvas){
            super.draw(canvas);
            this.text.draw(canvas);
        }

    }
}
