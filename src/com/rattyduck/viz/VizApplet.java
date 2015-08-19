package com.rattyduck.viz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
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
    System.out.println(System.getProperty("user.dir"));
    size(this.width, this.height, P3D);
    smooth();
    
    Minim minim = new Minim(this);
    audio = minim.loadFile("dirty ass rap beat.mp3");
    
    File file = new File("stage.json");
    try {
      FileInputStream in = new FileInputStream(file);
      JsonReader reader = new JsonReader(in);
      stage = (Stage) reader.readObject();
      in.close();
    } catch (IOException e) {
      System.out.println(e);
    }

    stage.init(audio, g);
    stage.goToScene(0);
    audio.play();

    setupListener.actionPerformed(null);
  }

  public void draw() {
    stage.update();
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
