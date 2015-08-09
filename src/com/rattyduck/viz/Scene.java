package com.rattyduck.viz;

import ddf.minim.AudioSource;
import processing.core.PApplet;
import processing.core.PGraphics;

public abstract class Scene {
  private boolean isFinished;
  protected PGraphics g; 
  protected int width;
  protected int height;
  
  public Scene(int width, int height, PGraphics g) {
    this.g = g;
    this.width = width;
    this.height = height;
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

  public abstract void render(int deltaMillis, AudioSource audio);
}