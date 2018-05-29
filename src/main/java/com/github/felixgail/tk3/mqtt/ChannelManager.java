package com.github.felixgail.tk3.mqtt;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class ChannelManager {
    public Set<Sensor> sensors = new HashSet<>();
    public Set<Actuator> actuators = new HashSet<>();
    public Set<Advertisement> ads = new HashSet<>();

    public void addToChannelList(Advertisement adv){
        List<Sensor> espSensors = adv.services.sensors;
        List<Actuator> espActuators = adv.services.actuators;
        ads.add(adv);

        sensors.addAll(espSensors);
        actuators.addAll(espActuators);
    }

    public void updateChannelList() throws IOException {
        Iterator it = ads.iterator();
        while (it.hasNext()) {
            Advertisement adv = (Advertisement) it.next();
            Socket socket = new Socket(adv.ip, adv.port);
            socket.getOutputStream().write((byte) '\n');
            int ch = socket.getInputStream().read();
            socket.close();
            if (ch != '\n') {
                sensors.remove(adv.services.sensors);
                actuators.remove(adv.services.actuators);
                it.remove();
            }
        }

    }
}
