package com.rattyduck.viz.scenes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.rattyduck.viz.Scene;
import com.rattyduck.viz.models.Lattice;
import com.rattyduck.viz.models.Lattice.Node;
import com.rattyduck.viz.models.Sphere;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;
import processing.core.PVector;

public class LatticeScene extends Scene {
  private Lattice lattice;
  private BeatDetect beat;
  
  public LatticeScene(int width, int height, PGraphics g) {
    super(width, height, g, "Lattice");
    
    beat = new BeatDetect();
    beat.setSensitivity(300);
  }
  
  public void start() {
    
    super.start();
    g.camera();
    
    lattice = new Lattice(width, height);
    
    for (int i = 0; i < 200; i++) {
      lattice.createNode(
          (float) Math.random() * width,
          (float) Math.random() * height,
          0, 0);
    }
  }
  
  public void kill() {
    super.kill();
    lattice = null;
  }
  
  public void render(int deltaMillis, AudioSource audio) {
    beat.detect(audio.mix);
    if (beat.isOnset()){
      lattice.signalNodes(audio.mix.level() / 4);
    }
    lattice.update(deltaMillis);
    g.background(0);
    g.noFill();
    
    for (Lattice.EdgeInfo e : lattice.getEdges()) {
      float brightness = e.brightness *
          (lattice.snappingThreshold - e.length()) / lattice.snappingThreshold;      
      if (e.signalCooldown > 0) {
        g.stroke(255, 255, 0, brightness);
        g.strokeWeight(1);
        g.line(e.n1.pos.x, e.n1.pos.y, e.n2.pos.x, e.n2.pos.y);
        g.strokeWeight(5);
        PVector signal = e.getSignalPosition();
        if (signal != null) g.point(signal.x, signal.y);
      } else {
        g.stroke(255, brightness);
        g.strokeWeight(1);
        g.line(e.n1.pos.x, e.n1.pos.y, e.n2.pos.x, e.n2.pos.y);
      }
    }
    
    g.strokeWeight(4);
    for (Lattice.Node n : lattice.getNodes()) {
      g.stroke(255, n.brightness);
      g.point(n.pos.x, n.pos.y);
    }
  }
  
  
  public JPanel getControlPanel() {
    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
    
    JButton signalButton = new JButton("Signal");
    signalButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int i = (int) Math.floor(Math.random() * lattice.getNodes().size());
        Node n = lattice.getNodes().get(i);
        n.signal(true, 0);
      }
    });
    p.add(signalButton);
    
    return p;
  }
}

