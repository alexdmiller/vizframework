package com.rattyduck.viz.graphics;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;

public class Sphere {
  public float radius, x, y, z, threshold;
  public int rangeStart, rangeEnd, rangeNum;
  public int c;
  
  public Sphere(
      float posX,
      float posY,
      float posZ,
      int c,
      float threshold,
      int rangeStart,
      int rangeEnd,
      int rangeNum) {
    this.x = posX;
    this.y = posY;
    this.z = posZ;
    this.c = c;
    this.radius = 0;
    this.threshold = threshold;
    this.rangeStart = rangeStart;
    this.rangeEnd = rangeEnd;
    this.rangeNum = rangeNum;
  }
  
  public void update(BeatDetect beat, AudioSource player) {
    if (player.mix.level() > this.threshold) {
      if (beat.isRange(rangeStart, rangeEnd, rangeNum) && Math.random() > 0.5) {
        radius += (20 + 5 * Math.random()) * player.mix.level();
      }
    }
    radius *= 0.95;
  }
}
