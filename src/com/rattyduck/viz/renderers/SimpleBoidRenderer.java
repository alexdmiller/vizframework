package com.rattyduck.viz.renderers;

import com.rattyduck.viz.models.Boids.Boid;

import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;

public class SimpleBoidRenderer extends BoidRenderer {
  public SimpleBoidRenderer(Boid b) {
    super(b);
  }

  @Override
  public void render(PGraphics g, int millis, BeatDetect beat) {
    g.stroke(255, 255, 0);
    g.strokeWeight(20);
    g.point(boid.pos.x, boid.pos.y, boid.pos.z);
  }
}
