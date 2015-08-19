package com.rattyduck.viz.scenes;

import java.util.ArrayList;
import java.util.Iterator;

import com.rattyduck.viz.Scene;
import com.rattyduck.viz.models.Sphere;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;

public class HyperSpaceScene extends Scene {
  public float sphereSize = 200;
  
  private transient ArrayList<Sphere> spheres;
  private transient BeatDetect beat;

  public HyperSpaceScene(int width, int height) {
    super(width, height);
    spheres = new ArrayList<Sphere>();
    beat = new BeatDetect();
    beat.detectMode(BeatDetect.FREQ_ENERGY);
    beat.setSensitivity(300);
  }
  
  public void start() {
    super.start();    
  }
  
  public void kill() {
    super.kill();
    spheres.clear();
  }
  
  public void render(int deltaMillis, AudioSource audio, PGraphics g) {
    g.camera(0, 0, 0, 0, 0, 100, 0, -1, 0);
    g.background(0);
    beat.detect(audio.mix);
    if (beat.isRange(0, 10, 4)) {
      for (int i = 0; i < audio.mix.level() * 50; i++) {
        Sphere s = new Sphere((float) Math.random() * 400 - 200, (float) Math.random() * 400 - 200, 1000f, 0xffffffff, 0.2f, 0, 20, 2);
        s.vz = -20;
        spheres.add(s);
      }
    }
    
    Iterator<Sphere> i = spheres.iterator();
    while (i.hasNext()) {
      Sphere s = i.next();
      if (s.z == 0) {
        i.remove();
      } else {
        s.update(beat, audio);
        g.strokeWeight(s.radius);
        g.stroke(s.c);
        g.point(s.x, s.y, s.z);
      }
    }
  }
}

