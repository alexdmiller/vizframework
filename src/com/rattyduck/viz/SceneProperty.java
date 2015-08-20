package com.rattyduck.viz;

public class SceneProperty<T> {
  private T value;
  
  public SceneProperty() { }
  
  public SceneProperty(T value) {
    this.value = value;
  }
  
  public T getValue() {
    return value;
  }
  
  public void setValue(T value) {
    this.value = value;
  }
  
  public static class NumericSceneProperty extends SceneProperty<Float> {
    private float min;
    private float max;
    
    public NumericSceneProperty(float value, float min, float max) {
      super(value);
      this.min = min;
      this.max = max;
    }
    
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
