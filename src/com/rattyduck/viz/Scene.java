package com.rattyduck.viz;

import javax.swing.JPanel;

import ddf.minim.AudioSource;
import processing.core.PGraphics;

public abstract class Scene {
  private transient boolean isFinished;
  
  protected int width;
  protected int height;
  protected String name;
  
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

  public abstract void render(int deltaMillis, AudioSource audio, PGraphics g);
}