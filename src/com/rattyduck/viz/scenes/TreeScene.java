package com.rattyduck.viz.scenes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rattyduck.viz.Scene;

import ddf.minim.AudioSource;
import ddf.minim.analysis.BeatDetect;
import processing.core.PGraphics;
import processing.core.PVector;

public class TreeScene extends Scene {
  final int NUM_ATTRACTORS = 5000;
  final float INFLUENCE_RADIUS = 50;
  final float KILL_RADIUS = 5;
  
  float growSpeed = 5;
  float flowerRadius = 1;
  float brightness = 0;
  private BeatDetect beat;

  private Tree tree;
  List<PVector> attractors;
  
  public TreeScene() {    
    attractors = new ArrayList<PVector>();
    tree = new Tree();
    beat = new BeatDetect();
    beat.setSensitivity(300);
  }
  
  public void start() {
    super.start();
    
    for (int i = 0; i < 4000; i++) {
      attractors.add(new PVector(
          (int) (Math.random() * width),
          (int) (Math.random() * height)));
    }
    
    tree.nodes.add(new PVector(width / 2, height / 2));
  }
  
  public void kill() {
    tree.nodes.clear();
  }
  
  @Override
  public void render(int deltaMillis, AudioSource audio, PGraphics g) {
    beat.detect(audio.mix);
    if (beat.isOnset()) {
      flowerRadius += 2;
      brightness += 100;
    }
    flowerRadius *= 0.99;
    brightness *= 0.5;
    
    g.background(0);
    tree.grow(attractors);
    g.strokeWeight(1);
    g.stroke(brightness);
    for (Edge edge : tree.edges) {
      g.line(edge.v1.x, edge.v1.y, edge.v2.x, edge.v2.y);
    }
    
    g.noStroke();
    g.fill(255);
    g.ellipseMode(PGraphics.RADIUS);
//    for (Flower f : tree.flowers) {
//      g.ellipse(f.pos.x, f.pos.y, flowerRadius, flowerRadius);
//    }
  }
  
  class Tree {
    List<Edge> edges;
    List<PVector> nodes;
    List<Flower> flowers;
    
    Tree() {
      edges = new ArrayList<Edge>();
      nodes = new ArrayList<PVector>();
      flowers = new ArrayList<Flower>();
    }
    
    void grow(List<PVector> attractors) {
      Map<PVector, PVector> forces = new HashMap<PVector, PVector>();
      List<PVector> attractorsToRemove = new ArrayList<PVector>();
      
      for (PVector attractor : attractors) {
        
        // Find the closest node to attractor.
        PVector closest = null;
        for (PVector node : nodes) {
          float dist = attractor.dist(node);
          if (dist < INFLUENCE_RADIUS &&
              (closest == null ||
                  dist < attractor.dist(closest))) {
            closest = node;
          }
          
          if (dist < KILL_RADIUS) {
            attractorsToRemove.add(attractor);
          }
        }
        
        // Apply a force to the nearest node.
        if (closest != null) {
          if (!forces.containsKey(closest)) {
            forces.put(closest, new PVector(0, 0));
          }
          PVector diff = new PVector();
          diff.set(attractor);
          diff.sub(closest);
          diff.normalize();
          forces.get(closest).add(diff);
        }
      }
      
      List<PVector> newNodes = new ArrayList<PVector>();
      for (PVector node : nodes) {
        if (forces.containsKey(node)) {
          PVector force = forces.get(node);
          force.normalize();
          force.mult(growSpeed);
          force.add(node);
          
          newNodes.add(force);
          if (Math.random() < 0.1) {
            flowers.add(new Flower(force.x, force.y));
          }
          edges.add(new Edge(node, force));
        }
      }
      
      nodes.addAll(newNodes);
      attractors.removeAll(attractorsToRemove);
    }
    
    void addNode(PVector v, PVector n) {
      nodes.add(n);
      edges.add(new Edge(v, n));
    } 
  }

  class Edge {
    PVector v1, v2;
    
    Edge(PVector v1, PVector v2) {
      this.v1 = v1;
      this.v2 = v2;
    }
  }
  
  class Flower {
    PVector pos;
    float radius;
    
    Flower(float x, float y) {
      this.pos = new PVector(x, y);
      this.radius = 0;
    }
    
    void updateAndDraw(PGraphics g) {
      if (radius < 4) {
        radius += 0.1;
      }
      g.fill(17, 150, 25);
      g.noStroke();
      g.ellipse(this.pos.x, this.pos.y, this.radius, this.radius);
    }
  }
}

