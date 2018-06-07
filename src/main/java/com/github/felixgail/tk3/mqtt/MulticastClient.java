package com.github.felixgail.tk3.mqtt;

import com.google.gson.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.util.ArrayList;
import java.util.Map.Entry;

public class MulticastClient {

  public final int MQTT_PORT;
  public final String INTERFACE;
  public final String IP_ADDRESS;
  public final int PORT;
  public final String LOCAL_IP;
  private DatagramChannel dc;
  private MembershipKey key;
  private Gson gson = new GsonBuilder().setLenient().excludeFieldsWithoutExposeAnnotation().create();
  private ChannelManager cm = new ChannelManager();
  private ByteBuffer byteBufferReceive = ByteBuffer.allocate(5000);
  private ByteBuffer byteBufferSend = ByteBuffer.allocate(5000);

  public MulticastClient(String ip_address, int port, String networkInterface, int mqttPort)
      throws SocketException, UnknownHostException {
    this.IP_ADDRESS = ip_address;
    this.PORT = port;
    this.INTERFACE = networkInterface;
    this.MQTT_PORT = mqttPort;
    // send
    final DatagramSocket socket = new DatagramSocket();
    socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
    LOCAL_IP = socket.getLocalAddress().getHostAddress();
  }

  public void run() {
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
        if (packet != null && !packet.isEmpty()) {
          Advertisement adv = gson.fromJson(packet, Advertisement.class);

          Advertisement response = new Advertisement(LOCAL_IP, new ArrayList<>(cm.getServices()), MQTT_PORT);
          sendPackage(response, InetAddress.getByName(adv.getIp()), adv.getPort());
          for (Entry<String, Advertisement> entry : cm.getAds().entrySet()) {
            if (!entry.getKey().equals(adv.getIp())) {
              Advertisement client = entry.getValue();
              System.out.printf("Will send to %s: %s\n", client.getIp(), gson.toJson(adv));
              sendPackage(response, InetAddress.getByName(client.getIp()), client.getPort());
            }
          }

          // add received channels to channel list
          cm.addToChannelList(adv);
        }
      } while (true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String receivePackage() throws IOException {
    if (key.isValid()) {
      ((Buffer) byteBufferReceive).rewind();
      InetSocketAddress sa = (InetSocketAddress) dc.receive(byteBufferReceive);
      String msg = new String(byteBufferReceive.array(), 0, byteBufferReceive.position());
      System.out.println("Multicast received from " + sa.getHostString() + ": " + msg);
      return msg;
    } else {
      System.out.println("Membership key invalid.");
    }
    return "";
  }

  private void sendPackage(Advertisement adv, InetAddress ip, int port) throws IOException {
    String dataString = gson.toJson(adv);
    ((Buffer) byteBufferSend).clear();
    byteBufferSend.put(dataString.getBytes());
    ((Buffer) byteBufferSend).flip();
    System.out.printf("Sending packet to '%s:%d': %s\n", ip, port, dataString);
    dc.send(byteBufferSend, new InetSocketAddress(ip, port));
  }

}
