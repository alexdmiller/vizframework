package com.rattyduck.viz.scenes;

import java.util.ArrayList;
import java.util.List;

import com.rattyduck.viz.ControllableProperty;
import com.rattyduck.viz.Scene;
import com.rattyduck.viz.SceneProperty;
import com.rattyduck.viz.SceneProperty.NumericSceneProperty;
import com.rattyduck.viz.models.Boids;
import com.rattyduck.viz.models.Boids.Boid;
import com.rattyduck.viz.models.Box;
import com.rattyduck.viz.renderers.BoidRenderer;
import com.rattyduck.viz.renderers.SimpleBoidRenderer;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;
import processing.core.PVector;

public class FlockingScene extends Scene {
  @ControllableProperty
  public NumericSceneProperty numBoids = SceneProperty.numeric("Boid count", 60, 10, 2000);
  
  @ControllableProperty
  public Boids boids;
  
  private transient List<BoidRenderer> renderers;
  private transient BeatDetect beat;

  public FlockingScene() {
    beat = new BeatDetect();
    beat.setSensitivity(300);
  }
  
  public void start() {
    super.start();
    
    Box bounds = new Box(new PVector(0, 0, -500), width, height, 1000);
    boids = new Boids(bounds);

    renderers = new ArrayList<>();
    
    for (int i = 0; i < numBoids.get(); i++) {
      Boid b = boids.createRandomBoid();
      renderers.add(new SimpleBoidRenderer(b));
    }
  }
  
  public void kill() {
    super.kill();
    boids.clear();
  }
  
  public void render(int deltaMillis, AudioSource audio, PGraphics g) {
    boids.update(deltaMillis);
    beat.detect(audio.mix);
    for (BoidRenderer br : renderers) {
      br.render(g, deltaMillis, beat);
    }
  }
}
