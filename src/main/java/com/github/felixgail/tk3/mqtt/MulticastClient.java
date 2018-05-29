package com.github.felixgail.tk3.mqtt;

import com.google.gson.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class MulticastClient {
    public final String IP_ADDRESS;
    public final int PORT;
    private MulticastSocket socket = null;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private ChannelManager cm = new ChannelManager();

    public MulticastClient(String ip_address, int port){
        this.IP_ADDRESS = ip_address;
        this.PORT = port;
    }

    public void run(){
        try {
            socket = new MulticastSocket(PORT);
            InetAddress group = InetAddress.getByName(IP_ADDRESS);
            socket.setSoTimeout(15000);
            socket.joinGroup(group);
            do {
                // check if there are unavailable devices and remove them
                cm.updateChannelList();

                // receive
                DatagramPacket packet = receivePackage();
                Advertisement adv = dumpPacket(packet);

                // send
                List<Sensor> sensors = new ArrayList<>(cm.sensors);
                List<Actuator> actuators = new ArrayList<>(cm.actuators);
                Advertisement response = new Advertisement(IP_ADDRESS, new Service(sensors, actuators), PORT);
                sendPackage(response, InetAddress.getByName(adv.ip), adv.port);

                // add received channels to channel list
                cm.addToChannelList(adv);

            } while (true);
        }
        catch (IOException e) {
            e.printStackTrace();
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

    public void sendPackage(Advertisement adv, InetAddress ip, int port) throws IOException{
        String dataString = gson.toJson(adv);
        byte[] buf = dataString.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, port);
        socket.send(packet);
    }

}
