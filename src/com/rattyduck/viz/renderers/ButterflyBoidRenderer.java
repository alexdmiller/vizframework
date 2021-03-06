package com.rattyduck.viz.renderers;

import com.rattyduck.viz.models.Boids.Boid;

import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;

public class ButterflyBoidRenderer extends BoidRenderer {
  private static int[] availableColors = {0xffffe8e8, 0xfffeffe8, 0xffe8ffff, 0xfffae8ff, 0xffffe8ef};
  
  private float theta;
  private float bodySize;
  private float wingSpan;
  private int wingColor;
  
  public ButterflyBoidRenderer(Boid b) {
    super(b);
    theta = (float) (Math.random() * Math.PI * 2);
    bodySize = (float) (Math.random() * 20 + 20);
    wingSpan = (float) (Math.random() * 10 + 20);
    wingColor = availableColors[(int) Math.floor(Math.random() * availableColors.length)];
  }

  @Override
  public void render(PGraphics g, int millis, BeatDetect beat) {    
    g.pushMatrix();
    g.translate(boid.pos.x, boid.pos.y, boid.pos.z);
    g.rotateY((float) Math.atan2(-boid.vel.z, boid.vel.x));
    g.rotateZ((float) Math.asin(boid.vel.y / boid.vel.mag()));
    
    if (boid.vel.y - 2 < 0) {
      theta += (boid.vel.y - 2) / 10;
    }
    
    g.noStroke();
    g.fill(wingColor);
    
    g.beginShape();
    g.vertex(0, 0, 0);
    g.vertex(bodySize / 4, (float) (Math.sin(theta) * 10), wingSpan);
    g.vertex(bodySize, 0, 0);
    g.endShape();
        
    g.beginShape();
    g.vertex(0, 0, 0);
    g.vertex(bodySize / 4, (float) (Math.sin(theta) * 10), -wingSpan);
    g.vertex(bodySize, 0, 0);
    g.endShape();
    
    g.popMatrix();
  }
}
