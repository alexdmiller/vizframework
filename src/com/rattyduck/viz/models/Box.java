package com.rattyduck.viz.models;

import java.util.ArrayList;
import java.util.List;

import processing.core.PVector;

public class Box {
  private PVector position;
  private float width;
  private float height;
  private float depth;
  
  public Box(PVector pos, float width, float height, float depth) {
    this.position = pos.get();
    this.width = width;
    this.height = height;
    this.depth = depth;
  }
  
  public PVector getPosition() {
    return position.get();
  }
  
  public float getWidth() {
    return width;
  }
  
  public float getHeight() {
    return height;
  }
  
  public float getDepth() {
    return depth;
  }
  
  public List<PVector> getProjectedPoints(PVector p) {
    List<PVector> result = new ArrayList<>();
    result.add(new PVector(p.x, p.y, position.z));
    result.add(new PVector(p.x, p.y, position.z + depth));
    result.add(new PVector(p.x, position.y, p.z));
    result.add(new PVector(p.x, position.y + height, p.z));
    result.add(new PVector(position.x, p.y, p.z));
    result.add(new PVector(position.x + width, p.y, p.z));
    return result;
  }
  
  public PVector getCenter() {
    return new PVector(
        position.x + width / 2,
        position.y + height / 2,
        position.z + depth / 2);
  }
  
  public PVector randomPoint() {
    return new PVector(
        (float) (position.x + Math.random() * width),
        (float) (position.y + Math.random() * height),
        (float) (position.z + Math.random() * depth));
  }
}
