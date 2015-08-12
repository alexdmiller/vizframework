package com.rattyduck.viz.scenes;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.rattyduck.viz.Scene;
import com.rattyduck.viz.models.Lattice;
import com.rattyduck.viz.models.Sphere;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;

public class LatticeScene extends Scene {
  private Lattice lattice;
  private BeatDetect beat;
  
  public LatticeScene(int width, int height, PGraphics g) {
    super(width, height, g, "Lattice");
    
    beat = new BeatDetect();
    beat.detectMode(BeatDetect.FREQ_ENERGY);
    beat.setSensitivity(300);
    
    lattice = new Lattice();
  }
  
  public void start() {
    super.start();
    g.camera();
    
    for (int i = 0; i < 500; i++) {
      lattice.createNode(
          (float) Math.random() * width,
          (float) Math.random() * height,
          0, 0);
    }
  }
  
  public void kill() {
    super.kill();
  }
  
  public void render(int deltaMillis, AudioSource audio) {
    lattice.update(deltaMillis);
    
    g.background(0);
    g.noFill();
    g.stroke(255);
    g.strokeWeight(5);
    
    for (Lattice.Node n : lattice.getNodes()) {
      g.point(n.pos.x, n.pos.y);
    }
    
    g.strokeWeight(1);
    for (Lattice.EdgeInfo e : lattice.getEdges()) {
      g.line(e.n1.pos.x, e.n1.pos.y, e.n2.pos.x, e.n2.pos.y);
    }
  }  
}

