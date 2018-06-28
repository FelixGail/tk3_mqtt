package com.github.felixgail.tk3.mqtt;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Player {

  private String id;
  private Team team;
  private String ip;

  private final static int modulo = 6;
  private static int nextID = 27;
  private static String getNextID() {
    String id = String.format("%d.%d.%d.%d.%d",
        genIDPart(4), genIDPart(3), genIDPart(2), genIDPart(1), genIDPart(0));
    nextID++;
    return id;
  }

  private static int genIDPart(int exp) {
    return ((nextID/(int)Math.pow(modulo,exp))%modulo)*1000;
  }

  public Player(String id, Team team, String ip) {
    this.id = id;
    this.team = team;
    this.ip = ip;
  }

  public Player(Team team, String ip) {
    this(getNextID(), team, ip);
  }

  public String getId() {
    return id;
  }

  public Team getTeam() {
    return team;
  }

  public InetAddress getIp() throws UnknownHostException {
    return InetAddress.getByName(ip);
  }
}
