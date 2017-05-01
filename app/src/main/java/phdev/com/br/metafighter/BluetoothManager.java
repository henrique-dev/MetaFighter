package phdev.com.br.metafighter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class BluetoothManager {

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

    public void check(){

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


    }

    public BluetoothAdapter getBluetoothAdapter(){
        return this.bluetoothAdapter;
    }

}
