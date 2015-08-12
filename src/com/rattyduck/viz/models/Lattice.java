package com.rattyduck.viz.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import processing.core.PVector;

public class Lattice {
  private List<Node> nodes;
  private Map<Edge, EdgeInfo> edges;
  
  public float repelThreshold = 10;
  public float birthThreshold = 100;
  public float breakThreshold = 100;
  
  public Lattice() {
    this.nodes = new ArrayList<>();
    this.edges = new HashMap<>();
  }
  
  public void createNode(float x, float y, float vx, float vy) {
    Node n = new Node(x, y, vx, vy);
    nodes.add(n);
  }
  
  private void createEdge(Node n1, Node n2) {
    EdgeInfo info = new Lattice.EdgeInfo(n1, n2);
    Edge e = info.getEdge();
    
    n1.edges.add(e);
    n2.edges.add(e);
    
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
        
        if (dist <= repelThreshold) {
          n1.repel(n2);
        }
        
        if (dist <= birthThreshold && !edges.containsKey(new Edge(n1, n2))) {
          createEdge(n1, n2);
        }
      }
      n1.update(millis);
    }
    
    Iterator<Map.Entry<Edge, EdgeInfo>> it = edges.entrySet().iterator();
    while (it.hasNext()) {
      Edge e = it.next().getKey();
      if (e.length() > breakThreshold) {
        e.detatch();
        it.remove();
      }
    }  
  }

  public static class Node {
    public PVector pos;
    public PVector vel;
    public float signalCookdown;
    private List<Edge> edges;
    
    public Node(float x, float y, float vx, float vy) {
      this.pos = new PVector(x, y);
      this.vel = new PVector(vx, vy);
      this.edges = new ArrayList<>();
    }
    
    public void update(int millis) {
      this.pos.add(vel);
      System.out.println(vel);  
    }
    
    public void repel(Node other) {
      PVector diff = this.pos.get();
      diff.sub(other.pos);
      float dist = diff.mag();
      float angle = diff.heading();
      
      float force = 20 / dist;
      PVector forceVector = new PVector(
          (float) Math.cos(angle) * force, (float) Math.sin(angle) * force);
      
      this.vel.add(forceVector);
      other.vel.sub(forceVector);
    }
    
    public void signal() {
      
    }
    
    public float distance(Node other) {
      return this.pos.dist(other.pos);
    }
  }
  
  public static class Edge {
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
    
    public float length() {
      return n1.distance(n2);
    }

    public void detatch() {
      n1.edges.remove(this);
      n2.edges.remove(this);
    }
    
    public boolean equals(Object other) {
      if (other instanceof Edge) {
        Edge otherEdge = (Edge) other;
        return this.n1 == otherEdge.n1 && this.n2 == otherEdge.n2;
      }
      return false;
    }
  }
  
  public static class EdgeInfo {
    public Node n1, n2;
    public float edgeCooldown;
    
    public EdgeInfo(Node n1, Node n2) {
      this.n1 = n1;
      this.n2 = n2;
    }
       
    public Edge getEdge() {
      return new Edge(n1, n2);
    }
  }
}
