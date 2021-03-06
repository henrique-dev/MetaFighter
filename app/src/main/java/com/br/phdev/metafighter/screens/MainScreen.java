package com.br.phdev.metafighter.screens;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.List;

import com.br.phdev.metafighter.BluetoothManager;
import com.br.phdev.metafighter.GameParameters;
import phdev.com.br.metafighter.R;
import com.br.phdev.metafighter.SoundManager;
import com.br.phdev.metafighter.cmp.event.ClickEvent;
import com.br.phdev.metafighter.cmp.event.Event;
import com.br.phdev.metafighter.cmp.event.animation.GoAndBack;
import com.br.phdev.metafighter.cmp.event.listeners.ActionListener;
import com.br.phdev.metafighter.cmp.game.AnimatedBackground;
import com.br.phdev.metafighter.cmp.graphics.Texture;
import com.br.phdev.metafighter.cmp.misc.Constant;
import com.br.phdev.metafighter.cmp.misc.GameContext;
import com.br.phdev.metafighter.cmp.window.BackGround;
import com.br.phdev.metafighter.cmp.window.Button;
import com.br.phdev.metafighter.cmp.window.Label;
import com.br.phdev.metafighter.cmp.window.Scene;
import com.br.phdev.metafighter.cmp.window.Screen;
import com.br.phdev.metafighter.cmp.window.Table;
import com.br.phdev.metafighter.cmp.window.TableItem;
import com.br.phdev.metafighter.cmp.window.Text;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class MainScreen extends Screen {

    private SoundManager soundManager;

    private BluetoothManager bluetoothManager;

    private List<BluetoothDevice> pairedDevices;

    private Scene mainMenuScene;
    private Scene optionsScene;
    private Scene multiPlayerSelectScene;
    private Scene multiPlayerHostScene;
    private Scene multiPlayerJoinScene;

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

    private Label labelInfo;

    private Table table;


    // Para testes*****************************************


    public MainScreen(GameContext context) {
        super(context);
        init();
        bluetoothManager = context.getConnectionType().getBluetoothManager();

        soundManager = context.getSoundManager();

    }

    @Override
    protected boolean loadTextures() {

        mainBackgroundTexture = new Texture("images/backgrounds/1.png");
        context.getProgressCmp().increase(3);
        buttonTexture = new Texture("images/buttons/2.png");
        context.getProgressCmp().increase(8);
        textureTableHead = new Texture("cmp/table/head.png");
        context.getProgressCmp().increase(10);
        textureTableBody = new Texture("cmp/table/body.png");
        context.getProgressCmp().increase(15);
        textureTableShow = new Texture("cmp/table/show.png");
        context.getProgressCmp().increase(20);
        textureTableItem = new Texture("cmp/table/item.png");
        context.getProgressCmp().increase(25);

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

        final RectF screenSize = GameParameters.getInstance().screenSize;
        mainBackGround = new BackGround(new RectF(screenSize), mainBackgroundTexture);


        // Carrega os componentes do menu principal
        mainMenuScene = new Scene(){

            @Override
            public void init() {

                float divx = (screenSize.width()/4)/8;
                float divy = (screenSize.height()/8)/2;

                RectF buttonSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);

                float fontSize = Text.adaptText(new String[]{"Multijogador"}, buttonSize);

                labelInfo = new Label(new RectF(0, screenSize.bottom - buttonSize.height(), buttonSize.width(), screenSize.bottom),
                        "Versão beta", null);
                labelInfo.getText().setTextSize(fontSize);

                singleplayerButton = new Button(
                        new RectF( screenSize.centerX() - divx - buttonSize.width(),
                                screenSize.centerY() - divy - buttonSize.height(),
                                screenSize.centerX() - divx,
                                screenSize.centerY() - divy),
                        "1 Jogador", buttonTexture);
                singleplayerButton.getText().setTextSize(fontSize);
                singleplayerButton.addEventListener(new ActionListener() {
                    @Override
                    public void actionPerformed(Event event) {
                        singleplayerButtonAction(event);
                    }
                });
                singleplayerButton.addAnimationListener(new GoAndBack(singleplayerButton));

                multiplayerButton = new Button(
                        new RectF( screenSize.centerX() + divx,
                                screenSize.centerY() - divy - buttonSize.height(),
                                screenSize.centerX() + divx + buttonSize.width(),
                                screenSize.centerY() - divy),
                        "2 Jogadores", buttonTexture);
                multiplayerButton.getText().setTextSize(fontSize);
                multiplayerButton.addEventListener(new ActionListener() {
                    @Override
                    public void actionPerformed(Event event) {
                        multiplayerButtonAction(event);
                    }
                });
                multiplayerButton.addAnimationListener(new GoAndBack(multiplayerButton));

                optionsButton = new Button(
                        new RectF( screenSize.centerX() - (buttonSize.width()/2),
                                screenSize.centerY() + divy,
                                screenSize.centerX() + (buttonSize.width()/2),
                                screenSize.centerY() + divy + buttonSize.height()),
                        "Sobre", buttonTexture);
                optionsButton.getText().setTextSize(fontSize);
                optionsButton.addEventListener(new ActionListener() {
                    @Override
                    public void actionPerformed(Event event) {
                        optionsButtonAction(event);
                    }
                });
                optionsButton.addAnimationListener(new GoAndBack(optionsButton));

                context.getProgressCmp().increase(80);


                super.add(mainBackGround);
                super.add(singleplayerButton);
                super.add(multiplayerButton);
                super.add(optionsButton);
                super.add(labelInfo);
            }

            @Override
            public Scene start(){
                super.start();

                if (soundManager.getCurrentMusic() != R.raw.music)
                    soundManager.playMusic(R.raw.music);

                return this;
            }

            private void optionsButtonAction(Event evt){
                //new MatchScreen(listener, null);
                context.sendMessage("Meta Fighter\n\nProgramador\nPaulo Henrique\n\nEdição imagens\nGabriel Matos\nMatheus Mendes"
                        + "\n\nModelos para os personagens\nCarlos Guedes\nLuis Carlos\nPatricia Vale\nQuele Rodrigues\nRomulo Lima\nKaila Cardoso\n\n" +
                        "Agradecimentos especiais para\nRonedo de Sá\nOn Time Comunicação\n\n" +
                        "Musicas\nSWiTCH - Broked It\nMetallica1136 - Onset and Reprecussion", 6);
                //currentScene = optionsScene.start();
            }

            private void multiplayerButtonAction(Event evt){
                bluetoothManager.activate();
                currentScene = multiPlayerSelectScene.start();
            }

            private void singleplayerButtonAction(Event evt){

                //context.sendMessage("Somente modo 2 jogadores disponivel na versão beta", 6);
                new SelectCharacterScreen(context, Constant.GAMEMODE_SINGLEPLAYER);
            }
        };

        // Carrega os componentes do menu de escolha multiplayer
        multiPlayerSelectScene = new Scene() {
            @Override
            public void init() {

                float divy = (screenSize.height()/8)/2;

                RectF buttonSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);

                float fontSize = Text.adaptText(new String[]{"Criar partida"}, buttonSize);

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

                joinButton = new Button(
                        new RectF( screenSize.centerX() - buttonSize.width()/2,
                                screenSize.centerY() - buttonSize.height()/2,
                                screenSize.centerX() + buttonSize.width()/2,
                                screenSize.centerY() + buttonSize.height()/2),
                        "Entrar em\numa partida", buttonTexture);
                joinButton.getText().setTextSize(fontSize);
                joinButton.addEventListener(new ActionListener() {
                    @Override
                    public void actionPerformed(Event event) {
                        joinButtonAction(event);
                    }
                });
                joinButton.addAnimationListener(new GoAndBack(joinButton));

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

                context.getProgressCmp().increase(85);


                super.add(mainBackGround);
                super.add(hostButton);
                super.add(joinButton);
                super.add(backToMainFromMultiSelectButton);
            }

            private void backToMainFromMultiSelectButtonAction(Event evt){
                currentScene = mainMenuScene.start();
            }

            int counter = 0;

            private void joinButtonAction(Event evt){
                if (bluetoothManager.haveBluetooth())
                    sendMessageToScreen("O bluetooth não foi ativado ou\n este dispositivo não é compatível");
                else
                if (!bluetoothManager.isEnabled())
                    sendMessageToScreen(": O bluetooth deve estar ativado para proseeguir.");
                else {
                    pairedDevices = bluetoothManager.getBondedDevices();

                    if (pairedDevices.size() > 0){

                        for (BluetoothDevice device : pairedDevices){

                            TableItem item = new TableItem(device.getName());
                            item.getText().setColor(Color.BLACK);
                            item.setId(counter);

                            item.addEventListener(new ActionListener() {
                                @Override
                                public void actionPerformed(Event event) {
                                    bluetoothManager.stop();
                                    bluetoothManager.connect(pairedDevices.get(((ClickEvent)event).id));
                                    bluetoothManager.cancelDiscovery();
                                }
                            });

                            table.addItem(item);
                            counter++;
                        }
                    }

                    BroadcastReceiver receiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {

                            try{
                                String action = intent.getAction();
                                if (BluetoothDevice.ACTION_FOUND.equals(action)){

                                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                                    TableItem item = new TableItem(device.getName());
                                    item.getText().setColor(Color.BLACK);
                                    item.setId(counter);

                                    pairedDevices.add(device);

                                    item.addEventListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(Event event) {
                                            bluetoothManager.stop();
                                            bluetoothManager.connect(pairedDevices.get(((ClickEvent)event).id));
                                            bluetoothManager.cancelDiscovery();
                                        }
                                    });

                                    counter++;
                                    table.addItem(item);
                                }
                            }
                            catch (Exception e){}
                        }
                    };

                    context.getConnectionType().getBluetoothManager().startDiscovery(receiver);

                    currentScene = multiPlayerJoinScene.start();

                }
            }

            private void hostButtonAction(Event evt){
                if (bluetoothManager.haveBluetooth())
                    sendMessageToScreen("O bluetooth não foi ativado ou\n este dispositivo não é compatível");
                else
                if (!bluetoothManager.isEnabled())
                    sendMessageToScreen(": O bluetooth deve estar ativado para proseeguir.");
                else {
                    bluetoothManager.stop();
                    bluetoothManager.start();

                    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
                    context.sendIntentRequest(discoverableIntent);

                    currentScene = multiPlayerHostScene.start();
                }
            }
        };

        // Carrega os componentes do menu multiplayer host
        multiPlayerHostScene = new Scene() {
            @Override
            public void init() {

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

                context.getProgressCmp().increase(90);


                super.add(mainBackGround);
                super.add(backToMultiSelectFromHostButton);
            }

            private void backToMultiSelectFromHostButtonAction(Event evt){
                bluetoothManager.stop();
                currentScene = multiPlayerSelectScene.start();
            }
        };

        // Carrega os componentes do menu multiplayer join
        multiPlayerJoinScene = new Scene() {
            @Override
            public void init() {
                float tableWidth = screenSize.width() / 2;
                float tableHeight = (float) (screenSize.height() / 1.3);

                table = new Table(
                        new RectF(screenSize.centerX() - tableWidth / 2,
                                screenSize.centerY() - tableHeight / 2,
                                screenSize.centerX() + tableWidth / 2,
                                screenSize.centerY() + tableHeight / 2),
                        new Paint(),
                        textureTableBody,
                        textureTableHead,
                        textureTableShow,
                        textureTableItem,
                        " Jogadores "
                );
                table.getTextHead().setColor(Color.BLACK);

                float fontSize = Text.adaptText(new String[]{table.getTextHead().getText()}, table.getAreaHead());
                table.getTextHead().setTextSize(fontSize);

                RectF buttonSize = new RectF(0, 0, screenSize.width() / 4, screenSize.height() / 4);
                backToMultiSelectFromJoinButton = new Button(
                        new RectF(screenSize.right - buttonSize.width(),
                                screenSize.bottom - buttonSize.height(),
                                screenSize.right, screenSize.bottom),
                        "Voltar", buttonTexture);
                backToMultiSelectFromJoinButton.addEventListener(new ActionListener() {
                    @Override
                    public void actionPerformed(Event event) {
                        backToMultiSelectFromJoinButtonAction(event);
                    }
                });
                backToMultiSelectFromJoinButton.getText().setTextSize(fontSize);

                context.getProgressCmp().increase(95);


                super.add(mainBackGround);
                super.add(table);
                super.add(backToMultiSelectFromJoinButton);
            }

            private void backToMultiSelectFromJoinButtonAction(Event evt){
                bluetoothManager.cancelDiscovery();
                bluetoothManager.stop();
                currentScene = multiPlayerSelectScene.start();
            }
        };

        // Carrega os componentes do menu opções
        optionsScene = new Scene() {
            @Override
            public void init() {

                RectF buttonSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);
                float fontSize = Text.adaptText(new String[]{"Voltar"}, buttonSize);
                backToMainFromOptionsButton = new Button(
                        new RectF(screenSize.right - buttonSize.width(),
                                screenSize.bottom - buttonSize.height(),
                                screenSize.right, screenSize.bottom),
                        "Voltar", buttonTexture);
                backToMainFromOptionsButton.addEventListener(new ActionListener() {
                    @Override
                    public void actionPerformed(Event event) {
                        backToMainFromOptionsButtonAction(event);
                    }
                });
                backToMainFromOptionsButton.getText().setTextSize(fontSize);

                context.getProgressCmp().increase(98);


                super.add(mainBackGround);
                super.add(backToMainFromOptionsButton);
            }

            private void backToMainFromOptionsButtonAction(Event evt){
                currentScene = mainMenuScene.start();
            }
        };

        currentScene = mainMenuScene.start();

        return true;
    }

}
