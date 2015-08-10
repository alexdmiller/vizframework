package com.rattyduck.viz.scenes;

import java.util.ArrayList;

import com.rattyduck.viz.Scene;
import com.rattyduck.viz.graphics.Sphere;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;

public class SpinningStarScene extends Scene {
  private ArrayList<Sphere> spheres;
  private float cameraAngle;
  final float SPHERE_SIZE = 200;
  private BeatDetect beat;

  public SpinningStarScene(int width, int height, PGraphics g) {
    super(width, height, g, "Spinning Stars");
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
    
    g.camera(0, 0, 0, 0, 0, 100, 0, -1, 0);
  }
  
  public void kill() {
    super.kill();
    spheres.clear();
  }
  
  void createSpheres(int c, int num, int rangeStart, int rangeEnd, int rangeNum) {
    for (int i = 0; i < num; i++) {
      float radius = (float) (Math.random() * SPHERE_SIZE);
      float angle = (float) (Math.random() * Math.PI * 2);
      float t = (float) (Math.random() * Math.PI * 2);
      float posX = (float) (Math.cos(angle) * Math.sin(t) * radius);
      float posY = (float) (Math.sin(angle) * Math.sin(t) * radius);
      float posZ = (float) (Math.cos(t) * radius);
      float threshold = radius / SPHERE_SIZE;
      
      spheres.add(new Sphere(posX, posY, posZ, c, threshold, rangeStart, rangeEnd, rangeNum));
    }
  }
  
  public void render(int deltaMillis, AudioSource audio) {
    g.camera((float) Math.cos(cameraAngle) * 100, 0, (float) Math.sin(cameraAngle) * 100,
      0, 0, 0,
      0, -1, 0);
    cameraAngle += 0.01;
    g.background(0);
    beat.detect(audio.mix);
    
    for (Sphere s : spheres) {
      s.update(beat, audio);
      g.strokeWeight(s.radius);
      g.stroke(s.c);
      g.point(s.x, s.y, s.z);
    }
  }
}

