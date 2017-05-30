package com.br.phdev.metafighter;

import android.util.Log;

import com.br.phdev.metafighter.GameParameters;
import com.br.phdev.metafighter.cmp.connections.packets.Packet;
import com.br.phdev.metafighter.cmp.misc.GameContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public final class ConnectionManager {

    private boolean active;

    private BluetoothManager bluetoothManager;

    //private GameContext context;

    private LinkedList<Packet> packetsToRead;

    private LinkedList<Packet> packetsToWrite;

    public ConnectionManager(GameContext context){
        //this.context = context;
        bluetoothManager = new BluetoothManager(context, this);
    }

    public void init(){
        active = true;
        packetsToRead = new LinkedList<>();
        packetsToWrite = new LinkedList<>();
    }

    public boolean isActive() {
        return active;
    }

    public BluetoothManager getBluetoothManager(){
        return this.bluetoothManager;
    }

    public void addPacketsToWrite(Packet packet){
        this.packetsToWrite.add(packet);
    }

    public void addPacketsToRead(Packet packet){
        this.packetsToRead.add(packet);
    }

    public Packet receivePackets(){
        return getCurrentPacketToRead();
    }

    public void sendPackets(){
        bluetoothManager.write(writePacket(getCurrentPacketToWrite()));
    }

    private Packet getCurrentPacketToRead(){
        if (packetsToRead.size() > 0){
            return packetsToRead.pop();
        }
        return null;
    }

    private Packet getCurrentPacketToWrite(){
        if (packetsToWrite.size() > 0){
            return packetsToWrite.pop();
        }
        return null;
    }

    public void readPacket(byte[] buffer, int bytes) {

        ByteArrayInputStream bis = new ByteArrayInputStream(buffer, 0, bytes);
        ObjectInput in = null;

        Packet tmpPacket = null;

        try {
            in = new ObjectInputStream(bis);
            tmpPacket = (Packet)in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (tmpPacket != null)
            addPacketsToRead(tmpPacket);
            //packetsToRead.add(tmpPacket);
    }

    private byte[] writePacket(Packet packet) {

        if (packet == null)
            return null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;

        byte[] bytes = null;

        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(packet);
            out.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    @Deprecated
    private void log(String msg){
        Log.v("GameEngine/Bluetooth", GameParameters.getInstance().logIndex++ + ": " + msg);
    }


}
