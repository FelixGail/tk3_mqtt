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
        List<Service> espServices = adv.getServices();
        ads.add(adv);
        if(espServices != null) {
            services.addAll(espServices);
        }
    }

    public List<Service> getChannelList(){
        return new ArrayList<>(services);
    }

    public void updateChannelList() throws IOException, InterruptedException {
        Iterator it = ads.iterator();
        System.out.println("Sending ping requests...");
        while (it.hasNext()) {
            Advertisement adv = (Advertisement) it.next();

            InetAddress remote = InetAddress.getByName(adv.getIp());
            if(!remote.isReachable(5000)) {
                System.out.printf("Timeout. Removing '%s'\n", adv.getIp());
                if(adv.getServices() != null) {
                    services.removeAll(adv.getServices());
                }
                it.remove();
            }else{
                System.out.printf("Answer received from '%s'\n", adv.getIp());
            }
        }
    }
}
