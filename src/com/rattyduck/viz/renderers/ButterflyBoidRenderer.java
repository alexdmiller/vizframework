package com.rattyduck.viz.renderers;

import com.google.common.collect.EvictingQueue;
import com.rattyduck.viz.models.Boids.Boid;

import processing.core.PGraphics;
import processing.core.PVector;

public class ButterflyBoidRenderer extends BoidRenderer {
  private float theta;
  private float bodySize;
  private float wingSpan;
  
  public ButterflyBoidRenderer(Boid b) {
    super(b);
    theta = (float) (Math.random() * Math.PI * 2);
    bodySize = (float) (Math.random() * 15 + 1);
    wingSpan = (float) (Math.random() * 10 + 1);
  }

  @Override
  public void render(PGraphics g, int millis) {
    theta += 0.1;

    g.stroke(255);
    g.pushMatrix();
    g.translate(boid.pos.x, boid.pos.y, boid.pos.z);
    g.rotateY((float) Math.atan2(-boid.vel.z, boid.vel.x));
    g.rotateZ((float) Math.asin(boid.vel.y / boid.vel.mag()));
    
    g.strokeWeight(2);
    g.line(0, 0, 0, bodySize, 0, 0);
    
    g.noStroke();
    g.fill(150);
    
    g.beginShape();
    g.vertex(0, 0, 0);
    g.vertex(bodySize / 2, (float) (Math.sin(theta) * 10), wingSpan);
    g.vertex(bodySize, 0, 0);
    g.endShape();
        
    g.beginShape();
    g.vertex(0, 0, 0);
    g.vertex(bodySize / 2, (float) (Math.sin(theta) * 10), -wingSpan);
    g.vertex(bodySize, 0, 0);
    g.endShape();
    
    g.popMatrix();
  }
}
