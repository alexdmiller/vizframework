package com.rattyduck.viz.models;

import java.util.List;

import processing.core.PVector;

public class Boid {
  public static final float MAX_SPEED = 4;
  public static final float MAX_STEER_FORCE = 0.1f;
  public static final float NEIGHBORHOOD_RADIUS = 100;
  
  public PVector pos, vel, acc;
  public PVector alignment, cohesion, separation;
  float neighborhoodRadius;
  
  public Boid(PVector pos) {
    this.pos = pos.get();
    vel = new PVector();
    acc = new PVector();
  }
  
  public void update(int millis, List<Boid> boids) {
    vel.add(acc);
    vel.limit(MAX_SPEED);
    pos.add(vel);
    acc.mult(0);
  }
  
  private void flock(List<Boid> boids) {
    
  }
  
  private PVector alignment(List<Boid> boids) {
    return null; 
  }

  private PVector cohesion(List<Boid> boids) {
    return null;
  }

  private PVector separationt(List<Boid> boids) {
    return null;
  }
}
