package com.github.felixgail.tk3.mqtt;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Service {

    @Expose
    public List<Sensor> sensors;
    @Expose
    public List<Actuator> actuators;

    public Service(List<Sensor> sensors, List<Actuator> actuators){
        this.sensors = sensors;
        this.actuators = actuators;
    }
}
