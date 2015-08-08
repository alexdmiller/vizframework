package com.rattyduck.viz;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JButton;
import javax.swing.JFrame;

import processing.core.*;

public class Main extends JFrame {
  JButton startButton;
  VizApplet viz;
  PAppletContainer vizContainer;
  
  public Main() {
    viz = new VizApplet();
    vizContainer = new PAppletContainer(viz);
    
    this.setLayout(new BorderLayout());
    this.setLocation(-100, 0);
    
    startButton = new JButton("Start");
    add(startButton, BorderLayout.SOUTH);
    pack();
    setVisible(true);
  }
  
  public class PAppletContainer extends JFrame {
    private PApplet applet;
    
    public PAppletContainer(PApplet applet) {
      this.applet = applet;
      this.setLayout(new BorderLayout());
      
      GraphicsDevice gd =
          GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
      gd.setFullScreenWindow(this);

      applet.frame = this;
      setTitle("test");
      applet.init();
      
      add(applet, BorderLayout.CENTER);
      pack();
      setVisible(true);
    }
  }
  
  public static void main(String[] args) {
    new Main();
  }
}