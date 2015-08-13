package com.rattyduck.viz.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import processing.core.PVector;

public class Lattice {
  private List<Node> nodes;
  private Map<Edge, EdgeInfo> edges;
  private int width, height; 
  
  public float fadeSpeed = 10;
  public float minBrightness = 20;
  public float friction = 0.9f;
  private float repelThreshold = 20;
  private float repelForce = 20;
  private float birthThreshold = 40;
  private float snappingThreshold = 60; 
  
  public Lattice(int width, int height) {
    this.width = width;
    this.height = height;
    this.nodes = new ArrayList<>();
    this.edges = new HashMap<>();
  }
  
  public void createNode(float x, float y, float vx, float vy) {
    Node n = new Node(x, y, vx, vy);
    nodes.add(n);
  }
  
  private void createEdge(Node n1, Node n2) {
    EdgeInfo info = new Lattice.EdgeInfo(n1, n2);
    Edge e = new Edge(n1, n2);
    
    n1.connectedEdges.add(info);
    n2.connectedEdges.add(info);
    
    edges.put(e, info);
  }
    
  public Iterable<Node> getNodes() {
    return nodes;
  }
  
  public Iterable<EdgeInfo> getEdges() {
    return edges.values();
  }
  
  public void update(int millis) {
    for (int i = 0; i < nodes.size(); i++) {
      Node n1 = nodes.get(i);
      for (int j = i + 1; j < nodes.size(); j++) {
        Node n2 = nodes.get(j);
        
        float dist = n1.distance(n2);
        
        if (dist < repelThreshold) {
          n1.repel(n2);
        }
        
        if (dist < birthThreshold && !edges.containsKey(new Edge(n1, n2))) {
          createEdge(n1, n2);
        }
      }
      
      n1.update(millis);
    }

    Iterator<Map.Entry<Edge, EdgeInfo>> it = edges.entrySet().iterator();
    while (it.hasNext()) {
      EdgeInfo e = it.next().getValue();
      e.update();
      if (e.length() > snappingThreshold) {
        it.remove();
      }
    }
  }
  
  public void signalNodes(float strength) {
    for (Node n : nodes) {
      if (Math.random() < strength) {
        n.signal();
      }
    }
  }

  public class Node {
    public PVector originalPos;
    public PVector pos;
    public PVector vel;
    public float brightness = minBrightness;
    private List<EdgeInfo> connectedEdges;
    
    public Node(float x, float y, float vx, float vy) {
      this.pos = new PVector(x, y);
      this.originalPos = pos.get();
      this.vel = new PVector(vx, vy);
      this.connectedEdges = new ArrayList<>();
    }
    
    public void update(int millis) {
      this.vel.mult(friction);
      this.pos.add(vel);
      
      if (pos.x < 0) {
        pos.x = 1;
      } else if (pos.x > width) {
        pos.x = width - 1;
      }
      
      if (pos.y < 0) {
        pos.y = 1;
      } else if (pos.y > height) {
        pos.y = height - 1;
      }
      
      if (brightness > 0) brightness -= fadeSpeed;
    }
    
    public void repel(Node other) {
      PVector diff = this.pos.get();
      diff.sub(other.pos);
      float dist = diff.mag();
      float angle = diff.heading();
      
      float force = repelForce / dist;
      PVector forceVector = new PVector(
          (float) Math.cos(angle) * force, (float) Math.sin(angle) * force);
      
      this.vel.add(forceVector);
      other.vel.sub(forceVector);
    }
    
    public void signal() {
      brightness = 255;
      PVector random = PVector.random2D();
      random.mult(20);
      this.vel.add(random);
      
      if (this.brightness > minBrightness) this.brightness -= fadeSpeed;
      for (EdgeInfo e : connectedEdges) {
        e.brightness = 255;
      }
    }
    
    public float distance(Node other) {
      return this.pos.dist(other.pos);
    }
  }
  
  public class Edge {
    public Node n1, n2;

    public Edge(Node n1, Node n2) {
      this.n1 = n1;
      this.n2 = n2;
    }
    
    public int hashCode() {
      int hashFirst = n1.hashCode();
      int hashSecond = n2.hashCode();
      return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }
    
    public boolean equals(Object other) {
      if (other instanceof Edge) {
        Edge otherEdge = (Edge) other;
        return this.n1 == otherEdge.n1 && this.n2 == otherEdge.n2;
      }
      return false;
    }
  }
  
  public class EdgeInfo {
    public Node n1, n2;
    public float brightness = minBrightness;
    
    public EdgeInfo(Node n1, Node n2) {
      this.n1 = n1;
      this.n2 = n2;
    }
    
    public void update() {
      if (this.brightness > minBrightness) this.brightness -= fadeSpeed;
    }

    public float length() {
      return n1.distance(n2);
    }

    public void detatch() {
      n1.connectedEdges.remove(this);
      n2.connectedEdges.remove(this);
    }
  }
}
