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
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import processing.core.*; 

public class Main extends JFrame {
  private static final long serialVersionUID = 1L;
  
  VizApplet viz;
  PAppletContainer vizContainer;
  Checkbox fullScreenCheckbox;
  JPanel startPanel;
  
  public Main() {
    this.setLayout(new BorderLayout());
    
    startPanel = new JPanel();
    startPanel.setLayout(new BorderLayout());
    
    fullScreenCheckbox = new Checkbox("Fullscreen", false);
    startPanel.add(fullScreenCheckbox);
    
    JButton startButton = new JButton("Start");
    startButton.addActionListener(new StartAction());
    startPanel.add(startButton, BorderLayout.SOUTH);

    add(startPanel);
    
    pack();
    setVisible(true);
  }
  
  class StartAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      viz = new VizApplet();
      vizContainer = new PAppletContainer(viz, fullScreenCheckbox.getState());
      
      remove(startPanel);
      
      add(viz.getControlPanel(), BorderLayout.CENTER);
      pack();
    }
  }
  
  class PAppletContainer extends JFrame {    
    private static final long serialVersionUID = 1L;

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