package com.github.felixgail.tk3.mqtt;

import com.google.gson.annotations.Expose;

public class Advertisement {

    @Expose
    public String ip;
    @Expose
    public Service services;

    public Advertisement(String ip, Service services){
        this.ip = ip;
        this.services = services;
    }
}
