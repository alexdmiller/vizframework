package com.rattyduck.viz.renderers;

import com.rattyduck.viz.models.Boids.Boid;

import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;

public abstract class BoidRenderer {
  protected Boid boid;
  
  public BoidRenderer(Boid b) {
    this.boid = b;
  }
  
  public abstract void render(PGraphics g, int millis, BeatDetect beat);
}
