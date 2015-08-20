package com.rattyduck.viz.ui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.rattyduck.viz.Scene;

public class SceneControlPanel extends JPanel {
  public SceneControlPanel(Scene scene) {
    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    JLabel nameLabel = new JLabel(scene.getName());
    add(nameLabel);
  }
}
