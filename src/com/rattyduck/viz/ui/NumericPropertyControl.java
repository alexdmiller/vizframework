package com.rattyduck.viz.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.rattyduck.viz.SceneProperty;
import com.rattyduck.viz.SceneProperty.NumericSceneProperty;
import com.rattyduck.viz.ScenePropertyChangeListener;

public class NumericPropertyControl extends JPanel implements ScenePropertyChangeListener {
  private static int MAX_SLIDER = 100;
  
  private JLabel valueLabel;
  private JSlider slider;
  
  public NumericPropertyControl(NumericSceneProperty property) {
    property.addChangeListener(this);
    
    setLayout(new BorderLayout());
    
    JLabel nameLabel = new JLabel(property.getName());
    this.add(nameLabel, BorderLayout.WEST);

    valueLabel = new JLabel(Float.toString(property.get()));
    this.add(valueLabel, BorderLayout.EAST);
    
    slider = new JSlider(JSlider.HORIZONTAL, 0, MAX_SLIDER, propertyToSlider(property));
    slider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        float newValue = sliderValueToProperty(source.getValue(), property);
        property.set(newValue);
      }
    });
    this.add(slider, BorderLayout.CENTER);
  }
  
  private float sliderValueToProperty(int sliderValue, NumericSceneProperty property) {
    return (property.getMax() - property.getMin()) / MAX_SLIDER *
        sliderValue + property.getMin();
  }
  
  private int propertyToSlider(NumericSceneProperty property) {
    return (int) (Math.round(property.getMax() - property.getMin()) / MAX_SLIDER *
        (property.get() - property.getMin()));
  }

  @Override
  public void scenePropertyChanged(SceneProperty<?> property) {
    NumericSceneProperty p = (NumericSceneProperty) property;
    valueLabel.setText(Float.toString(p.get()));
    slider.setValue(propertyToSlider(p));
  }
}
