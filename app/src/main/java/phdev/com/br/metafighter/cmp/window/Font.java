package phdev.com.br.metafighter.cmp.window;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Font {

    private String name;

    protected Font(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
