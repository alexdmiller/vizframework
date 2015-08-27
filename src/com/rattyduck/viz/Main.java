package com.rattyduck.viz;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import processing.core.PApplet; 

public class Main extends JFrame {
  private static final long serialVersionUID = 1L;
  
  VizApplet viz;
  PAppletContainer vizContainer;
  Checkbox fullScreenCheckbox;
  JPanel startPanel;
  
  public Main() {
    setLayout(new BorderLayout());
    setVisible(true);
    setPreferredSize(new Dimension(600, 1000));
    
    viz = new VizApplet(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          add(viz.getControlPanel(), BorderLayout.CENTER);
          pack();
        }
      }, this);
  
    vizContainer = new PAppletContainer(viz);
  }
  
  class PAppletContainer extends JFrame {    
    private static final long serialVersionUID = 1L;

    public PAppletContainer(PApplet applet) {
      BorderLayout layout = new BorderLayout();
      this.setLayout(layout);
      
      setVisible(true);
      
      Dimension canvasSize = new Dimension(VizApplet.WIDTH / 2, VizApplet.HEIGHT / 2);
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