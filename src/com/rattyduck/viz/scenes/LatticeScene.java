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
    beat.setSensitivity(300);
    
    lattice = new Lattice(width, height);
  }
  
  public void start() {
    super.start();
    g.camera();
    
    for (int i = 0; i < 1000; i++) {
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
    beat.detect(audio.mix);
    if (beat.isOnset()){
      lattice.signalNodes(audio.mix.level() / 4);
    }
    lattice.update(deltaMillis);
    
    g.background(0);

    g.noFill();
    
    g.strokeWeight(1);
    for (Lattice.EdgeInfo e : lattice.getEdges()) {
      float brightness = e.brightness *
          (lattice.snappingThreshold - e.length()) / lattice.snappingThreshold;
      g.stroke(255, brightness);
      g.line(e.n1.pos.x, e.n1.pos.y, e.n2.pos.x, e.n2.pos.y);
    }
    
    g.strokeWeight(3);
    for (Lattice.Node n : lattice.getNodes()) {
      g.stroke(255, n.brightness);
      g.point(n.pos.x, n.pos.y);
    }

  }  
}

