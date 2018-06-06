package com.github.felixgail.tk3.mqtt;

import com.google.gson.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;

public class MulticastClient {

    public final String INTERFACE;
    public final String IP_ADDRESS;
    public final int PORT;
    private DatagramChannel dc;
    private MembershipKey key;
    private Gson gson = new GsonBuilder().setLenient().excludeFieldsWithoutExposeAnnotation().create();
    private ChannelManager cm = new ChannelManager();
    private ByteBuffer byteBuffer = ByteBuffer.allocate(1500);

    public MulticastClient(String ip_address, int port, String networkInterface){
        this.IP_ADDRESS = ip_address;
        this.PORT = port;
        this.INTERFACE = networkInterface;
    }

    public void run(){
        try {
            NetworkInterface ni = NetworkInterface.getByName(INTERFACE);

            dc = DatagramChannel.open(StandardProtocolFamily.INET)
                .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                .bind(new InetSocketAddress(PORT))
                .setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);

            InetAddress group = InetAddress.getByName(IP_ADDRESS);

            key = dc.join(group, ni);

            new Thread(() -> {
                // check if there are unavailable devices and remove them
                try {
                    while (true) {
                        cm.updateChannelList();
                        Thread.sleep(5000);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            do {
                String packet = receivePackage();
                if(packet!=null && !packet.isEmpty()){
                    Advertisement adv = gson.fromJson(packet, Advertisement.class);

                    // send
                    Advertisement response = new Advertisement(IP_ADDRESS, cm.getChannelList(), PORT);
                    sendPackage(response, InetAddress.getByName(adv.getIp()), adv.getPort());
                    for(Advertisement client : cm.getAds()) {
                        sendPackage(response, InetAddress.getByName(client.getIp()), client.getPort());
                    }

                    // add received channels to channel list
                    cm.addToChannelList(adv);
                }
            } while (true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String receivePackage() throws IOException{
        if(key.isValid()){
            ((Buffer)byteBuffer).rewind();
            InetSocketAddress sa = (InetSocketAddress) dc.receive(byteBuffer);
            String msg = new String(byteBuffer.array(), 0, byteBuffer.position());
            System.out.println("Multicast received from " + sa.getHostString() + ": " +msg);
            return msg;
        }else {
            System.out.println("Membership key invalid.");
        }
        return "";
    }

    private void sendPackage(Advertisement adv, InetAddress ip, int port) throws IOException{
        String dataString = gson.toJson(adv);
        byte[] buf = dataString.getBytes();
        ((Buffer)byteBuffer).clear();
        byteBuffer.put(buf);
        System.out.printf("Sending packet to '%s:%d': %s\n", ip, port, dataString);
        dc.send(byteBuffer, new InetSocketAddress(ip, port));
    }

}
