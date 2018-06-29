package com.github.felixgail.tk3.mqtt;

import com.google.gson.annotations.Expose;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Advertisement {

  @Expose
  private String ip;
  @Expose
  private int port;
  @Expose
  private List<Service> services;
  @Expose
  private Team team;
  @Expose
  @SerializedName("player_id")
  private String playerID;
  @Expose
  private boolean beacon;

  public Advertisement(String ip, List<Service> services, int port) {
    this.ip = ip;
    this.services = services;
    this.port = port;
  }

  @Override
  // Advertisements are equal if they have the same ip, services and port
  public boolean equals(Object other) {
    if (!(other instanceof Advertisement))
      return false;

    Advertisement adv = (Advertisement) other;
    boolean sameContent = this.services.containsAll(adv.services) && adv.services.containsAll(this.services);

    return this.ip.equals(adv.ip) && sameContent && this.port == adv.port;
  }

  @Override
  public int hashCode() {
    return Objects.hash(ip, services, port);
  }

  public String getIp() {
    return ip;
  }

  public List<Service> getServices() {
    return services;
  }

  public int getPort() {
    return port;
  }

  public boolean hasServices() {
    return services != null;
  }

  public Optional<List<Service>> getServiceOptional() {
    return Optional.ofNullable(services);
  }

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  public String getPlayerID() {
    return playerID;
  }

  public void setPlayerID(String playerID) {
    this.playerID = playerID;
  }

  public boolean isBeacon() {
	return beacon;
  }

  public void setBeacon(boolean beacon) {
	this.beacon = beacon;
  }
  
}
