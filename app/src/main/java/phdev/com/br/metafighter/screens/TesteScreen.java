package phdev.com.br.metafighter.screens;

import android.graphics.RectF;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;

import phdev.com.br.metafighter.BluetoothManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.event.listeners.ConnectionListener;
import phdev.com.br.metafighter.cmp.event.listeners.EventListener;
import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.LabelMove;
import phdev.com.br.metafighter.cmp.window.Scene;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.connections.Server;
import phdev.com.br.metafighter.connections.packets.Move;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class TesteScreen extends Screen {

    private Scene mainscene;

    private Texture backgroundTexture;
    private Texture labelTexture;

    private LabelMove label;

    private BackGround backGround;

    private BluetoothManager manager;

    private Server server;

    public TesteScreen(EventListener listener, BluetoothManager manager) {
        super(listener);

        init();

        this.manager = manager;

        server = new Server();
        server.setPacketListener(currentScene.getListener());
        this.manager.setConnectionListener(server.getListener());

    }

    @Override
    protected boolean loadTextures() {

        backgroundTexture = new Texture("images/backgrounds/background1.png");
        labelTexture = new Texture("images/labels/label2.png");

        return true;
    }

    @Override
    protected boolean loadFonts() {
        return true;
    }

    @Override
    protected boolean loadSounds() {
        return true;
    }

    @Override
    protected boolean loadComponents() {

        RectF screenSize = new RectF(GameParameters.getInstance().screenSize);

        backGround = new BackGround(new RectF(screenSize), backgroundTexture);

        RectF labelSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);

        label = new LabelMove(labelSize, null, labelTexture);

        backGround.setId(0);
        label.setId(1);

        mainscene = new Scene();
        mainscene.add(backGround);
        mainscene.add(label);

        currentScene = mainscene;

        return true;
    }

    @Override
    public void update(){
        super.update();


    }

}
