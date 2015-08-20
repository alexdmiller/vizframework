package com.rattyduck.viz.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rattyduck.viz.ControllableProperty;
import com.rattyduck.viz.SceneProperty;
import com.rattyduck.viz.SceneProperty.NumericSceneProperty;
import com.rattyduck.viz.models.Boids.Boid;

import processing.core.PVector;

public class Boids implements Iterable<Boid> {
  @ControllableProperty
  public NumericSceneProperty maxSpeed = SceneProperty.numeric("Max speed", 4, 0, 10);

  @ControllableProperty
  public NumericSceneProperty maxSteerForce = SceneProperty.numeric("Max steer force", 0.1f, 0f, 1f);
  
  @ControllableProperty
  public NumericSceneProperty neighborhoodRadius = SceneProperty.numeric("Neighborhood radius", 100, 0, 1000);
  
  @ControllableProperty
  public NumericSceneProperty separationWeight = SceneProperty.numeric("Separation", 1, 0, 20);
  
  @ControllableProperty
  public NumericSceneProperty cohesionWeight = SceneProperty.numeric("Cohesion", 1, 0, 20);

  @ControllableProperty
  public NumericSceneProperty alignmentWeight = SceneProperty.numeric("Alignment", 1, 0, 20);
  
  @ControllableProperty
  public NumericSceneProperty avoidWeight = SceneProperty.numeric("Wall avoidance", 10, 0, 20);
  
  @ControllableProperty
  public Box bounds;
  
  private transient List<Boid> boids;

  public Boids(Box bounds) {
    this.boids = new ArrayList<>();
    this.bounds = bounds;
  }
  
  public void update(int millis) {
    for (Boid b : boids) {
      b.bound(bounds);
      b.update(millis);
    }
  }
  
  @Override
  public Iterator<Boid> iterator() {
    return boids.iterator();
  }
  
  public Box getBounds() {
    return bounds;
  }
  
  public class Boid {    
    public PVector pos, vel, acc;
    public PVector alignment, cohesion, separation;
    
    public Boid(PVector pos) {
      this.pos = pos.get();
      vel = new PVector();
      acc = new PVector();
    }
    
    public void update(int millis) {
      acc.add(PVector.mult(alignment(), alignmentWeight.get()));
      acc.add(PVector.mult(cohesion(), cohesionWeight.get()));
      acc.add(PVector.mult(separation(), separationWeight.get()));
      vel.add(acc);
      vel.limit(maxSpeed.get());
      pos.add(vel);
      acc.mult(0);
    }
    
    public PVector avoid(PVector target, boolean weighted) {
      PVector steer = new PVector();
      steer.set(PVector.sub(pos, target));
      steer.normalize();
      if (weighted) {
        float d = PVector.dist(pos, target);
        steer.mult(1 / d);        
      }
      return steer;
    }
    
    public void bound(Box bounds) {
      List<PVector> projectedPoints = bounds.getProjectedPoints(pos);
      for (PVector p : projectedPoints) {
        acc.add(PVector.mult(avoid(p, true), avoidWeight.get()));
      }
    }
    
    private PVector alignment() {
      PVector averageVelocity = new PVector();
      int total = 0;
      for (Boid b : boids) {
        float distance = b.pos.dist(this.pos);
        if (distance > 0 && distance < neighborhoodRadius.get()) {
          averageVelocity.add(b.vel);
          total++;
        }
      }
      if (total > 0) {
        averageVelocity.div(total);
      }
      averageVelocity.limit(maxSteerForce.get());
      return averageVelocity;
    }
  
    private PVector cohesion() {
      PVector averagePosition = new PVector();
      int total = 0;
      for (Boid b : boids) {
        float distance = b.pos.dist(this.pos);
        if (distance > 0 && distance < neighborhoodRadius.get()) {
          averagePosition.add(b.pos);
          total++;
        }
      }
      if (total == 0) {
        return new PVector();
      }
      averagePosition.div(total);
      PVector cohesionForce = PVector.sub(averagePosition, this.pos);
      cohesionForce.normalize();
      cohesionForce.limit(maxSteerForce.get());
      return cohesionForce;
    }
  
    private PVector separation() {
      PVector separationForce = new PVector();
      for (Boid b : boids) {
        float distance = b.pos.dist(this.pos);
        if (distance > 0 && distance < neighborhoodRadius.get()) {
          PVector f = PVector.sub(pos, b.pos);
          f.normalize();
          f.div(distance);
          separationForce.add(f);
        }
      }
      separationForce.limit(maxSteerForce.get());
      return separationForce;
    }
  }

  public Boid createRandomBoid() {
    Boid b = new Boid(bounds.randomPoint());
    boids.add(b);
    return b;
  }

  public void clear() {
    boids.clear();
  }
}
