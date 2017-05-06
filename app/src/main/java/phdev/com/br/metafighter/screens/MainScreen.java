package phdev.com.br.metafighter.screens;

import android.bluetooth.BluetoothDevice;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.List;

import phdev.com.br.metafighter.BluetoothManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.event.ClickEvent;
import phdev.com.br.metafighter.cmp.event.Event;
import phdev.com.br.metafighter.cmp.event.listeners.ActionListener;
import phdev.com.br.metafighter.cmp.event.listeners.EventListener;
import phdev.com.br.metafighter.cmp.game.AnimatedBackground;
import phdev.com.br.metafighter.cmp.window.BackGround;
import phdev.com.br.metafighter.cmp.window.Button;
import phdev.com.br.metafighter.cmp.window.Scene;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.cmp.window.Table;
import phdev.com.br.metafighter.cmp.window.TableItem;
import phdev.com.br.metafighter.cmp.window.Text;
import phdev.com.br.metafighter.cmp.graphics.Texture;
import phdev.com.br.metafighter.cmp.event.animation.GoAndBack;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MainScreen extends Screen {

    private BluetoothManager manager;
    private List<BluetoothDevice> pairedDevices;

    private Scene mainMenuScene;
    private Scene optionsScene;
    private Scene multiplayerSelectScene;
    private Scene multiplayerHostScene;
    private Scene multiplayerJoinScene;

    private Texture buttonTexture;
    private Texture mainBackgroundTexture;

    private Texture textureTableHead;
    private Texture textureTableBody;
    private Texture textureTableShow;
    private Texture textureTableItem;
    //private Texture backButtonTexture;

    private BackGround mainBackGround;

    private AnimatedBackground animatedBackground;

    private Button singleplayerButton;
    private Button multiplayerButton;
    private Button optionsButton;
    private Button hostButton;
    private Button joinButton;
    private Button backToMainFromMultiSelectButton;
    private Button backToMultiSelectFromHostButton;
    private Button backToMultiSelectFromJoinButton;
    private Button backToMainFromOptionsButton;

    private Table table;

    public MainScreen(EventListener listener) {
        super(listener);
    }

    @Override
    protected boolean loadTextures() {

        this.mainBackgroundTexture = new Texture("images/backgrounds/background7.png");
        this.buttonTexture = new Texture("images/buttons/button2.png");
        this.textureTableHead = new Texture("cmp/table/head.png");
        this.textureTableBody = new Texture("cmp/table/body.png");
        this.textureTableShow = new Texture("cmp/table/show.png");
        this.textureTableItem = new Texture("cmp/table/item.png");
        //this.backButtonTexture = new Texture("images/buttons/button1.png");

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
        mainBackGround = new BackGround(new RectF(screenSize), mainBackgroundTexture);
        //mainBackGround = new AnimatedBackground(mainBackGround, 4, AnimatedBackground.LEFT, AnimatedBackground.NO_MOVEMENT, true, true);

        //**********************************************************************************************
        //**********************************************************************************************
        // Carrega os componentes do menu principal
        {
            float divx = (screenSize.width()/4)/8;
            float divy = (screenSize.height()/8)/2;

            RectF buttonSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);

            float fontSize = Text.adaptText(new String[]{"Multijogador"}, buttonSize);

            singleplayerButton = new Button(
                    new RectF( screenSize.centerX() - divx - buttonSize.width(),
                            screenSize.centerY() - divy - buttonSize.height(),
                            screenSize.centerX() - divx,
                            screenSize.centerY() - divy),
                    "Um jogador", this.buttonTexture);
            singleplayerButton.getText().setTextSize(fontSize);
            singleplayerButton.addEventListener(new ActionListener() {
                @Override
                public void actionPerformed(Event event) {
                    singleplayerButtonAction(event);
                }
            });
            singleplayerButton.addAnimationListener(new GoAndBack(singleplayerButton));
            singleplayerButton.setId(0);

            multiplayerButton = new Button(
                    new RectF( screenSize.centerX() + divx,
                            screenSize.centerY() - divy - buttonSize.height(),
                            screenSize.centerX() + divx + buttonSize.width(),
                            screenSize.centerY() - divy),
                    "Multijogador", this.buttonTexture);
            multiplayerButton.getText().setTextSize(fontSize);
            multiplayerButton.addEventListener(new ActionListener() {
                @Override
                public void actionPerformed(Event event) {
                    multiplayerButtonAction(event);
                }
            });
            multiplayerButton.addAnimationListener(new GoAndBack(multiplayerButton));
            multiplayerButton.setId(1);

            this.optionsButton = new Button(
                    new RectF( screenSize.centerX() - (buttonSize.width()/2),
                            screenSize.centerY() + divy,
                            screenSize.centerX() + (buttonSize.width()/2),
                            screenSize.centerY() + divy + buttonSize.height()),
                    "Opções", this.buttonTexture);
            optionsButton.getText().setTextSize(fontSize);
            optionsButton.addEventListener(new ActionListener() {
                @Override
                public void actionPerformed(Event event) {
                    optionsButtonAction(event);
                }
            });
            optionsButton.addAnimationListener(new GoAndBack(optionsButton));
            optionsButton.setId(2);

            mainMenuScene = new Scene();
            mainMenuScene.add(mainBackGround);
            mainMenuScene.add(singleplayerButton);
            mainMenuScene.add(multiplayerButton);
            mainMenuScene.add(optionsButton);
        }

        //******************************************************************************************
        // Carrega os componentes do menu de escolha multiplayer

        {
            float divy = (screenSize.height()/8)/2;

            RectF buttonSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);

            float fontSize = Text.adaptText(new String[]{"Entrar em uma partida"}, buttonSize);

            //ButtonHandlerMultiplayer buttonHandler = new ButtonHandlerMultiplayer();

            hostButton = new Button(
                    new RectF( screenSize.centerX() - buttonSize.width()/2,
                            screenSize.centerY() - buttonSize.height()/2 - divy - buttonSize.height(),
                            screenSize.centerX() + buttonSize.width()/2,
                            screenSize.centerY() - buttonSize.height()/2 - divy),
                    "Criar partida", buttonTexture);
            hostButton.getText().setTextSize(fontSize);
            hostButton.addEventListener(new ActionListener() {
                @Override
                public void actionPerformed(Event event) {
                    hostButtonAction(event);
                }
            });
            hostButton.addAnimationListener(new GoAndBack(hostButton));
            hostButton.setId(0);

            joinButton = new Button(
                    new RectF( screenSize.centerX() - buttonSize.width()/2,
                            screenSize.centerY() - buttonSize.height()/2,
                            screenSize.centerX() + buttonSize.width()/2,
                            screenSize.centerY() + buttonSize.height()/2),
                    "Entrar em uma partida", buttonTexture);
            joinButton.getText().setTextSize(fontSize);
            joinButton.addEventListener(new ActionListener() {
                @Override
                public void actionPerformed(Event event) {
                    joinButtonAction(event);
                }
            });
            joinButton.addAnimationListener(new GoAndBack(joinButton));
            joinButton.setId(1);

            backToMainFromMultiSelectButton = new Button(
                    new RectF( screenSize.centerX() - buttonSize.width()/2,
                            screenSize.centerY() + buttonSize.height()/2 + divy,
                            screenSize.centerX() + buttonSize.width()/2,
                            screenSize.centerY() + buttonSize.height()/2 + divy + buttonSize.height()),
                    "Voltar", buttonTexture);
            backToMainFromMultiSelectButton.getText().setTextSize(fontSize);
            backToMainFromMultiSelectButton.addEventListener(new ActionListener() {
                @Override
                public void actionPerformed(Event event) {
                    backToMainFromMultiSelectButtonAction(event);
                }
            });
            backToMainFromMultiSelectButton.addAnimationListener(new GoAndBack(backToMainFromMultiSelectButton));
            backToMainFromMultiSelectButton.setId(2);

            multiplayerSelectScene = new Scene();
            multiplayerSelectScene.add(mainBackGround);
            multiplayerSelectScene.add(hostButton);
            multiplayerSelectScene.add(joinButton);
            multiplayerSelectScene.add(backToMainFromMultiSelectButton);
        }

        //*******************************************************************************************
        // Carrega os componentes do menu multiplayer host
        {
            RectF buttonSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);

            float fontSize = Text.adaptText(new String[]{"Voltar"}, buttonSize);

            backToMultiSelectFromHostButton = new Button(
                    new RectF(screenSize.right - buttonSize.width(),
                            screenSize.bottom - buttonSize.height(),
                            screenSize.right, screenSize.bottom),
                    "Voltar", buttonTexture);
            backToMultiSelectFromHostButton.addEventListener(new ActionListener() {
                @Override
                public void actionPerformed(Event event) {
                    backToMultiSelectFromHostButtonAction(event);
                }
            });
            backToMultiSelectFromHostButton.getText().setTextSize(fontSize);

            multiplayerHostScene = new Scene();
            multiplayerHostScene.add(mainBackGround);
            multiplayerHostScene.add(backToMultiSelectFromHostButton);
        }
        //**************************************************************************************
        // Carrega os componentes do menu multiplayer join
        {
            float tableWidth = screenSize.width()/2;
            float tableHeight = (float)(screenSize.height() / 1.3);

            //RectF tableArea = new RectF(0,0,tableWidth, tableHeight);

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
            this.backToMultiSelectFromJoinButton = new Button(
                    new RectF(screenSize.right - buttonSize.width(),
                            screenSize.bottom - buttonSize.height(),
                            screenSize.right, screenSize.bottom),
                    "Voltar", buttonTexture);
            this.backToMultiSelectFromJoinButton.addEventListener(new ActionListener() {
                @Override
                public void actionPerformed(Event event) {
                    backToMultiSelectFromJoinButtonAction(event);
                }
            });
            this.backToMultiSelectFromJoinButton.getText().setTextSize(fontSize);

            multiplayerJoinScene = new Scene();
            multiplayerJoinScene.add(mainBackGround);
            multiplayerJoinScene.add(table);
            multiplayerJoinScene.add(backToMultiSelectFromJoinButton);

        }

        //**************************************************************************************
        // Carrega os componentes do menu opções
        {
            RectF buttonSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);
            float fontSize = Text.adaptText(new String[]{"Voltar"}, buttonSize);
            this.backToMainFromOptionsButton = new Button(
                    new RectF(screenSize.right - buttonSize.width(),
                            screenSize.bottom - buttonSize.height(),
                            screenSize.right, screenSize.bottom),
                    "Voltar", buttonTexture);
            this.backToMainFromOptionsButton.addEventListener(new ActionListener() {
                @Override
                public void actionPerformed(Event event) {
                    backToMainFromOptionsButtonAction(event);
                }
            });
            this.backToMainFromOptionsButton.getText().setTextSize(fontSize);

            optionsScene = new Scene();
            optionsScene.add(mainBackGround);
            optionsScene.add(backToMainFromOptionsButton);
        }

        currentScene = mainMenuScene;

        return true;
    }


    private void singleplayerButtonAction(Event evt){
        new SelectCharacterScreen(listener);
    }

    private void multiplayerButtonAction(Event evt){
        manager = new BluetoothManager(listener);
        manager.activate();
        currentScene = multiplayerSelectScene;
    }

    private void optionsButtonAction(Event evt){
        //new MatchScreen(listener, null);
        currentScene = optionsScene;
    }

    private void hostButtonAction(Event evt){
        if (manager.haveBluetooth())
            sendMessageToScreen("O bluetooth não foi ativado ou\n este dispositivo não é compatível");
        else
        if (!manager.isEnabled())
            Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": O bluetooth deve estar ativado para proseeguir.");
        else {
            manager.start();
            currentScene = multiplayerHostScene;
        }
    }

    private void joinButtonAction(Event evt){
        if (manager.haveBluetooth())
            sendMessageToScreen("O bluetooth não foi ativado ou\n este dispositivo não é compatível");
        else
        if (!manager.isEnabled())
            sendMessageToScreen(": O bluetooth deve estar ativado para proseeguir.");
        else {
            pairedDevices = manager.getBondedDevices();
            int counter = 0;
            if (pairedDevices.size() > 0){

                for (BluetoothDevice device : pairedDevices){

                    log("Inserindo elementos");

                    TableItem item = new TableItem(device.getName());
                    item.getText().setTextSize(20);
                    item.setId(counter);

                    item.addEventListener(new ActionListener() {
                        @Override
                        public void actionPerformed(Event event) {
                            log("Clicou");
                            manager.connect(pairedDevices.get(((ClickEvent)event).id));
                        }
                    });

                    this.table.addItem(item);
                    counter++;
                }
            }
            currentScene = multiplayerJoinScene;

        }
    }

    private void backToMainFromMultiSelectButtonAction(Event evt){
        manager = null;
        currentScene = mainMenuScene;
    }

    private void backToMultiSelectFromHostButtonAction(Event evt){
        manager.stop();
        currentScene = multiplayerSelectScene;
    }

    private void backToMultiSelectFromJoinButtonAction(Event evt){
        manager.stop();
        currentScene = multiplayerSelectScene;
    }

    private void backToMainFromOptionsButtonAction(Event evt){
        currentScene = mainMenuScene;
    }

}
