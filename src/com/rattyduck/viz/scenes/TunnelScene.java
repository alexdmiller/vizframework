package com.rattyduck.viz.scenes;

import java.util.ArrayList;
import java.util.List;

import com.rattyduck.viz.Scene;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;
import processing.core.PConstants;
import processing.core.PGraphics;

public class TunnelScene extends Scene {
  private transient BeatDetect beat;
  
  private transient List<Float> circles = new ArrayList<>();

  public TunnelScene(int width, int height) {
    super(width, height);
    beat = new BeatDetect();
    beat.detectMode(BeatDetect.SOUND_ENERGY);
    beat.setSensitivity(300);
  }
  
  @Override
  public void start() {
    super.start();
  }
  
  @Override
  public void render(int deltaMillis, AudioSource audio, PGraphics g) {
    beat.detect(audio.mix);
    
    if (beat.isOnset()) {
      circles.add(-1000f);
    }
    
    g.camera();
    g.background(0);
    g.smooth();
    g.stroke(255);
    g.strokeWeight(2);
    g.noFill();
   
    g.ellipseMode(PConstants.RADIUS);
    
    for (int i = 0; i < circles.size(); i++) {
      g.pushMatrix();
      g.translate(0, 0, circles.get(i));
      g.ellipse(width / 2, height / 2, 100, 100);
      g.popMatrix();

      circles.set(i, circles.get(i) + 5);
    }
  }
}