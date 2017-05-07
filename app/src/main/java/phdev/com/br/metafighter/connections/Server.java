package phdev.com.br.metafighter.connections;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import phdev.com.br.metafighter.cmp.Component;
import phdev.com.br.metafighter.cmp.event.listeners.ConnectionListener;
import phdev.com.br.metafighter.cmp.event.listeners.EventListener;
import phdev.com.br.metafighter.cmp.event.listeners.PacketListener;
import phdev.com.br.metafighter.cmp.window.Screen;
import phdev.com.br.metafighter.connections.packets.Move;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Server {

    ConnectionListener listener;
    PacketListener packetListener;

    //private List<Move> packetsToRead;
    //private List<Move> packetsToWrite;

    private int currentIndexRead = -1;
    private int currentIndexWrite = -1;

    public Server(){
        //packetsToRead = new ArrayList<>();
        //packetsToWrite = new ArrayList<>();

        listener = new ConnectionListener() {
            @Override
            public void read(byte[] bytes) {
                //setPacketsToRead(readPacket(bytes));
                packetListener.send(readPacket(bytes));
            }

            @Override
            public byte[] write() {
                //return writePacket(getPacketToWrite());
                return writePacket(packetListener.receive());
            }
        };

    }

    public void setListener(ConnectionListener listener){
        this.listener = listener;
    }


    public ConnectionListener getListener(){
        return this.listener;
    }

    public void setPacketListener(PacketListener packetListener){
        this.packetListener = packetListener;
    }


    /*
    public void setPacketsToRead(Move move){
        packetsToRead.add(move);
        currentIndexRead++;
    }

    public Move getPacketToRead(){
        return packetsToRead.get(currentIndexRead);
    }

    public void setPacketsToWrite(Move move){
        packetsToWrite.add(move);
        currentIndexWrite++;
    }

    public Move getPacketToWrite(){
        return packetsToWrite.get(currentIndexWrite);
    }
    */


    public Move readPacket(byte[] bytes) {

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;

        Move tmpMove = null;

        try {
            in = new ObjectInputStream(bis);
            tmpMove = (Move)in.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return tmpMove;
    }

    public byte[] writePacket(Move move) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;

        byte[] bytes = null;

        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(move);
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
}
