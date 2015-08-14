package com.rattyduck.viz.scenes;

import java.util.ArrayList;
import java.util.List;

import com.rattyduck.viz.Scene;
import com.rattyduck.viz.models.Boid;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;
import processing.core.PVector;

public class FlockingScene extends Scene {
  private List<Boid> boids;
  private BeatDetect beat;
  
  public FlockingScene(int width, int height, PGraphics g) {
    super(width, height, g, "Bugs");
    
    boids = new ArrayList<>();
    
    beat = new BeatDetect();
    beat.setSensitivity(300);
  }
  
  public void start() {
    super.start();
    g.camera();
    
    for (int i = 0; i < 100; i++) {
      boids.add(new Boid(new PVector(
          (float) Math.random() * width,
          (float) Math.random() * height,
          (float) Math.random() * 200)));
    }
  }
  
  public void kill() {
    super.kill();
  }
  
  public void render(int deltaMillis, AudioSource audio) {
    g.strokeWeight(10);
    g.stroke(255);
    for (Boid b : boids) {
      b.update(deltaMillis, boids);
      g.point(b.pos.x, b.pos.y, b.pos.z);
    }
  }
}

