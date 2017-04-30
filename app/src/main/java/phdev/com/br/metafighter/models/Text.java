package phdev.com.br.metafighter.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import phdev.com.br.metafighter.GameParameters;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Text extends WindowEntity {

    public final int TOP = 0;
    public final int CENTER = 1;
    public final int BOTTON = 2;
    public final int LEFT = 0;
    public final int RIGHT = 2;

    private boolean autosize = false;
    public static boolean fontsPers;
    private int verticalAlignment;
    private int horizontalAlignment;
    private String text;
    private String textToDraw[];
    public static Font fonts[];
    private float textSize;
    private int colorText;

    public Text(String text){
        super(new RectF(), new Paint(), null);
        this.colorText = Color.RED;
        super.paint.setColor(colorText);
        this.textSize = 100;
        this.text = text;
        this.verticalAlignment = CENTER;
        this.horizontalAlignment = CENTER;
        super.drawableArea = new RectF(0, 0, GameParameters.getInstance().screenWidth, GameParameters.getInstance().screenHeight);
        this.checkAndFormatText();
        this.prepareTextToDraw();
    }

    public RectF getDrawableArea(){
        return this.drawableArea;
    }

    public void setDrawableArea(RectF drawableArea){
        super.drawableArea = new RectF(drawableArea);
        prepareTextToDraw();
    }

    public void setFont(String fontName){
        loadFont(fontName);
        prepareTextToDraw();
        if(autosize)
            this.adaptText();
    }

    public void setTextSize(float size){
        this.textSize = size;
        //this.prepareTextToDraw();
        if(autosize)
            this.adaptText();
    }

    public String getText(){
        return this.text;
    }

    public void setText(String text){
        this.text = text;
        this.checkAndFormatText();
        this.prepareTextToDraw();
    }

    public boolean isAutosize() {
        return autosize;
    }

    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public String[] getTextToDraw() {
        return textToDraw;
    }

    public void setTextToDraw(String[] textToDraw) {
        this.textToDraw = textToDraw;
    }

    public int getColorText() {
        return colorText;
    }

    public void setColorText(int colorText) {
        this.colorText = colorText;
    }

    public void setColor(int color){
        this.paint.setColor(color);
    }

    private boolean loadFont(String name){
        if(fonts != null){
            for(Font font : fonts){
                if(font.getName().equals(name)){
                    if(font instanceof TyperfaceFont){
                        this.paint.setTypeface(((TyperfaceFont) font).getFont());
                        return true;
                    }
                }
            }
            Log.e("GameEngine", "Fonte não encontrada!");
        }
        return false;
    }

    private void prepareTextToDraw(){

        super.paint.setTextSize(textSize);

        this.setVerticalAlignment(verticalAlignment);
        this.setHorizontalAlignment(horizontalAlignment);

    }

    private void checkAndFormatText(){
        if(checkEspecialChars(this.text) > 0){
            textToDraw = new String[checkEspecialChars(text) + 1];
            int cont = 0;
            for(int i=0; cont<text.length(); cont++){
                if(textToDraw[i] == null)
                    textToDraw[i] = "";
                if(text.charAt(cont) == '\n')
                    i++;
                else
                    textToDraw[i] = textToDraw[i] + text.charAt(cont);
            }
        }
        else
            textToDraw = new String[]{text};

    }

    private int checkEspecialChars(String text){
        int cont = 0;
        for (int i=0; i<text.length(); i++)
            if(text.charAt(i) == '\n')
                cont++;
        return cont;
    }

    public void setVerticalAlignment(int alignment){
        Rect rectTextBounds = new Rect();
        super.paint.getTextBounds(text, 0, text.length(), rectTextBounds);

        switch (alignment){
            case TOP:
                super.setY( super.drawableArea.top - rectTextBounds.top );
                break;
            case CENTER:
                super.setY( super.drawableArea.centerY() - (this.textSize * textToDraw.length)/2 - rectTextBounds.top );
                break;
            case BOTTON:
                super.setY( super.drawableArea.bottom - (this.textSize * textToDraw.length) - rectTextBounds.top );
                break;
        }
    }

    public void setHorizontalAlignment(int alignment){

        switch (alignment){
            case LEFT:
                super.paint.setTextAlign(Paint.Align.LEFT);
                super.setX( this.drawableArea.left );
                break;
            case CENTER:
                super.paint.setTextAlign(Paint.Align.CENTER);
                super.setX( this.drawableArea.centerX() );
                break;
            case RIGHT:
                super.paint.setTextAlign(Paint.Align.RIGHT);
                super.setX( this.drawableArea.right );
                break;

        }
    }

    public void setAutosize(boolean autosize){
        this.autosize = autosize;
        this.adaptText();
    }

    public void adaptText(){
        int largerWord = 0;
        int indexLargerWord = 0;
        for(int i=0; i<textToDraw.length; i++){
            if(textToDraw[i].length() > largerWord) {
                largerWord = textToDraw[i].length();
                indexLargerWord = i;
            }
        }

        Paint paint = new Paint();
        paint.setTextSize( this.textSize );

        Rect rect = new Rect();
        paint.getTextBounds(textToDraw[indexLargerWord], 0, textToDraw[indexLargerWord].length(), rect);

        if(rect.width() >= (super.drawableArea.width() - (super.drawableArea.width()/10)))
            while(rect.width() >= (super.drawableArea.width() - (super.drawableArea.width()/10))){
                paint.setTextSize( paint.getTextSize() - 2);
                paint.getTextBounds(textToDraw[indexLargerWord], 0, textToDraw[indexLargerWord].length(), rect);
            }


        paint.getTextBounds(textToDraw[indexLargerWord], 0, textToDraw[indexLargerWord].length(), rect);

        if(rect.height() * textToDraw.length >= (super.drawableArea.height() - super.drawableArea.width()/10))
            while(rect.height() * textToDraw.length >= (super.drawableArea.height() - super.drawableArea.width()/10 )){
                //this.setTextSize( this.textSize - 2);
                paint.setTextSize( paint.getTextSize() - 2);
                paint.getTextBounds(textToDraw[indexLargerWord], 0, textToDraw[indexLargerWord].length(), rect);
            }

        this.textSize = paint.getTextSize();
        this.prepareTextToDraw();

    }


    @Override
    public void draw(Canvas canvas) {
        //canvas.clipRect(super.drawableArea);
        for (int i = 0; i < textToDraw.length; i++) {
            canvas.drawText(this.textToDraw[i], super.getX(),
                    super.getY() + (i * ( super.paint.getTextSize())),  super.paint);

        }
    }

    @Override
    public void update() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
