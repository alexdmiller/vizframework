package com.rattyduck.viz.models;

import java.util.List;

import processing.core.PVector;

public class Boid {
  public static final float MAX_SPEED = 4;
  public static final float MAX_STEER_FORCE = 0.1f;
  public static final float NEIGHBORHOOD_RADIUS = 100;
  
  public static final float SEPARATION_WEIGHT = 1;
  public static final float COHESION_WEIGHT = 1f;
  public static final float ALIGNMENT_WEIGHT = 5;
  
  public PVector pos, vel, acc;
  public PVector alignment, cohesion, separation;
  
  public Boid(PVector pos) {
    this.pos = pos.get();
    vel = new PVector();
    acc = new PVector();
  }
  
  public void update(int millis, List<Boid> boids) {
    acc.add(PVector.mult(alignment(boids), ALIGNMENT_WEIGHT));
    acc.add(PVector.mult(cohesion(boids), COHESION_WEIGHT));
    acc.add(PVector.mult(separation(boids), SEPARATION_WEIGHT));

    vel.add(acc);
    vel.limit(MAX_SPEED);
    pos.add(vel);
    acc.mult(0);
  }
  
  private PVector steer(PVector target, boolean arrival) {
    PVector steer = new PVector();
    
    if (!arrival) {
      steer.set(PVector.sub(target, pos));
      steer.limit(MAX_STEER_FORCE);
    } else {
      PVector targetOffset = PVector.sub(target, pos);
      float distance = targetOffset.mag();
      float rampedSpeed = MAX_SPEED * (distance/100);
      float clippedSpeed = Math.min(rampedSpeed, MAX_SPEED);
      PVector desiredVelocity = PVector.mult(targetOffset,(clippedSpeed/distance));
      steer.set(PVector.sub(desiredVelocity,vel));
    }
    
    return steer;
  }
  
  public PVector avoid(PVector target) {
    PVector steer = new PVector();
    steer.set(PVector.sub(pos, target));
    float d = PVector.dist(pos, target);
    steer.mult(1 / d * d);
    return steer;
  }
  
  public void bound(Box bounds) {
    List<PVector> projectedPoints = bounds.getProjectedPoints(pos);
    for (PVector p : projectedPoints) {
      acc.add(avoid(p));
    }
  }
  
  private PVector alignment(List<Boid> boids) {
    PVector averageVelocity = new PVector();
    int total = 0;
    for (Boid b : boids) {
      float distance = b.pos.dist(this.pos);
      if (distance > 0 && distance < NEIGHBORHOOD_RADIUS) {
        averageVelocity.add(b.vel);
        total++;
      }
    }
    averageVelocity.div(total);
    if (total > 0) {
      averageVelocity.div(total);
    }
    averageVelocity.limit(MAX_STEER_FORCE);
    return averageVelocity;
  }

  private PVector cohesion(List<Boid> boids) {
    PVector averagePosition = new PVector();
    int total = 0;
    for (Boid b : boids) {
      float distance = b.pos.dist(this.pos);
      if (distance > 0 && distance < NEIGHBORHOOD_RADIUS) {
        averagePosition.add(b.pos);
        total++;
      }
    }
    if (total > 0) {
      averagePosition.div(total);
    }
    PVector cohesionForce = PVector.sub(averagePosition, this.pos);
    cohesionForce.normalize();
    return cohesionForce;
  }

  private PVector separation(List<Boid> boids) {
    PVector separationForce = new PVector();
    for (Boid b : boids) {
      float distance = b.pos.dist(this.pos);
      if (distance > 0 && distance < NEIGHBORHOOD_RADIUS) {
        PVector f = PVector.sub(pos, b.pos);
        f.normalize();
        f.div(distance);
        separationForce.add(f);
      }
    }
    return separationForce;
  }
}
