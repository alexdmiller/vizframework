package com.rattyduck.viz.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rattyduck.viz.models.Boids.Boid;

import processing.core.PVector;

public class Boids implements Iterable<Boid> {
  public float maxSpeed = 6;
  public float maxSteerForce = 0.1f;
  public float neighborhoodRadius = 50;
  
  public float separationWeight = 10;
  public float cohesionWeight = 1;
  public float alignmentWeight = 3;
  public float avoidWeight = 20;
  
  private List<Boid> boids;
  private Box bounds;
  
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
  
  public void create(float x, float y, float z) {
    boids.add(new Boid(new PVector(x, y, z)));
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
      acc.add(PVector.mult(alignment(), alignmentWeight));
      acc.add(PVector.mult(cohesion(), cohesionWeight));
      acc.add(PVector.mult(separation(), separationWeight));
      vel.add(acc);
      vel.limit(maxSpeed);
      pos.add(vel);
      acc.mult(0);
    }
    
    public PVector avoid(PVector target) {
      PVector steer = new PVector();
      steer.set(PVector.sub(pos, target));
      float d = PVector.dist(pos, target);
      steer.mult(1 / (d * d));
      return steer;
    }
    
    public void bound(Box bounds) {
      List<PVector> projectedPoints = bounds.getProjectedPoints(pos);
      for (PVector p : projectedPoints) {
        acc.add(PVector.mult(avoid(p), avoidWeight));
      }
    }
    
    private PVector alignment() {
      PVector averageVelocity = new PVector();
      int total = 0;
      for (Boid b : boids) {
        float distance = b.pos.dist(this.pos);
        if (distance > 0 && distance < neighborhoodRadius) {
          averageVelocity.add(b.vel);
          total++;
        }
      }
      if (total > 0) {
        averageVelocity.div(total);
      }
      averageVelocity.limit(maxSteerForce);
      return averageVelocity;
    }
  
    private PVector cohesion() {
      PVector averagePosition = new PVector();
      int total = 0;
      for (Boid b : boids) {
        float distance = b.pos.dist(this.pos);
        if (distance > 0 && distance < neighborhoodRadius) {
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
      cohesionForce.limit(maxSteerForce);
      return cohesionForce;
    }
  
    private PVector separation() {
      PVector separationForce = new PVector();
      for (Boid b : boids) {
        float distance = b.pos.dist(this.pos);
        if (distance > 0 && distance < neighborhoodRadius) {
          PVector f = PVector.sub(pos, b.pos);
          f.normalize();
          f.div(distance);
          separationForce.add(f);
        }
      }
      separationForce.limit(maxSteerForce);
      return separationForce;
    }
  }
}
