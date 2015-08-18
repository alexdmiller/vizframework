package com.rattyduck.viz.renderers;

import com.google.common.collect.EvictingQueue;
import com.rattyduck.viz.models.Boids.Boid;

import processing.core.PGraphics;
import processing.core.PVector;

public class WormBoidRenderer extends BoidRenderer {
  public EvictingQueue<PVector> history;

  public WormBoidRenderer(Boid b, int historySize) {
    super(b);
    history = EvictingQueue.create(historySize);
  }

  @Override
  public void render(PGraphics g, int millis) {
    history.add(boid.pos.get());
    
    PVector last = null;
    g.stroke(255);
    g.strokeWeight(1);
    for (PVector p : history) {
      if (last != null) {
        g.line(last.x, last.y, last.z, p.x, p.y, p.z);
      }
      last = p;
    }
  }
}
