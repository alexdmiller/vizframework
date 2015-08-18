package com.rattyduck.viz.renderers;

import com.google.common.collect.EvictingQueue;
import com.rattyduck.viz.models.Boids.Boid;

import processing.core.PGraphics;
import processing.core.PVector;

public class ButterflyBoidRenderer extends BoidRenderer {
  private static int[] availableColors = {0xFFf2c2ff, 0xFFb3fffc, 0xffcfffb8};
  private float theta;
  private float bodySize;
  private float wingSpan;
  private int wingColor;
  
  public ButterflyBoidRenderer(Boid b) {
    super(b);
    theta = (float) (Math.random() * Math.PI * 2);
    bodySize = (float) (Math.random() * 20 + 5);
    wingSpan = (float) (Math.random() * 10 + 1);
    wingColor = availableColors[(int) Math.floor(Math.random() * availableColors.length)];
  }

  @Override
  public void render(PGraphics g, int millis) {
    g.stroke(wingColor);
    g.pushMatrix();
    g.translate(boid.pos.x, boid.pos.y, boid.pos.z);
    g.rotateY((float) Math.atan2(-boid.vel.z, boid.vel.x));
    g.rotateZ((float) Math.asin(boid.vel.y / boid.vel.mag()));
    
    if (boid.vel.y - 2 < 0) {
      theta += (boid.vel.y - 2) / 10;
    }
    
    g.strokeWeight(2);
    g.line(0, 0, 0, bodySize, 0, 0);
    
    g.noStroke();
    g.fill(wingColor);
    
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
