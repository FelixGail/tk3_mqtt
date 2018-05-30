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
      Service serv1 = new Service("hall_sensor", "7644a627-1b4e-47d0-b8bc-6a01813e99f6", "ch1");
      Service serv2 = new Service("led", "7644a627-1b4e-l3d0-b8bc-6a01813e99f9", "response_channel1");
      List<Service> services = new ArrayList<>();
      services.add(serv1);
      services.add(serv2);
      Advertisement test = new Advertisement("bla", services, 25);
      System.out.println(test.toString());
  }

}
