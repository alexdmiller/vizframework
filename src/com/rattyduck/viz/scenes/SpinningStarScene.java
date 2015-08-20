package com.rattyduck.viz.scenes;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.rattyduck.viz.ControllableProperty;
import com.rattyduck.viz.Scene;
import com.rattyduck.viz.SceneProperty;
import com.rattyduck.viz.SceneProperty.NumericSceneProperty;
import com.rattyduck.viz.models.Sphere;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;

public class SpinningStarScene extends Scene {
  @ControllableProperty
  public NumericSceneProperty sphereSize = SceneProperty.numeric("Sphere size", 200, 20, 1000);
  @ControllableProperty
  public NumericSceneProperty rotationSpeed = SceneProperty.numeric("Rotation speed", 0.1f, -0.2f, 0.2f);
  
  private transient ArrayList<Sphere> spheres;
  private transient float cameraAngle;
  private transient BeatDetect beat;

  public SpinningStarScene(int width, int height) {
    super(width, height);
    spheres = new ArrayList<Sphere>();
    beat = new BeatDetect();
    beat.detectMode(BeatDetect.FREQ_ENERGY);
    beat.setSensitivity(300);
  }
  
  public void start() {
    super.start();
    createSpheres(0xfff8e7f8, 1000, 0, 6, 3);
    createSpheres(0xfffff394, 1000, 7, 20, 5);
    createSpheres(0xffffc994, 1000, 20, 25, 2);
  }
  
  public void kill() {
    super.kill();
    spheres.clear();
  }
  
  private void createSpheres(int c, int num, int rangeStart, int rangeEnd, int rangeNum) {
    for (int i = 0; i < num; i++) {
      float radius = (float) (Math.random() * sphereSize.get());
      float angle = (float) (Math.random() * Math.PI * 2);
      float t = (float) (Math.random() * Math.PI * 2);
      float posX = (float) (Math.cos(angle) * Math.sin(t) * radius);
      float posY = (float) (Math.sin(angle) * Math.sin(t) * radius);
      float posZ = (float) (Math.cos(t) * radius);
      float threshold = radius / sphereSize.get();
      
      spheres.add(new Sphere(posX, posY, posZ, c, threshold, rangeStart, rangeEnd, rangeNum));
    }
  }
  
  public void render(int deltaMillis, AudioSource audio, PGraphics g) {
    g.camera((float) Math.cos(cameraAngle) * 100, 0, (float) Math.sin(cameraAngle) * 100,
      0, 0, 0,
      0, -1, 0);
    cameraAngle += rotationSpeed.get();
    g.background(0);
    beat.detect(audio.mix);
    
    for (Sphere s : spheres) {
      s.update(beat, audio);
      g.strokeWeight(s.radius);
      g.stroke(s.c);
      g.point(s.x, s.y, s.z);
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
        //rotationSpeed = (float) (source.getValue() / 100.0 * (Math.PI / 10));
      }
    });
    p.add(starSpeedSlider);
    
    return p;
  }
}

