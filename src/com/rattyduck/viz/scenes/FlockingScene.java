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
import com.rattyduck.viz.renderers.ButterflyBoidRenderer;
import com.rattyduck.viz.renderers.SimpleBoidRenderer;
import com.rattyduck.viz.renderers.TriangleBoidRenderer;
import com.rattyduck.viz.renderers.WormBoidRenderer;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;
import processing.core.PVector;

public class FlockingScene extends Scene {
  @ControllableProperty
  public NumericSceneProperty numBoids = SceneProperty.numeric("Boid count", 600, 10, 2000);
  
  @ControllableProperty
  public Boids boids;
  
  private transient List<BoidRenderer> renderers;
  private transient BeatDetect beat;
  
  public FlockingScene(int width, int height) {
    super(width, height);
    beat = new BeatDetect();
    beat.setSensitivity(300);
    
    Box bounds = new Box(new PVector(0, 0, -500), width, height, 1000);
    boids = new Boids(bounds);
  }
  
  public void start() {
    super.start();

    renderers = new ArrayList<>();
    
    for (int i = 0; i < numBoids.get(); i++) {
      Boid b = boids.createRandomBoid();
      
      double r = Math.random(); 
      if (r < 0.33) {
        renderers.add(new WormBoidRenderer(b, 50, 255, 50, 50));
      } else if (r < 0.66) {
        renderers.add(new WormBoidRenderer(b, 50, 255, 255, 50));
      } else {
        renderers.add(new WormBoidRenderer(b, 50, 50, 100, 255));
      }
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
