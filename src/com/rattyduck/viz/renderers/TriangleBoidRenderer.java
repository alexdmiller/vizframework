package com.rattyduck.viz.renderers;

import com.rattyduck.viz.models.Boids.Boid;

import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;

public class TriangleBoidRenderer extends BoidRenderer {
  public TriangleBoidRenderer(Boid b) {
    super(b);
  }

  @Override
  public void render(PGraphics g, int millis, BeatDetect beat) {
    g.fill(50, 100, 255);
    g.strokeWeight(0);
    g.pushMatrix();
    g.translate(boid.pos.x, boid.pos.y);
    g.rotate(boid.vel.heading());
    g.triangle(20, 0, 0, 20, 0, -20);
    g.popMatrix();
  }
}
