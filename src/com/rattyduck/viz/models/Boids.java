package com.rattyduck.viz.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rattyduck.viz.models.Boids.Boid;

import processing.core.PVector;

public class Boids implements Iterable<Boid> {
  public float maxSpeed = 5;
  public float maxSteerForce = 0.1f;
  public float neighborhoodRadius = 100;
  public float separationWeight = 10;
  public float cohesionWeight = 1;
  public float alignmentWeight = 3;
  public float avoidWeight = 20;

  private Box bounds;
  
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
      acc.add(PVector.mult(alignment(), alignmentWeight));
      acc.add(PVector.mult(cohesion(), cohesionWeight));
      acc.add(PVector.mult(separation(), separationWeight));
      vel.add(acc);
      vel.limit(maxSpeed);
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
        acc.add(PVector.mult(avoid(p, true), avoidWeight));
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

  public Boid createRandomBoid() {
    Boid b = new Boid(bounds.randomPoint());
    boids.add(b);
    return b;
  }
}
