package com.rattyduck.viz.scenes;

import java.util.ArrayList;
import java.util.List;

import com.rattyduck.viz.Scene;
import com.rattyduck.viz.models.Boid;
import com.rattyduck.viz.models.Box;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;
import processing.core.PVector;

public class FlockingScene extends Scene {
  private List<Boid> boids;
  private BeatDetect beat;
  private Box bounds;
  
  public FlockingScene(int width, int height, PGraphics g) {
    super(width, height, g, "Bugs");
    
    boids = new ArrayList<>();
    
    beat = new BeatDetect();
    beat.setSensitivity(300);
    
    bounds = new Box(new PVector(), width, height, 100);
  }
  
  public void start() {
    super.start();
    g.camera();
    
    for (int i = 0; i < 1000; i++) {
      boids.add(new Boid(new PVector(
          (float) Math.random() * bounds.getWidth(),
          (float) Math.random() * bounds.getHeight(),
          (float) Math.random() * bounds.getDepth())));
    }
  }
  
  public void kill() {
    super.kill();
  }
  
  public void render(int deltaMillis, AudioSource audio) {
    g.strokeWeight(3);
    g.stroke(255);
    PVector center = new PVector(width / 2, 0, 0);
    for (Boid b : boids) {
      //b.bound(bounds);
      b.acc.add(b.avoid(center));
      b.update(deltaMillis, boids);
      g.point(b.pos.x, b.pos.y, b.pos.z);
    }
  }
}

