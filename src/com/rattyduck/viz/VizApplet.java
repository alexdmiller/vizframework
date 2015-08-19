package com.rattyduck.viz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.rattyduck.viz.scenes.FlockingScene;
import com.rattyduck.viz.scenes.HyperSpaceScene;
import com.rattyduck.viz.scenes.LatticeScene;
import com.rattyduck.viz.scenes.SetupScene;
import com.rattyduck.viz.scenes.SpinningStarScene;
import com.rattyduck.viz.ui.StageControlPanel;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class VizApplet extends PApplet implements Controllable, Observer {
  private Stage stage;
  private JFrame frame;
  private AudioPlayer audio;
  private ActionListener setupListener;
  
  public VizApplet(ActionListener setupListener, JFrame frame) {
    this.setupListener = setupListener;
    this.frame = frame;
  }
  
  public void setup() {
    size(this.width, this.height, P3D);
    smooth();
    
    Minim minim = new Minim(this);
    audio = minim.loadFile("reverie.mp3");
    stage = new Stage(audio, g);
    
    getStage().addScene(new FlockingScene(this.width, this.height, g));
    getStage().addScene(new LatticeScene(this.width, this.height, g));
    getStage().addScene(new HyperSpaceScene(this.width, this.height, g));
    getStage().addScene(new SpinningStarScene(this.width, this.height, g));
    getStage().addScene(new SetupScene(this.width, this.height, g));
    
    getStage().goToScene(0);
    audio.play();

    setupListener.actionPerformed(null);
  }

  public void draw() {
    getStage().update();
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
    
    StageControlPanel stagePanel = new StageControlPanel(stage, frame);
    p.add(stagePanel, BorderLayout.SOUTH);
    
    return p;
  }

  @Override
  public void update(Observable o, Object arg) {
    
  }
}
