package com.rattyduck.viz.scenes;

import com.rattyduck.viz.Scene;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;

public class SetupScene extends Scene {
  private transient BeatDetect beat;

  public SetupScene() {
    beat = new BeatDetect();
    beat.detectMode(BeatDetect.FREQ_ENERGY);
    beat.setSensitivity(300);
  }
  
  @Override
  public void start() {
    super.start();
  }
  
  @Override
  public void render(int deltaMillis, AudioSource audio, PGraphics g) {
    g.stroke(255);
    g.strokeWeight(2);
    g.noFill();
    g.rect(0, 0, width, height);
    g.strokeWeight(1);
    g.line(0, 0, width, height);
    g.line(0, height, width, 0); 
  }
}