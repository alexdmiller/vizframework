package com.rattyduck.viz.renderers;

import com.rattyduck.viz.models.Boids.Boid;

import processing.core.PGraphics;

public class SimpleBoidRenderer extends BoidRenderer {
  public SimpleBoidRenderer(Boid b) {
    super(b);
  }

  @Override
  public void render(PGraphics g, int millis) {
    g.stroke(255);
    g.strokeWeight(4);
    g.point(boid.pos.x, boid.pos.y, boid.pos.z);
  }
}
