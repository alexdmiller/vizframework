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
    
    bounds = new Box(new PVector(0, 0, -1200), width, height, 1000);
  }
  
  public void start() {
    super.start();
    g.camera();
    
    for (int i = 0; i < 1000; i++) {
      boids.add(new Boid(new PVector(
          (float) (bounds.getPosition().x + Math.random() * bounds.getWidth()),
          (float) (bounds.getPosition().y + Math.random() * bounds.getHeight()),
          (float) (bounds.getPosition().z + Math.random() * bounds.getDepth()))));
    }
  }
  
  public void kill() {
    super.kill();
  }
  
  public void render(int deltaMillis, AudioSource audio) {
    g.strokeWeight(3);
    g.stroke(255);
    for (Boid b : boids) {
      b.bound(bounds);
      b.update(deltaMillis, boids);
      
      g.stroke(255);
      g.point(b.pos.x, b.pos.y, b.pos.z);
    }
    
    g.stroke(255);
    g.noFill();
    g.pushMatrix();
    g.translate(
        bounds.getPosition().x + bounds.getWidth() / 2,
        bounds.getPosition().y + bounds.getHeight() / 2,
        bounds.getPosition().z + bounds.getDepth() / 2);
    g.box(bounds.getWidth(), bounds.getHeight(), bounds.getDepth());
    g.popMatrix();
  }
}
