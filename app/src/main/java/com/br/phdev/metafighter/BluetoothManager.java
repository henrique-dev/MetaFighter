package com.br.phdev.metafighter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.br.phdev.metafighter.cmp.misc.Constant;
import com.br.phdev.metafighter.cmp.misc.GameContext;
import com.br.phdev.metafighter.screens.MainScreen;
import com.br.phdev.metafighter.screens.SelectCharacterScreen;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public final class BluetoothManager {

    private static final String NAME = "MetaFighter";
    private static final UUID MY_UUID = UUID.fromString("287c5f5a-5140-47af-bd32-d8db4ec9728d");

    protected AcceptThread acceptThread;
    protected ConnectThread connectThread;
    protected ConnectedThread connectedThread;

    private BluetoothAdapter bluetoothAdapter;

    private GameContext context;

    private ConnectionManager connectionManager;

    private BroadcastReceiver receiver;

    protected BluetoothManager(GameContext context, ConnectionManager connectionManager){
        this.context = context;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.connectionManager = connectionManager;
    }

    public boolean startDiscovery(BroadcastReceiver receiver){
        if (!bluetoothAdapter.startDiscovery())
            return false;

        this.receiver = receiver;

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.getAppContetxt().registerReceiver(receiver, filter);

        return true;

    }

    public void cancelDiscovery(){
        bluetoothAdapter.cancelDiscovery();
        try {
            if (receiver != null)
                context.getAppContetxt().unregisterReceiver(receiver);
        }
        catch (Exception e){}
    }


    public boolean haveBluetooth(){
        return bluetoothAdapter == null;
    }

    public boolean isEnabled(){
        return bluetoothAdapter.isEnabled();
    }

    public void activate(){

        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {

                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                context.sendIntentRequest(enableBluetooth);

            }
        }
    }

    public ArrayList<BluetoothDevice> getBondedDevices(){

        ArrayList<BluetoothDevice> lista = new ArrayList<>();

        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()){
            lista.add(device);
        }

        return lista;
    }

    public synchronized void start(){
        if (connectedThread != null){
            connectedThread.cancel();
            connectedThread = null;
        }

        if(connectThread != null){
            connectThread.cancel();
            connectThread = null;
        }

        if (acceptThread == null){
            acceptThread = new AcceptThread();
            acceptThread.start();
        }
    }

    public void stop(){
        try{
            if (connectedThread != null){
                connectedThread.cancel();
                connectedThread = null;
            }

            if(connectThread != null){
                connectThread.cancel();
                connectThread = null;
            }

            if (acceptThread != null){
                acceptThread.cancel();
                acceptThread = null;
            }
        }
        catch (Exception e){
            e.getLocalizedMessage();
        }


    }

    public synchronized void connect(BluetoothDevice device){
        if (connectedThread != null){
            connectedThread.cancel();
            connectedThread = null;
        }

        connectThread = new ConnectThread(device);
        connectThread.start();
    }

    private synchronized void connected(BluetoothSocket socket, int userID){


        if (connectThread != null){
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread != null){
            connectedThread.cancel();
            connectedThread = null;
        }

        if (acceptThread != null){
            acceptThread.cancel();
            acceptThread = null;
        }


        connectedThread = new ConnectedThread(socket, userID);
        connectedThread.start();
    }

    public void write(byte[] out){
        if (out == null)
            return;

        ConnectedThread c;
        synchronized (this) {
            c = connectedThread;
        }

        c.write(out);
    }

    private class AcceptThread extends Thread{

        private final BluetoothServerSocket serverSocket;

        public AcceptThread(){
            log("Thread para esperar conexão criada");
            BluetoothServerSocket tmp = null;
            try{
                tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            this.serverSocket = tmp;
        }

        @Override
        public void run(){
            BluetoothSocket socket;

            while (true){

                try{
                    log("Esperando uma conexão");
                    socket = this.serverSocket.accept();
                }
                catch (Exception e){
                    sendMessageToScreen("Não achou uma conexão. \n" + e.getMessage());
                    break;
                }

                if (socket != null){
                    log("Achou uma conexão. Conectando..");

                    synchronized (BluetoothManager.this){
                        connected(socket, Constant.GAMEMODE_MULTIPLAYER_HOST);
                    }

                    try {
                        this.serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }

        }

        public void cancel(){
            try {
                if (serverSocket != null)
                    this.serverSocket.close();
                //log("Encerrando thread");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectThread extends Thread{

        private final BluetoothSocket socket;
        private final BluetoothDevice device;

        public ConnectThread(BluetoothDevice device){

            log("Thread para conectar criada");

            BluetoothSocket tmp = null;
            this.device = device;

            try{
                tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            this.socket = tmp;
        }

        @Override
        public void run(){
            bluetoothAdapter.cancelDiscovery();

            try{
                log("Iniciando de conexão ao dispositivo de mac [" + device.getAddress() + "] e nome [" + device.getName() + "]");
                this.socket.connect();
            }
            catch (IOException connectException){
                try{
                    socket.close();
                    sendMessageToScreen("Não conectou. \n" + connectException.getMessage());
                }
                catch (IOException e){
                    //log("Não conectou. " + e.getMessage());
                }
                return;
            }
            log("Conectou!");

            synchronized (BluetoothManager.this) {
                connectThread = null;
            }

            if (!socket.isConnected())
                log("Thread Conectar: o socket não esta conectado");

            connected(socket, Constant.GAMEMODE_MULTIPLAYER_JOIN);
        }

        public void cancel(){
            try {
                if (socket != null)
                    this.socket.close();
                //log("Encerrando thread");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectedThread extends Thread{

        private final BluetoothSocket socket;
        private final InputStream in;
        private final OutputStream out;

        public ConnectedThread(BluetoothSocket socket, int userID){
            log("Thread criada. Ira tentar iniciar a comunicação entre os dispositivos");

            this.socket = socket;

            if (!socket.isConnected())
                log("Thread Conectado: O soquete não esta conectado");

            log("ID de conexão: " + userID);

            //gameEngine.connectionManager = new ConnectionManager(BluetoothManager.this);

            //new MultiplayerSelectCharacterScreen(context, userID);
            new SelectCharacterScreen(context, userID);


            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try{
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                log("Erro: " + e.getMessage());
            }

            in = tmpIn;
            out = tmpOut;
        }

        @Override
        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;


            while (true){
                try{

                    bytes = this.in.read(buffer);

                    connectionManager.readPacket(buffer, bytes);

                } catch (Exception e) {
                    log(e.getMessage());
                    break;
                }
            }
            log("Conexão encerrada");
            new MainScreen(context);
        }

        public void write(byte[] bytes){

            try{
                this.out.write(bytes);

            } catch (IOException e) {
                log(e.getMessage());
            }
        }

        public void cancel(){
            try {
                if (socket != null)
                    this.socket.close();
                log("Encerrando thread");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void log(String msg){
        Log.v("GameEngine/Bluetooth", GameParameters.getInstance().logIndex++ + ": " + msg);
    }


    private void sendMessageToScreen(String msg){
        log(msg);
        context.sendMessage(msg, 5);
    }

}
