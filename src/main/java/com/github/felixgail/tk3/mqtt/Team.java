package com.github.felixgail.tk3.mqtt;

import com.google.gson.annotations.Expose;
import java.awt.Color;
import java.util.Random;

public class Team {

  @Expose
  private String id;
  @Expose
  private int red;
  @Expose
  private int green;
  @Expose
  private int blue;

  private static Random random = new Random();
  private static int createdTeams = 0;
  private enum PredefinedColors {
    RED(Color.RED), BLUE(Color.BLUE), GREEN(Color.GREEN), WHITE(Color.WHITE), ORANGE(Color.ORANGE);

    private Color color;
    PredefinedColors(Color color) {
      this.color = color;
    }

    public Color getColor() {
      return color;
    }
  }

  public Team(String id, int red, int green, int blue) {
    this.id = id;
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public Team(String id) {
    this.id = id;
    Color color = getNextColor();
    this.red = color.getRed();
    this.green = color.getGreen();
    this.blue = color.getBlue();
  }

  private static Color getNextColor() {
    if(createdTeams < PredefinedColors.values().length-1) {
      Color color = PredefinedColors.values()[createdTeams].getColor();
      createdTeams++;
      return color;
    }
    int red = random.nextInt(256);
    int green = random.nextInt(256);
    int blue = random.nextInt(256);
    return new Color(red, green, blue);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getRed() {
    return red;
  }

  public void setRed(int red) {
    this.red = red;
  }

  public int getGreen() {
    return green;
  }

  public void setGreen(int green) {
    this.green = green;
  }

  public int getBlue() {
    return blue;
  }

  public void setBlue(int blue) {
    this.blue = blue;
  }
}
