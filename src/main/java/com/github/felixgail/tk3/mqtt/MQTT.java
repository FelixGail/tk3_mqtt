package com.github.felixgail.tk3.mqtt;

import java.net.SocketException;
import java.net.UnknownHostException;

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
    Option multicastAdress = Option.builder().argName("m")
        .hasArg()
        .desc("multicast address to listen on")
        .required(false)
        .longOpt("m")
        .build();
    Option multicastPort = Option.builder().argName("p")
        .hasArg()
        .desc("multicast port to listen on")
        .required(false)
        .longOpt("p")
        .build();
    Option networkInterface = Option.builder().argName("i")
        .hasArg()
        .desc("name of the network interface to listen on")
        .required(false)
        .longOpt("i")
        .build();
    Option mqttPort = Option.builder().argName("P")
        .hasArg()
        .desc("port of the mqtt server")
        .required(false)
        .longOpt("P")
        .build();
    Option help = Option.builder().argName("h")
        .hasArg(false)
        .desc("prints this help")
        .required(false)
        .longOpt("h")
        .build();
    Options options = new Options().addOption(help)
        .addOption(multicastAdress).addOption(multicastPort)
        .addOption(networkInterface).addOption(mqttPort);
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
        Integer.parseInt(line.getOptionValue("P", "1883"))
    );

    System.out.printf("Starting multicast client on %s:%d.\n", client.IP_ADDRESS, client.PORT);
    client.run();
  }

}
