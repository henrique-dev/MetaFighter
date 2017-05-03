package phdev.com.br.metafighter.cmp.graphics;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Sprite {

    private Texture texture;

    public Sprite(Texture texture){
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
