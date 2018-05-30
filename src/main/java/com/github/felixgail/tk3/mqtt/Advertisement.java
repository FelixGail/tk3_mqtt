package com.github.felixgail.tk3.mqtt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Objects;

public class Advertisement {

    @Expose
    public String ip;
    @Expose
    public List<Service> services;
    @Expose int port;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public Advertisement(String ip, List<Service> services, int port){
        this.ip = ip;
        this.services = services;
        this.port = port;
    }

    @Override
    public String toString(){
        return gson.toJson(this);
    }

    @Override
    // advertisments are equal if they have the same ip, services and port
    public boolean equals(Object other){
        if (!(other instanceof Advertisement))
            return false;

        Advertisement adv = (Advertisement) other;
        boolean sameContent = this.services.containsAll(adv.services) && adv.services.containsAll(this.services);

        return  this.ip.equals(adv.ip) && sameContent && this.port == adv.port;
    }

    @Override
    public int hashCode(){
        return Objects.hash(ip, services, port);
    }
}
