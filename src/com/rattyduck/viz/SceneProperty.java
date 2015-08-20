package com.rattyduck.viz;

import java.util.ArrayList;
import java.util.List;

public class SceneProperty<T> {
  private transient List<ScenePropertyChangeListener> listeners;
  
  public static NumericSceneProperty numeric(
      String name, float defaultValue, float min, float max) {
    NumericSceneProperty p = new NumericSceneProperty();
    p.name = name;
    p.value = defaultValue;
    p.max = max;
    p.min = min;
    return p;
  }
  
  public static NumericSceneProperty numeric(
      String name, int defaultValue, int min, int max) {
    return numeric(name, (float) defaultValue, (float) min, (float) max);
  }

  public static StringSceneProperty string(String name, String defaultValue) {
    StringSceneProperty p = new StringSceneProperty();
    p.name = name;
    p.value = defaultValue;
    return p;
  }
  

  protected String name;
  protected T value;

  public SceneProperty() {
    listeners = new ArrayList<>();
  }
    
  public T get() {
    return value;
  }
  
  public void set(T value) {
    this.value = value;
    for (ScenePropertyChangeListener l : listeners) {
      l.scenePropertyChanged(this);
    }
  }
  
  public String getName() {
    return name;
  }
  
  public void addChangeListener(ScenePropertyChangeListener l) {
    listeners.add(l);
  }
  
  public static class NumericSceneProperty extends SceneProperty<Float> {
    private float min;
    private float max;
    
    public float getMin() {
      return min;
    }
    
    public float getMax() {
      return max;
    }
  }
  
  public static class StringSceneProperty extends SceneProperty<String> {
  }
}
