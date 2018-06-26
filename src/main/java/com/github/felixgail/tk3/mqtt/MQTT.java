package com.github.felixgail.tk3.mqtt;

import java.net.SocketException;
import java.net.UnknownHostException;

import java.util.Arrays;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class MQTT {

  public static void main(String[] args)
      throws SocketException, UnknownHostException, ParseException {
    Option multicastAdress = Option.builder("m")
        .hasArg()
        .desc("multicast address to listen on")
        .required(false)
        .longOpt("multicast-address")
        .build();
    Option multicastPort = Option.builder("p")
        .hasArg()
        .desc("multicast port to listen on")
        .required(false)
        .longOpt("multicast-port")
        .build();
    Option networkInterface = Option.builder("i")
        .hasArg()
        .desc("name of the network interface to listen on")
        .required(false)
        .longOpt("interface")
        .build();
    Option mqttIp = Option.builder("l")
        .hasArg()
        .desc("mqtt ip to advertise")
        .required(false)
        .longOpt("mqtt-ip")
        .build();
    Option mqttPort = Option.builder("P")
        .hasArg()
        .desc("port of the mqtt server")
        .required(false)
        .longOpt("mqtt-port")
        .build();
    Option help = Option.builder("h")
        .hasArg(false)
        .desc("prints this help")
        .required(false)
        .longOpt("help")
        .build();
    Option teamCount = Option.builder("t")
        .hasArg()
        .desc("number of teams. -1 for FreeForAll mode")
        .required(false)
        .longOpt("teamcount")
        .build();
    Options options = new Options().addOption(help)
        .addOption(multicastAdress).addOption(mqttIp).addOption(multicastPort)
        .addOption(networkInterface).addOption(mqttPort).addOption(teamCount);
    CommandLineParser parser = new DefaultParser();
    CommandLine line = parser.parse(options, args);
    if (line.hasOption("h")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("mqtt", options);
      System.exit(0);
    }
    MulticastClient client = new MulticastClient(
        line.getOptionValue("m", "239.0.0.57"),
        Integer.parseInt(line.getOptionValue("p", "5000")),
        line.getOptionValue("i", "wlan0"),
        Integer.parseInt(line.getOptionValue("P", "1883")),
        line.getOptionValue("l", null),
        Integer.parseInt(line.getOptionValue("t", "-1"))
    );

    Service blaster = new Service("Blaster Hits", "d9f7b2c1-411f-467a-9137-21a19698697d", "hits");
    Service spacial = new Service("Spacial Context", "3c7c4d11-2be6-4869-ac9d-ad6d378b7ce3", "spacial");
    Service beacon = new Service("Beacon Information", "9baa2da0-2138-4709-bfba-ad8611dfabf3", "beacon");
    client.getChannelManager().getNativeServices().addAll(Arrays.asList(blaster, spacial, beacon));

    System.out.printf("Starting multicast client on '%s:%d'.\n", client.IP_ADDRESS, client.PORT);
    client.run();
  }

}
