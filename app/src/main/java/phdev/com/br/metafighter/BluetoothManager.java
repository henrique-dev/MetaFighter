package phdev.com.br.metafighter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import phdev.com.br.metafighter.cmp.event.EventListener;
import phdev.com.br.metafighter.cmp.event.IntentListener;
import phdev.com.br.metafighter.cmp.event.MessageListener;
import phdev.com.br.metafighter.screens.MatchScreen;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class BluetoothManager {

    public int data = -1;

    private static final String NAME = "MetaFighter";
    private static final UUID MY_UUID = UUID.fromString("287c5f5a-5140-47af-bd32-d8db4ec9728d");

    protected AcceptThread acceptThread;
    protected ConnectThread connectThread;
    protected ConnectedThread connectedThread;

    private BluetoothAdapter bluetoothAdapter;

    protected BluetoothManager manager;

    protected EventListener listener;

    public BluetoothManager(EventListener listener){
        this.listener = listener;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        manager = this;
    }

    public boolean isEnabled(){
        return bluetoothAdapter.isEnabled();
    }

    public void activate(){

        if (bluetoothAdapter == null) {

        }
        else {

            if (!bluetoothAdapter.isEnabled()) {

                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((IntentListener)listener).sendIntentRequest(enableBluetooth);

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

    public synchronized void connected(BluetoothSocket socket){
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

        connectedThread = new ConnectedThread(socket);
        connectedThread.start();
    }

    public void write(byte[] out){
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
            Looper.prepare();
            BluetoothSocket socket = null;

            while (true){

                try{
                    log("Esperando uma conexão");
                    socket = this.serverSocket.accept();
                }
                catch (Exception e){
                    log("Não achou uma conexão. " + e.getMessage());
                    break;
                }

                if (socket != null){
                    log("Achou uma conexão. Conectando..");
                    connected(socket);
                    try {
                        this.serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            Looper.loop();
        }

        public void cancel(){
            try {
                if (serverSocket != null)
                    this.serverSocket.close();
                log("Encerrando thread");
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
                    /*
                    Looper.prepare();
                    sendMessage("Não conectou. " + connectException.getMessage());
                    Looper.loop();
                    */
                    log("Não conectou. " + connectException.getMessage());
                }
                catch (IOException e){
                    //log("Não conectou. " + e.getMessage());
                }
                return;
            }
            log("Conectou!");
            connected(socket);
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

    private class ConnectedThread extends Thread{

        private final BluetoothSocket socket;
        private final InputStream in;
        private final OutputStream out;

        public ConnectedThread(BluetoothSocket socket){
            log("Thread criada. Ira tentar iniciar a comunicação entre os dispositivos");
            new MatchScreen(listener, manager);

            this.socket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try{
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                log("Erro: " + e.getMessage());
            }

            this.in = tmpIn;
            this.out = tmpOut;
        }

        @Override
        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;

            while (true){
                try{
                    bytes = this.in.read(buffer);

                    if (bytes != -1){
                        data = bytes;
                    }
                    else
                        data = -1;

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void write(byte[] bytes){
            try{
                this.out.write(bytes);

            } catch (IOException e) {
                e.printStackTrace();
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

    @Deprecated
    private void log(String msg){
        Log.v("GameEngine/Bluetooth", GameParameters.getInstance().logIndex++ + ": " + msg);
    }

    @Deprecated
    private void sendMessage(String msg){
        log(msg);
        ((MessageListener)listener).sendToast(msg, Toast.LENGTH_LONG);
    }

}
