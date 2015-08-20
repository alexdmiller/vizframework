package com.rattyduck.viz.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.rattyduck.viz.SceneChangeListener;
import com.rattyduck.viz.Stage;

public class StageControlPanel extends JPanel implements SceneChangeListener {
  private Stage stage;
  private JLabel sceneLabel;
  private JPanel sceneControl;
  private JFrame frame;
  
  public StageControlPanel(Stage stage, JFrame frame) {
    this.stage = stage;
    this.frame = frame;
    
    stage.addSceneChangeListener(this);
    
    setSize(new Dimension(100, 100));
    setLayout(new BorderLayout());

    JButton prev = new JButton("Previous");
    prev.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        stage.prevScene();
      }
    });
    add(prev, BorderLayout.WEST);
    
    sceneLabel = new JLabel();
    sceneLabel.setPreferredSize(new Dimension(200, 15));
    add(sceneLabel, BorderLayout.CENTER);
    
    JButton next = new JButton("Next");
    next.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        stage.nextScene();
      }
    });
    add(next, BorderLayout.EAST);
  }
  
  @Override
  public void sceneChanged() {
    sceneLabel.setText(stage.getCurrentScene().getName());
    if (sceneControl != null) {
      remove(sceneControl);
    }
    sceneControl = stage.getCurrentScene().getControlPanel();
    add(sceneControl, BorderLayout.SOUTH);
    repaint();
    frame.pack();
  }
}
