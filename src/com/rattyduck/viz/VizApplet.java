package com.rattyduck.viz;

import processing.core.PApplet;
import ddf.minim.Minim;
import ddf.minim.AudioPlayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.rattyduck.viz.scenes.SetupScene;
import com.rattyduck.viz.scenes.SpinningStarScene;
import com.rattyduck.viz.ui.StageControlPanel;

public class VizApplet extends PApplet implements Controllable, Observer {
  private Stage stage;
  AudioPlayer audio;
  ActionListener setupListener;
  
  public VizApplet(ActionListener setupListener) {
    this.setupListener = setupListener;
  }
  
  public void setup() {
    size(this.width, this.height, P3D);
    
    Minim minim = new Minim(this);
    audio = minim.loadFile("reverie.mp3");
    stage = new Stage(audio, g);
    
    getStage().addScene(new SetupScene(this.width, this.height, g));
    getStage().addScene(new SpinningStarScene(this.width, this.height, g));
    
    getStage().goToScene(0);
    
    setupListener.actionPerformed(null);
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
    p.setLayout(new BorderLayout());
    
    JButton playMusic = new JButton("Play Music");
    playMusic.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        audio.play();
      }
    });
    p.add(playMusic, BorderLayout.NORTH);
    
    StageControlPanel stagePanel = new StageControlPanel(stage);
    p.add(stagePanel, BorderLayout.SOUTH);
    
    return p;
  }

  @Override
  public void update(Observable o, Object arg) {
    
  }
}
