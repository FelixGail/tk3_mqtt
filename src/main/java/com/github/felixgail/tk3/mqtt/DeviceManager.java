package com.github.felixgail.tk3.mqtt;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.Map.Entry;

public class DeviceManager {
  private Map<String, Advertisement> ads = new HashMap<>();
  private Set<Service> nativeServices = new HashSet<>();
  private Map<Team, Map<String, Player>> players;
  private Map<String, Player> beacons;
  private final Team beaconTeam = new Team("beacon", 0, 0, 0);

  public DeviceManager(int teamCount) {
    players = new HashMap<>(Math.max(teamCount, 1));
    beacons = new HashMap<>();
    for(int i = 0; i<teamCount; i++) {
      players.put(new Team(String.valueOf(i)), new HashMap<>());
    }
    if(teamCount < 0) {
      players.put(new Team("-1", 255, 255, 255), new HashMap<>());
    }
  }

  public Set<Service> getServices() {
    Set<Service> services = new HashSet<>();
    getAds().forEach((key, value) -> value.getServiceOptional().ifPresent(services::addAll));
    services.addAll(nativeServices);
    return services;
  }

  public Map<String, Advertisement> getAds() {
    return ads;
  }

  public Player addToChannelList(Advertisement adv) {
    ads.put(adv.getIp(), adv);
    if(adv.isBeacon()) {
    	if(beacons.containsKey(adv.getIp())) {
    		return beacons.get(adv.getIp());
    	}
    	Player beacon = new Player(beaconTeam, adv.getIp());
    	beacons.put(adv.getIp(), beacon);
    	return beacon;
    }
    Entry<Team, Map<String, Player>> smallestTeam = null;
    for(Entry<Team, Map<String, Player>> entry : players.entrySet()) {
      if(smallestTeam == null || smallestTeam.getValue().size()>entry.getValue().size()) {
        smallestTeam = entry;
      }
      if(entry.getValue().containsKey(adv.getIp())) {
        return entry.getValue().get(adv.getIp());
      }
    }
    Player newPlayer = new Player(smallestTeam.getKey(), adv.getIp());
    smallestTeam.getValue().put(adv.getIp(), newPlayer);
    return newPlayer;
  }

  public void addToNativeServices(Service service) {
    nativeServices.add(service);
  }

  public Set<Service> getNativeServices() {
    return nativeServices;
  }

  public void updateLists() throws IOException {
    Iterator<String> it = ads.keySet().iterator();
    System.out.println("Sending ping requests...");
    while (it.hasNext()) {
      String ip = it.next();

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
