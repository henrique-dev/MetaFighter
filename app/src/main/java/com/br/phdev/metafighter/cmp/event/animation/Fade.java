package com.br.phdev.metafighter.cmp.event.animation;

import com.br.phdev.metafighter.cmp.event.listeners.AnimationListener;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Fade implements AnimationListener {

    private int tax;
    private int alpha;
    private boolean fadein;
    private boolean fadeout;
    private boolean loop;

    public Fade(int tax, int alpha, boolean loop){
        this.tax = tax;
        this.alpha = alpha;
        this.loop = loop;
    }

    public void setActive(boolean active){
        if (active){
            if (alpha > 0)
                fadeout = true;
            else
                fadein = true;
        }
        else{
            fadeout = false;
            fadein = false;
        }
    }

    private void fadeIn(){
        if (alpha < 255){
            alpha += tax;
        }
        else
            if (alpha >= 255){
                alpha = 255;
                fadein = false;
                if (loop)
                    fadeout = true;
            }
    }

    private void fadeOut(){
        if (alpha > 0){
            alpha -= tax;
        }
        else
        if (alpha <= 0){
            alpha = 0;
            fadeout = false;
            if (loop)
                fadein = true;
        }
    }

    public void update() {
        if (fadein)
            fadeIn();
        if (fadeout)
            fadeOut();
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public boolean isFadein() {
        return fadein;
    }

    public void setFadein(boolean fadein) {
        this.fadein = fadein;
    }

    public boolean isFadeout() {
        return fadeout;
    }

    public void setFadeout(boolean fadeout) {
        this.fadeout = fadeout;
    }
}
