package com.github.felixgail.tk3.mqtt;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Service {

  @Expose
  public String name;
  @Expose
  @SerializedName("service-type")
  public String serviceType;
  @Expose
  public String channel;

  public Service(String name, String serviceType, String channel) {
    this.name = name;
    this.serviceType = serviceType;
    this.channel = channel;
  }

  @Override
  // services are equal if they have the same name, service-type and channel
  public boolean equals(Object other) {
    if (!(other instanceof Service))
      return false;
    Service serv = (Service) other;
    return serv.name.equals(this.name) && serv.serviceType.equals(this.serviceType) && serv.channel.equals(this.channel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, serviceType, channel);
  }
}
