package com.github.felixgail.tk3.mqtt;

import com.google.gson.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

public class MulticastClient {
    public MulticastSocket socket = null;
    public final String IP_ADDRESS = "239.0.0.1";
    public final int PORT = 5000;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public void run() throws IOException{
        try {
            socket = new MulticastSocket(PORT);
            InetAddress group = InetAddress.getByName(IP_ADDRESS);
            socket.setSoTimeout(15000);
            socket.joinGroup(group);
            do {
                DatagramPacket packet = receivePackage();
                Advertisement adv = dumpPacket(packet);

            } while (true);
        }
        catch (SocketTimeoutException e) {
            System.out.println("Timeout");
        }
    }

    private Advertisement dumpPacket(DatagramPacket packet){
        String received = new String(
                packet.getData(), 0, packet.getLength());
        return gson.fromJson(received, Advertisement.class);
    }

    private DatagramPacket receivePackage() throws IOException{
        byte[] buf = new byte[512];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        return packet;
    }

    public void sendPackage(Advertisement adv, InetAddress group) throws IOException{
        String dataString = gson.toJson(adv);
        byte[] buf = dataString.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
        socket.send(packet);
    }

}
