package phdev.com.br.metafighter.screens;

import android.bluetooth.BluetoothDevice;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.List;

import phdev.com.br.metafighter.BluetoothManager;
import phdev.com.br.metafighter.GameParameters;
import phdev.com.br.metafighter.cmp.event.ClickEvent;
import phdev.com.br.metafighter.cmp.event.Event;
import phdev.com.br.metafighter.cmp.event.listeners.ActionListener;
import phdev.com.br.metafighter.cmp.game.AnimatedBackground;
import phdev.com.br.metafighter.cmp.game.Collision;
import phdev.com.br.metafighter.cmp.misc.Constant;
import phdev.com.br.metafighter.cmp.misc.GameContext;
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

    private BluetoothManager bluetoothManager;

    private List<BluetoothDevice> pairedDevices;

    private Scene mainMenuScene;
    private Scene optionsScene;
    private Scene multiplayerSelectScene;
    private Scene multiplayerHostScene;
    private Scene multiplayerJoinScene;

    private Texture buttonSingleTexture;
    private Texture buttonMultiTexture;
    private Texture buttonOptionTexture;

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


    // Para testes*****************************************


    public MainScreen(GameContext context) {
        super(context);
        init();
        this.bluetoothManager = context.getConnectionType().getBluetoothManager();
    }

    @Override
    protected boolean loadTextures() {

        this.mainBackgroundTexture = new Texture("images/backgrounds/background6.png");
        this.buttonTexture = new Texture("images/buttons/button2.png");
        this.textureTableHead = new Texture("cmp/table/head.png");
        this.textureTableBody = new Texture("cmp/table/body.png");
        this.textureTableShow = new Texture("cmp/table/show.png");
        this.textureTableItem = new Texture("cmp/table/item.png");

        buttonSingleTexture = new Texture("images/buttons/botao1.png");
        buttonMultiTexture = new Texture("images/buttons/botao2.png");
        buttonOptionTexture = new Texture("images/buttons/botao3.png");

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

        float fontSizeButton = GameParameters.getInstance().fontSizeButton;


        // Carrega os componentes do menu principal
        mainMenuScene = new Scene(){

            @Override
            public void init() {

                float divx = (screenSize.width()/4)/8;
                float divy = (screenSize.height()/8)/2;

                RectF buttonSize = new RectF(0, 0, screenSize.width()/4, screenSize.height()/4);

                float fontSize = Text.adaptText(new String[]{"Multijogador"}, buttonSize);

                singleplayerButton = new Button(
                        new RectF( screenSize.centerX() - divx - buttonSize.width(),
                                screenSize.centerY() - divy - buttonSize.height(),
                                screenSize.centerX() - divx,
                                screenSize.centerY() - divy),
                        "", buttonSingleTexture);
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
                        "", buttonMultiTexture);
                multiplayerButton.getText().setTextSize(fontSize);
                multiplayerButton.addEventListener(new ActionListener() {
                    @Override
                    public void actionPerformed(Event event) {
                        multiplayerButtonAction(event);
                    }
                });
                multiplayerButton.addAnimationListener(new GoAndBack(multiplayerButton));
                multiplayerButton.setId(1);

                optionsButton = new Button(
                        new RectF( screenSize.centerX() - (buttonSize.width()/2),
                                screenSize.centerY() + divy,
                                screenSize.centerX() + (buttonSize.width()/2),
                                screenSize.centerY() + divy + buttonSize.height()),
                        "", buttonOptionTexture);
                optionsButton.getText().setTextSize(fontSize);
                optionsButton.addEventListener(new ActionListener() {
                    @Override
                    public void actionPerformed(Event event) {
                        optionsButtonAction(event);
                    }
                });
                optionsButton.addAnimationListener(new GoAndBack(optionsButton));
                optionsButton.setId(2);


                super.add(mainBackGround);
                super.add(singleplayerButton);
                super.add(multiplayerButton);
                super.add(optionsButton);
            }

            private void optionsButtonAction(Event evt){
                //new MatchScreen(listener, null);
                currentScene = optionsScene;
            }

            private void multiplayerButtonAction(Event evt){
                bluetoothManager.activate();
                currentScene = multiplayerSelectScene;
            }

            private void singleplayerButtonAction(Event evt){
                new SelectCharacterScreen(context);
            }
        };

        // Carrega os componentes do menu de escolha multiplayer
        multiplayerSelectScene = new Scene() {
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
                hostButton.setId(0);

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


                super.add(mainBackGround);
                super.add(hostButton);
                super.add(joinButton);
                super.add(backToMainFromMultiSelectButton);
            }

            private void backToMainFromMultiSelectButtonAction(Event evt){
                currentScene = mainMenuScene;
            }

            private void joinButtonAction(Event evt){
                if (bluetoothManager.haveBluetooth())
                    sendMessageToScreen("O bluetooth não foi ativado ou\n este dispositivo não é compatível");
                else
                if (!bluetoothManager.isEnabled())
                    sendMessageToScreen(": O bluetooth deve estar ativado para proseeguir.");
                else {
                    pairedDevices = bluetoothManager.getBondedDevices();
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
                                    bluetoothManager.connect(pairedDevices.get(((ClickEvent)event).id));
                                }
                            });

                            table.addItem(item);
                            counter++;
                        }
                    }
                    currentScene = multiplayerJoinScene;

                }
            }

            private void hostButtonAction(Event evt){
                if (bluetoothManager.haveBluetooth())
                    sendMessageToScreen("O bluetooth não foi ativado ou\n este dispositivo não é compatível");
                else
                if (!bluetoothManager.isEnabled())
                    sendMessageToScreen(": O bluetooth deve estar ativado para proseeguir.");
                else {
                    bluetoothManager.start();
                    currentScene = multiplayerHostScene;
                }
            }
        };

        // Carrega os componentes do menu multiplayer host
        multiplayerHostScene = new Scene() {
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


                super.add(mainBackGround);
                super.add(backToMultiSelectFromHostButton);
            }

            private void backToMultiSelectFromHostButtonAction(Event evt){
                bluetoothManager.stop();
                currentScene = multiplayerSelectScene;
            }
        };

        // Carrega os componentes do menu multiplayer join
        multiplayerJoinScene = new Scene() {
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
                        "Dispositivos pareados"
                );

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


                super.add(mainBackGround);
                super.add(table);
                super.add(backToMultiSelectFromJoinButton);
            }

            private void backToMultiSelectFromJoinButtonAction(Event evt){
                bluetoothManager.stop();
                currentScene = multiplayerSelectScene;
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


                super.add(mainBackGround);
                super.add(backToMainFromOptionsButton);
            }

            private void backToMainFromOptionsButtonAction(Event evt){
                currentScene = mainMenuScene;
            }
        };

        currentScene = mainMenuScene;

        return true;
    }

}
