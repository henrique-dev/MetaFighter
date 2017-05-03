package phdev.com.br.metafighter.screens;

import android.bluetooth.BluetoothDevice;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.List;

import phdev.com.br.metafighter.BluetoothManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.event.ClickEvent;
import phdev.com.br.metafighter.cmp.event.Event;
import phdev.com.br.metafighter.cmp.event.listeners.ClickListener;
import phdev.com.br.metafighter.cmp.event.listeners.EventListener;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.Button;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.window.Table;
import phdev.com.br.metafighter.cmp.window.TableItem;
import phdev.com.br.metafighter.cmp.window.Text;
import phdev.com.br.metafighter.cmp.graphics.Texture;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MultiplayerJoinScreen extends Screen {

    private BluetoothManager manager;

    private List<BluetoothDevice> pairedDevices;

    private BackGround mainBackGround;

    private Texture textureTableHead;
    private Texture textureTableBody;
    private Texture textureTableShow;
    private Texture textureTableItem;
    private Texture backButtonTexture;

    private Button backButton;


    private Table table;

    public MultiplayerJoinScreen(EventListener listener, BluetoothManager manager) {
        super(listener);
        this.manager = manager;
        pairedDevices = manager.getBondedDevices();
    }

    @Override
    protected boolean loadTextures() {

        this.textureTableHead = new Texture("cmp/table/head.png");
        this.textureTableBody = new Texture("cmp/table/body.png");
        this.textureTableShow = new Texture("cmp/table/show.png");
        this.textureTableItem = new Texture("cmp/table/item.png");
        this.backButtonTexture = new Texture("images/buttons/button1.png");

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

        RectF screenSize = GameParameters.getInstance().screenSize;
        float tableWidth = screenSize.width()/2;
        float tableHeight = (float)(screenSize.height() / 1.3);

        RectF tableArea = new RectF(0,0,tableWidth, tableHeight);

        table = new Table(
                new RectF(screenSize.centerX() - tableWidth/2,
                        screenSize.centerY() - tableHeight/2,
                        screenSize.centerX() + tableWidth/2,
                        screenSize.centerY() + tableHeight/2),
                new Paint(),
                this.textureTableBody,
                this.textureTableHead,
                this.textureTableShow,
                this.textureTableItem,
                "Dispositivos pareados"
        );

        float fontSize = Text.adaptText(new String[]{table.getTextHead().getText()}, table.getAreaHead());
        table.getTextHead().setTextSize(fontSize);

        RectF buttonSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);
        this.backButton = new Button(
                new RectF(screenSize.right - buttonSize.width(),
                        screenSize.bottom - buttonSize.height(),
                        screenSize.right, screenSize.bottom),
                "Voltar", backButtonTexture);
        this.backButton.addEventListener(new ClickListener() {
            @Override
            public void actionPerformed(Event event) {
                manager.stop();
                new MultiplayerSelectScreen(listener);
            }

            @Override
            public boolean pressedPerformed(ClickEvent event) {
                return true;
            }

            @Override
            public boolean releasedPerformed(ClickEvent event) {
                return true;
            }
        });
        this.backButton.getText().setTextSize(fontSize);

        int counter = 0;
        if (pairedDevices.size() > 0){

            for (BluetoothDevice device : pairedDevices){
                TableItem item = new TableItem(device.getName());
                item.getText().setTextSize(fontSize);
                item.setId(counter);

                item.addEventListener(new ClickListener() {
                    @Override
                    public void actionPerformed(Event event) {
                        manager.connect(pairedDevices.get(((ClickEvent)event).id));
                    }

                    @Override
                    public boolean pressedPerformed(ClickEvent event) {
                        return true;
                    }

                    @Override
                    public boolean releasedPerformed(ClickEvent event) {
                        return true;
                    }
                });

                this.table.addItem(item);
                counter++;
            }
        }

        add(table);
        add(backButton);

        return true;
    }
}
