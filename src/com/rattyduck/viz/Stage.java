package com.rattyduck.viz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import ddf.minim.AudioSource;
import processing.core.PGraphics;

public class Stage extends Observable {
  private transient Scene currentScene;
  private transient int currentSceneIndex;
  private transient List<Scene> activeScenes;
  private transient AudioSource audio;
  private transient PGraphics g;
  private transient long lastMillis;
  
  private List<Scene> scenes;  
  
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
    
    setChanged();
    notifyObservers();
  }
  
  public Scene getCurrentScene() {
    return currentScene;
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