package com.rattyduck.viz;

import java.util.Map;

import ddf.minim.AudioSource;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.HashMap;
import java.util.List;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.Iterator;

class Stage {
  private Scene currentScene;
  private int currentSceneIndex;
  
  private List<Scene> activeScenes;
  private List<Scene> scenes;
  
  private AudioSource audio;
  private PGraphics g;
  private long lastMillis;
  
  Stage(AudioSource audio, PGraphics g) {
    this.scenes = new ArrayList<Scene>();
    this.activeScenes = new ArrayList<Scene>();
    this.audio = audio;
    this.g = g;
  }
  
  public void addScene(Scene scene) {
    scenes.add(scene);      
  }
  
  public void goToScene(int index) {
    if (index < 0 || index >= scenes.size()) return;
    if (currentScene != null) {
      currentScene.kill();
    }
    currentScene = scenes.get(index);
    currentSceneIndex = index;
    activeScenes.add(currentScene);
    currentScene.start();
  }
  
  public void nextScene() {
    goToScene(currentSceneIndex + 1);
  }
  
  public void prevScene() {
    goToScene(currentSceneIndex - 1);
  }
  
  public void update() {
    g.background(0);
    long millis = System.currentTimeMillis();
    long delta = millis - lastMillis;
    Iterator<Scene> i = activeScenes.iterator();
    while(i.hasNext()) {
      Scene s = i.next();
      if (s.isFinished()) {
        i.remove();
      } else {
        s.render((int) delta, audio);
      }
    }
    lastMillis = millis;
  }
}