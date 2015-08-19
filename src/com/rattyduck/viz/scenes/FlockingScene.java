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
import com.rattyduck.viz.models.Boids.Boid;
import com.rattyduck.viz.models.Box;
import com.rattyduck.viz.renderers.BoidRenderer;
import com.rattyduck.viz.renderers.ButterflyBoidRenderer;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;
import processing.core.PVector;

public class FlockingScene extends Scene {
  private int numBoids = 600;
  
  private transient Boids boids;
  private transient List<BoidRenderer> renderers;
  private transient BeatDetect beat;
  
  public FlockingScene(int width, int height, PGraphics g) {
    super(width, height, g, "Bugs");
    beat = new BeatDetect();
    beat.setSensitivity(300);
  }
  
  public void start() {
    super.start();
    g.camera();

    Box bounds = new Box(new PVector(0, 0, -500), width, height, 1000);
    boids = new Boids(bounds);
    renderers = new ArrayList<>();
    
    for (int i = 0; i < numBoids; i++) {
      Boid b = boids.createRandomBoid();
      renderers.add(new ButterflyBoidRenderer(b));
    }
  }
  
  public void kill() {
    super.kill();
    boids = null;
  }
  
  public void render(int deltaMillis, AudioSource audio) {
    boids.update(deltaMillis);
    beat.detect(audio.mix);
    for (BoidRenderer br : renderers) {
      br.render(g, deltaMillis);
    }
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
