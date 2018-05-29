package com.github.felixgail.tk3.mqtt;

import com.google.gson.annotations.Expose;

public class Sensor {

    @Expose
    public String name;
    @Expose
    public String service_type;
    @Expose
    public String channel;

    public Sensor(String name, String service_type, String channel){
        this.name = name;
        this.service_type = service_type;
        this.channel = channel;
    }
}
