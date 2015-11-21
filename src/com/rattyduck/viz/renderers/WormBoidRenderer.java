package com.rattyduck.viz.renderers;

import com.google.common.collect.EvictingQueue;
import com.rattyduck.viz.models.Boids.Boid;

import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;
import processing.core.PVector;

public class WormBoidRenderer extends BoidRenderer {
  public EvictingQueue<PVector> history;
  private float red, green, blue;

  public WormBoidRenderer(Boid b, int historySize, float red, float green, float blue) {
    super(b);
    history = EvictingQueue.create(historySize);
    this.red = red;
    this.blue = blue;
    this.green = green;
  }

  @Override
  public void render(PGraphics g, int millis, BeatDetect beat) {
    history.add(boid.pos.get());
    
    PVector last = null;
    g.stroke(red, green, blue);
    g.strokeWeight(10);
    for (PVector p : history) {
      if (last != null) {
        g.line(last.x, last.y, last.z, p.x, p.y, p.z);
      }
      last = p;
    }
  }
}
