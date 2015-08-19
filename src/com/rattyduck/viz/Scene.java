package com.rattyduck.viz;

import javax.swing.JPanel;

import ddf.minim.AudioSource;
import processing.core.PGraphics;

public abstract class Scene implements Controllable {
  private transient boolean isFinished;
  protected transient PGraphics g;
  
  protected int width;
  protected int height;
  protected String name;
  
  public Scene(int width, int height, PGraphics g, String name) {
    this.g = g;
    this.width = width;
    this.height = height;
    this.name = name;
  }
  
  public void start() {
    isFinished = false;
  }
  
  public void kill() {
    isFinished = true;
  }
  
  public boolean isFinished() {
    return isFinished;
  }
  
  public String getName() {
    return name;
  }
  
  public JPanel getControlPanel() {
    return new JPanel();
  }

  public abstract void render(int deltaMillis, AudioSource audio);
}