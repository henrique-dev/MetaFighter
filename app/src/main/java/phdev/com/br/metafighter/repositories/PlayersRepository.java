package phdev.com.br.metafighter.repositories;

import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.cmp.game.Player;
import phdev.com.br.metafighter.cmp.graphics.Sprite;
import phdev.com.br.metafighter.cmp.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class PlayersRepository {

    public static final int GUEDES = 0;
    public static final int KAILA = 1;
    public static final int LUIZ = 2;
    public static final int PATRICIA = 3;
    public static final int QUELE = 4;
    public static final int ROMULO = 5;

    private List<Player> players;

    public void loadBasicTextures(){
        players = new ArrayList<>();

        players.add( new Player(new RectF(), new Paint(), null,
                new Sprite[]{new Sprite(new Texture("images/players/guedes.png"))}, "Carlos Guedes"));
        players.add( new Player(new RectF(), new Paint(), null,
                new Sprite[]{new Sprite(new Texture("images/players/kaila.png"))}, "Kaila"));
        players.add( new Player(new RectF(), new Paint(), null,
                new Sprite[]{new Sprite(new Texture("images/players/luiz.png"))}, "Luiz Carlos"));
        players.add( new Player(new RectF(), new Paint(), null,
                new Sprite[]{new Sprite(new Texture("images/players/patricia.png"))}, "Patricia"));
        players.add( new Player(new RectF(), new Paint(), null,
                new Sprite[]{new Sprite(new Texture("images/players/quele.png"))}, "Quele"));
        players.add( new Player(new RectF(), new Paint(), null,
                new Sprite[]{new Sprite(new Texture("images/players/romulo.png"))}, "Romulo"));

    }

    public void loadFull(){

    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int i){
        return players.get(i);
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
