package com.rattyduck.viz;

import javax.swing.JPanel;

import ddf.minim.AudioSource;
import processing.core.PGraphics;

public abstract class Scene {
  private transient boolean isFinished;
  
  protected int width;
  protected int height;
  protected String name;
  
  public Scene(int width, int height) {
    this.width = width;
    this.height = height;
  }
  
  public void start() {
    this.isFinished = false;
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

  public abstract void render(int deltaMillis, AudioSource audio, PGraphics g);
}