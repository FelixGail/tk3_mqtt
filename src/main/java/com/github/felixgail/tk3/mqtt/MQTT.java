package com.github.felixgail.tk3.mqtt;

public class MQTT {

  public static void main(String[] args) {
    MulticastClient client = new MulticastClient("239.0.0.57", 5000, "wlan0");
    System.out.printf("Starting multicast client on %s:%d.\n", client.IP_ADDRESS, client.PORT);
    client.run();
  }

}
