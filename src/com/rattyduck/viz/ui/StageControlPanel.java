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
  private SceneControlPanel sceneControl;
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
    if (sceneControl != null) {
      remove(sceneControl);
    }
    sceneControl = new SceneControlPanel(stage.getCurrentScene());
    add(sceneControl, BorderLayout.SOUTH);
    repaint();
    frame.pack();
  }
}
