package com.github.felixgail.tk3.mqtt;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Player {

  private String id;
  private Team team;
  private String ip;

  private static int nextID = 0;
  private static int getNextID() {
    return nextID++;
  }

  public Player(String id, Team team, String ip) {
    this.id = id;
    this.team = team;
    this.ip = ip;
  }

  public Player(Team team, String ip) {
    this(String.valueOf(getNextID()), team, ip);
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
