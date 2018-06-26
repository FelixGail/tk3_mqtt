package com.github.felixgail.tk3.mqtt;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

public class ChannelManager {
  private Map<String, Advertisement> ads = new HashMap<>();
  private Set<Service> nativeServices = new HashSet<>();

  public Set<Service> getServices() {
    Set<Service> services = new HashSet<>();
    getAds().forEach((key, value) -> value.getServiceOptional().ifPresent(services::addAll));
    services.addAll(nativeServices);
    return services;
  }

  public Map<String, Advertisement> getAds() {
    return ads;
  }

  public void addToChannelList(Advertisement adv) {
    List<Service> espServices = adv.getServices();
    ads.put(adv.getIp(), adv);
  }

  public void addToNativeServices(Service service) {
    nativeServices.add(service);
  }

  public Set<Service> getNativeServices() {
    return nativeServices;
  }

  public void updateChannelList() throws IOException {
    Iterator it = ads.keySet().iterator();
    System.out.println("Sending ping requests...");
    while (it.hasNext()) {
      String ip = (String) it.next();

      InetAddress remote = InetAddress.getByName(ip);
      if (!remote.isReachable(5000)) {
        System.out.printf("Timeout. Removing '%s'\n", ip);
        it.remove();
      } else {
        System.out.printf("Answer received from '%s'\n", ip);
      }
    }
  }
}
