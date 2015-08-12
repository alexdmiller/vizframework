package com.rattyduck.viz.scenes;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.rattyduck.viz.Scene;
import com.rattyduck.viz.models.Sphere;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;

public class HyperSpaceScene extends Scene {
  private ArrayList<Sphere> spheres;
  private float cameraAngle;
  final float SPHERE_SIZE = 200;
  private float rotationSpeed = 0.01f;
  private BeatDetect beat;

  public HyperSpaceScene(int width, int height, PGraphics g) {
    super(width, height, g, "Hyperspace");
    spheres = new ArrayList<Sphere>();
    beat = new BeatDetect();
    beat.detectMode(BeatDetect.FREQ_ENERGY);
    beat.setSensitivity(300);
  }
  
  public void start() {
    super.start();    
    g.camera(0, 0, 0, 0, 0, 100, 0, -1, 0);
  }
  
  public void kill() {
    super.kill();
    spheres.clear();
  }
  
  public void render(int deltaMillis, AudioSource audio) {
    g.camera(0, 0, 0, 0, 0, 100, 0, -1, 0);
    g.background(0);
    beat.detect(audio.mix);
    if (beat.isRange(0, 10, 4)) {
      for (int i = 0; i < audio.mix.level() * 50; i++) {
        Sphere s = new Sphere((float) Math.random() * 400 - 200, (float) Math.random() * 400 - 200, 1000f, 0xffffffff, 0.2f, 0, 20, 2);
        s.vz = -20;
        spheres.add(s);
      }
    }
    
    Iterator<Sphere> i = spheres.iterator();
    while (i.hasNext()) {
      Sphere s = i.next();
      if (s.z == 0) {
        i.remove();
      } else {
        s.update(beat, audio);
        g.strokeWeight(s.radius);
        g.stroke(s.c);
        g.point(s.x, s.y, s.z);
      }
    }
  }
  
  public JPanel getControlPanel() {
    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
    
    JSlider starSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 1);
    starSpeedSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        rotationSpeed = (float) (source.getValue() / 100.0 * (Math.PI / 10));
      }
    });
    p.add(starSpeedSlider);
    
    return p;
  }
}

