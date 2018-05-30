package com.github.felixgail.tk3.mqtt;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class ChannelManager {
    private Set<Service> services = new HashSet<>();
    private Set<Advertisement> ads = new HashSet<>();

    public void addToChannelList(Advertisement adv){
        List<Service> espServices = adv.services;
        ads.add(adv);
        services.addAll(espServices);
    }

    public List<Service> getChannelList(){
        return new ArrayList<>(services);
    }

    public void updateChannelList() throws IOException {
        Iterator it = ads.iterator();
        while (it.hasNext()) {
            Advertisement adv = (Advertisement) it.next();

            // checks if ip is reachable
            Socket socket = new Socket(adv.ip, adv.port);
            socket.getOutputStream().write((byte) '\n');
            int ch = socket.getInputStream().read();
            socket.close();
            if (ch == 'n')
                continue;

            services.removeAll(adv.services);
            it.remove();
        }

    }
}
