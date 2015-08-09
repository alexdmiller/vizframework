package com.rattyduck.viz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
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
    this.setLayout(new BorderLayout());
    this.setLocation(-100, 0);
    
    startButton = new JButton("Start");
    add(startButton, BorderLayout.SOUTH);
    pack();
    setVisible(true);
    
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        viz = new VizApplet();
        vizContainer = new PAppletContainer(viz);
      }
  });
  }
  
  public class PAppletContainer extends JFrame {    
    public PAppletContainer(PApplet applet) {
      BorderLayout layout = new BorderLayout();
      this.setLayout(layout);
      
      GraphicsDevice gd =
          GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
      gd.setFullScreenWindow(this);
      setExtendedState(JFrame.MAXIMIZED_BOTH);
      
      applet.frame = this;
      applet.init();
      applet.setPreferredSize(new Dimension(getWidth(), getHeight()));
      applet.setMinimumSize(new Dimension(getWidth(), getHeight()));
      applet.setSize(new Dimension(getWidth(), getHeight()));
      setTitle("test");
      
      add(applet, BorderLayout.CENTER);
      pack();
      setVisible(true);
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