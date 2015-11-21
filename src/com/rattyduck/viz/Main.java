package com.rattyduck.viz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.rattyduck.viz.ui.SketchControlPanel;

import processing.core.PApplet; 

public class Main extends JFrame {
  private static final long serialVersionUID = 1L;
  
  private Sketch sketch;
  private SketchControlPanel sketchControl;
  
  public Main() {
    setLayout(new BorderLayout());
    setVisible(true);    
    sketch = new Sketch(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
      }, this);
    
    new PAppletContainer(sketch);
    
    sketchControl = new SketchControlPanel(sketch, this);
    add(sketchControl, BorderLayout.CENTER);
    pack();
  }
  
  class PAppletContainer extends JFrame {    
    private static final long serialVersionUID = 1L;

    public PAppletContainer(PApplet applet) {
      BorderLayout layout = new BorderLayout();
      this.setLayout(layout);
      
      setVisible(true);
      
      Dimension canvasSize = new Dimension(Sketch.WIDTH / 2, Sketch.HEIGHT / 2);
      this.setLocation(0, 0);

      applet.frame = this;
      applet.init();
      
      applet.setPreferredSize(canvasSize);
      applet.setMinimumSize(canvasSize);
      applet.setSize(canvasSize);
      
      add(applet, BorderLayout.CENTER);
      pack();
    }
  }
  
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
          new Main();
        }
    });
  }
}