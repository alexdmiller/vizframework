package com.rattyduck.viz.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.rattyduck.viz.Sketch;

public class SketchControlPanel extends JPanel {
  public SketchControlPanel(Sketch sketch, JFrame frame) {
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    
    JButton saveStage = new JButton("Save");
    saveStage.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sketch.saveStage();
      }
    });
    add(saveStage);
    
    StageControlPanel stagePanel = new StageControlPanel(sketch.getStage(), frame);
    add(stagePanel);
  }
}
