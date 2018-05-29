package com.github.felixgail.tk3.mqtt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class ExampleClass {

  public static void main(String [ ] args)
  {
      // Test
      Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
      Actuator act = new Actuator("bla", "bla", "bla");
      Sensor sens = new Sensor("bla", "bla", "bla");
      List<Sensor> sensors = new ArrayList<Sensor>();
      sensors.add(sens);
      List<Actuator> actuators = new ArrayList<Actuator>();
      actuators.add(act);
      Service serv = new Service(sensors, actuators);
      Advertisement test = new Advertisement("bla", serv);
      String json = gson.toJson(test);
      System.out.println(json);
  }

}
