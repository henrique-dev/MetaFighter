package phdev.com.br.metafighter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    protected GameEngine gameEngine;
    private BluetoothAdapter bluetoothAdapter;

    private static final BluetoothManager instance = new BluetoothManager();

    private BluetoothManager(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static BluetoothManager getInstance(){
        return instance;
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
                this.gameEngine.getContext().startActivity(enableBluetooth);

            }
        }
    }

    public ArrayList<BluetoothDevice> getBondedDevices(){

        ArrayList<BluetoothDevice> lista = new ArrayList<>();

        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()){
            lista.add(device);
        }

        return lista;

        //return bluetoothAdapter.getBondedDevices();
        /*
        List<String> lista = new ArrayList<>();

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                lista.add(device.getName() + "\n" + device.getAddress());
                Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": " + device.getName() + "\n" + device.getAddress());
            }
        }
        */
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
            Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Thread criada.");
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
            BluetoothSocket socket = null;

            while (true){

                try{
                    socket = this.serverSocket.accept();
                    Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Esperando.");
                }
                catch (Exception e){
                    break;
                }

                if (socket != null){
                    //manageConnectedSocket(socket);
                    Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Achou.");
                    connected(socket);
                    try {
                        this.serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Saiu.");
        }

        public void cancel(){
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectThread extends Thread{

        private final BluetoothSocket socket;
        private final BluetoothDevice device;

        public ConnectThread(BluetoothDevice device){
            Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Thread criada111111111111.");
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
                this.socket.connect();
            }
            catch (IOException connectException){
                try{
                    socket.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                return;
            }

            //manageConnectedSocket(socket);
            connected(socket);
        }

        public void cancel(){
            try {
                this.socket.close();
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
            Log.v("GameEngine", GameParameters.getInstance().logIndex++ + ": Thread criada.");

            new MatchScreen();

            this.socket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try{
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
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
                    bytes = in.read(buffer);

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
                out.write(bytes);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel(){
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
