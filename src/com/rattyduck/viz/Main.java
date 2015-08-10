package com.rattyduck.viz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
 
import javax.swing.JButton;
import javax.swing.JFrame;

import processing.core.*; 

public class Main extends JFrame {
  VizApplet viz;
  PAppletContainer vizContainer;
  Checkbox fullScreenCheckbox;
  
  public Main() {
    viz = new VizApplet();
    
    this.setLayout(new BorderLayout());
    
    fullScreenCheckbox = new Checkbox("Fullscreen", false);
    add(fullScreenCheckbox);
    
    JButton startButton = new JButton("Start");
    startButton.addActionListener(new StartAction());
    add(startButton, BorderLayout.SOUTH);

    pack();
    setVisible(true);
  }
  
  class StartAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      vizContainer = new PAppletContainer(viz, fullScreenCheckbox.getState());
    }
  }
  
  public class PAppletContainer extends JFrame {    
    public PAppletContainer(PApplet applet, boolean fullScreen) {
      PAppletContainer self = this;
      
      BorderLayout layout = new BorderLayout();
      this.setLayout(layout);
      
      setVisible(true);
      
      Dimension canvasSize;
      if (fullScreen) {
        GraphicsDevice gd =
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);
        canvasSize = Toolkit.getDefaultToolkit().getScreenSize();
      } else {
        canvasSize = new Dimension(800, 600);
        this.setLocation(100, 100);        
      }
      
      applet.frame = self;
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