package com.rattyduck.viz.ui;

import java.lang.reflect.Field;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.rattyduck.viz.ControllableProperty;
import com.rattyduck.viz.Scene;
import com.rattyduck.viz.SceneProperty.NumericSceneProperty;

public class SceneControlPanel extends JPanel {
  public SceneControlPanel(Scene scene) {
    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    JLabel nameLabel = new JLabel(scene.getName());
    add(nameLabel);

    addControlsForObject(scene);
  }
  
  private void addControlsForObject(Object o) {
    for (Field field : o.getClass().getDeclaredFields()) {
      try {
        if (field.getAnnotation(ControllableProperty.class) != null) {
          if (field.getType() == NumericSceneProperty.class) {
            NumericPropertyControl c = new NumericPropertyControl((NumericSceneProperty) field.get(o));
            add(c);         
          } else {
            addControlsForObject(field.get(o));
          }
        }
      } catch (IllegalArgumentException | IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } 
    }
  }
  
}
