package com.rattyduck.viz.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.rattyduck.viz.SceneProperty;
import com.rattyduck.viz.SceneProperty.NumericSceneProperty;
import com.rattyduck.viz.ScenePropertyChangeListener;

public class NumericPropertyControl extends JPanel implements ScenePropertyChangeListener, ChangeListener {
  private static int MAX_SLIDER = 1000;
  private static DecimalFormat format = new DecimalFormat("#.##");
  
  private JLabel valueLabel;
  private JSlider slider;
  private NumericSceneProperty property;
  
  public NumericPropertyControl(NumericSceneProperty property) {
    this.property = property;
    
    property.addChangeListener(this);
    
    setLayout(new BorderLayout());
    
    JLabel nameLabel = new JLabel(property.getName());
    nameLabel.setPreferredSize(new Dimension(150, 20));
    nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    this.add(nameLabel, BorderLayout.WEST);

    valueLabel = new JLabel(Float.toString(property.get()));
    valueLabel.setPreferredSize(new Dimension(50, 20));
    this.add(valueLabel, BorderLayout.EAST);
        
    slider = new JSlider(JSlider.HORIZONTAL, 0, MAX_SLIDER, 6);
    slider.setPreferredSize(new Dimension(200, 20));
    slider.addChangeListener(this);
    this.add(slider, BorderLayout.CENTER);
    
    scenePropertyChanged(property);
  }
  
  @Override
  public void scenePropertyChanged(SceneProperty<?> property) {
    NumericSceneProperty p = (NumericSceneProperty) property;
    valueLabel.setText(format.format(p.get()));
    slider.setValue(propertyToSlider(p));
  }
  
  @Override
  public void stateChanged(ChangeEvent e) {
    float newValue = sliderValueToProperty(slider.getValue(), property);
    property.set(newValue);
  }
  
  private float sliderValueToProperty(int sliderValue, NumericSceneProperty property) {
    return (float) sliderValue / MAX_SLIDER * (property.getMax() - property.getMin()) + property.getMin();
  }
  
  private int propertyToSlider(NumericSceneProperty property) {
    return (int) Math.round((property.get() - property.getMin()) / (property.getMax() - property.getMin()) * MAX_SLIDER);
  }
}
