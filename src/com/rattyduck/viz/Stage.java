package com.rattyduck.viz;

import java.util.ArrayList;
import java.util.List;

import ddf.minim.AudioSource;
import processing.core.PGraphics;

public class Stage {
  private transient Scene currentScene;
  private transient int nextSceneIndex;
  private transient int currentSceneIndex;
  private transient AudioSource audio;
  private transient PGraphics g;
  private transient long lastMillis;
  private transient List<SceneChangeListener> sceneChangeListeners;

  private List<Scene> scenes;  
  
  public Stage() {
    this.scenes = new ArrayList<Scene>();
    this.sceneChangeListeners = new ArrayList<>();
  }
  
  public void init(AudioSource audio, PGraphics g) {
    this.audio = audio;
    this.g = g;
  }
  
  public void addScene(Scene scene) {
    scenes.add(scene);      
  }
  
  public void addSceneChangeListener(SceneChangeListener listener) {
    sceneChangeListeners.add(listener);
  }
  
  public void gotoScene(int index) {
    if (index < 0 || index >= scenes.size()) return;
    if (currentScene != null) {
      currentScene.kill();
    }
    nextSceneIndex = index;
  }
  
  public Scene getCurrentScene() {
    return currentScene;
  }
  
  public int getCurrentSceneIndex() {
    return currentSceneIndex;
  }
  
  public void nextScene() {
    gotoScene(currentSceneIndex + 1);
  }
  
  public void prevScene() {
    gotoScene(currentSceneIndex - 1);
  }
  
  public void restartCurrentScene() {
    gotoScene(currentSceneIndex);
  }
  
  public void update() {
    g.background(0);
    long millis = System.currentTimeMillis();
    long delta = millis - lastMillis;
    if (currentScene == null || currentScene.isFinished()) {
      if (nextSceneIndex == -1) {
        throw new Error("A new scene is not scheduled to run next.");
      }
      currentScene = scenes.get(nextSceneIndex);
      currentSceneIndex = nextSceneIndex;
      nextSceneIndex = -1;
      g.camera();
      currentScene.start();
      
      for (SceneChangeListener l : sceneChangeListeners) {
        l.sceneChanged();
      }
    }
    currentScene.render((int) delta, audio, g);
    lastMillis = millis;
  }
}