package com.rattyduck.viz;

import processing.core.PApplet;
import ddf.minim.Minim;
import ddf.minim.AudioPlayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.rattyduck.viz.scenes.SetupScene;
import com.rattyduck.viz.scenes.SpinningStarScene;

public class VizApplet extends PApplet implements Controllable {
  private Stage stage;
  AudioPlayer audio;
  
  public void setup() {
    size(this.width, this.height, P3D);
    
    Minim minim = new Minim(this);
    audio = minim.loadFile("reverie.mp3");
    stage = new Stage(audio, g);
    
    getStage().addScene(new SetupScene(this.width, this.height, g));
    getStage().addScene(new SpinningStarScene(this.width, this.height, g));
    
    getStage().goToScene(0);
  }

  public void draw() {
    getStage().update();
  }
  
  public void keyPressed() {
    if (key == ' ') {
      audio.play();
      getStage().nextScene();
    }
    if (keyCode == LEFT) {
      getStage().prevScene();
    }
    if (keyCode == RIGHT) {
      getStage().nextScene();
    }
  }

  public Stage getStage() {
    return stage;
  }
  
  @Override
  public JPanel getControlPanel() {
    JPanel p = new JPanel();
    p.setSize(new Dimension(100, 100));
    p.setLayout(new BorderLayout());
    
    JButton playMusic = new JButton("Play Music");
    playMusic.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        audio.play();
      }
    });
    p.add(playMusic, BorderLayout.NORTH);
    
    JButton prev = new JButton("Previous");
    prev.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        getStage().prevScene();
      }
    });
    p.add(prev, BorderLayout.WEST);
    
    JButton next = new JButton("Next");
    next.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        getStage().nextScene();
      }
    });
    p.add(next, BorderLayout.EAST);
    
    return p;
  }
}
