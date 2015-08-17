package com.rattyduck.viz.scenes;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.rattyduck.viz.Scene;
import com.rattyduck.viz.models.Boids;
import com.rattyduck.viz.models.Box;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;
import processing.core.PVector;

public class FlockingScene extends Scene {
  private Boids boids;
  private BeatDetect beat;
  
  public FlockingScene(int width, int height, PGraphics g) {
    super(width, height, g, "Bugs");
    
    Box bounds = new Box(new PVector(0, 0, -500), width, height, 1000);
    boids = new Boids(bounds);
    
    beat = new BeatDetect();
    beat.setSensitivity(300);
  }
  
  public void start() {
    super.start();
    g.camera();
    
    for (int i = 0; i < 1000; i++) {
      Box bounds = boids.getBounds();
      boids.create(
          (float) (bounds.getPosition().x + Math.random() * bounds.getWidth()),
          (float) (bounds.getPosition().y + Math.random() * bounds.getHeight()),
          (float) (bounds.getPosition().z + Math.random() * bounds.getDepth()));
    }
  }
  
  public void kill() {
    super.kill();
  }
  
  public void render(int deltaMillis, AudioSource audio) {
    boids.update(deltaMillis);

    g.strokeWeight(3);
    g.stroke(255);
    for (Boids.Boid b : boids) {
      g.stroke(255);
      
      PVector direction = b.vel.get();
      direction.normalize();
      direction.mult(10);
      
      g.line(
          b.pos.x,
          b.pos.y,
          b.pos.z,
          b.pos.x + direction.x,
          b.pos.y + direction.y,
          b.pos.z + direction.z);
    }
    
    g.stroke(255);
    g.noFill();
    g.pushMatrix();
    
    Box bounds = boids.getBounds();
    g.translate(
        bounds.getPosition().x + bounds.getWidth() / 2,
        bounds.getPosition().y + bounds.getHeight() / 2,
        bounds.getPosition().z + bounds.getDepth() / 2);
    g.box(bounds.getWidth(), bounds.getHeight(), bounds.getDepth());
    g.popMatrix();
  }
  
  public JPanel getControlPanel() {
    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
    
    JSlider alignmentSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 1);
    alignmentSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        boids.alignmentWeight = (float) source.getValue();
      }
    });
    p.add(alignmentSlider);
    
    JSlider cohesionSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 1);
    cohesionSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        boids.cohesionWeight = (float) source.getValue();
      }
    });
    p.add(cohesionSlider);
    
    JSlider separationSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 1);
    separationSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        boids.separationWeight = (float) source.getValue();
      }
    });
    p.add(separationSlider);
    
    JSlider neighborhoodSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 1);
    separationSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        boids.neighborhoodRadius = (float) source.getValue();
      }
    });
    p.add(neighborhoodSlider);
    
    return p;
  }
}
