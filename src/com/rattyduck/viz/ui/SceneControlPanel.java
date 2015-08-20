package com.rattyduck.viz.ui;

import java.lang.reflect.Field;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.rattyduck.viz.Scene;
import com.rattyduck.viz.SceneProperty.NumericSceneProperty;

public class SceneControlPanel extends JPanel {
  public SceneControlPanel(Scene scene) {
    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    JLabel nameLabel = new JLabel(scene.getName());
    add(nameLabel);
    
    for (Field field : scene.getClass().getDeclaredFields()) {
      if (field.getType() == NumericSceneProperty.class) {
        try {
          NumericPropertyControl c = new NumericPropertyControl((NumericSceneProperty) field.get(scene));
          add(c);
        } catch (IllegalArgumentException | IllegalAccessException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }
}
