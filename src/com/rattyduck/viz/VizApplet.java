package com.rattyduck.viz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
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

import codeanticode.syphon.SyphonServer;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;
import processing.core.PGraphics;

public class VizApplet extends PApplet implements Controllable {
  private static final String STAGE_FILENAME = "stage.json";

  private Stage stage;
  private JFrame frame;
  private AudioPlayer audio;
  private ActionListener setupListener;
  PGraphics canvas;
  SyphonServer server;
  
  public VizApplet(ActionListener setupListener, JFrame frame) {
    this.setupListener = setupListener;
    this.frame = frame;
  }
  
  public void setup() {
    size(this.width, this.height, P3D);
    smooth();
    
    canvas = createGraphics(this.width, this.height, P3D);

    // Create syhpon server to send frames out.
    server = new SyphonServer(this, "Processing Syphon");
    
    Minim minim = new Minim(this);
    audio = minim.loadFile("reverie.mp3");
    
    File file = new File(STAGE_FILENAME);
    try {
      FileInputStream in = new FileInputStream(file);
      JsonReader reader = new JsonReader(in);
      stage = (Stage) reader.readObject();
      reader.close();
    } catch (IOException e) {
      stage = new Stage();
      stage.addScene(new SetupScene(width, height));
    }

    stage.init(audio, g);
    stage.gotoScene(0);
    audio.play();

    setupListener.actionPerformed(null);
  }

  public void draw() {
    canvas.beginDraw();
    canvas.background(100);
    canvas.stroke(255);
    canvas.line(50, 50, 100, 100);
    canvas.endDraw();
    server.sendImage(canvas);
      
    stage.update();
  }

  public Stage getStage() {
    return stage;
  }
  
  public void saveStage() {
    File file = new File(STAGE_FILENAME);
    try {
      FileOutputStream out = new FileOutputStream(file);
      
      Map<String, Object> m = new HashMap<>();
      m.put(JsonWriter.PRETTY_PRINT, true);
      
      JsonWriter writer = new JsonWriter(out, m);
      writer.write(stage);
      writer.close();
    } catch (IOException e) {
      System.out.println(e);
    }
  }
  
  @Override
  public JPanel getControlPanel() {
    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
    
    JButton playMusic = new JButton("Play Music");
    playMusic.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        audio.play();
      }
    });
    p.add(playMusic);
    
    JButton saveStage = new JButton("Save");
    saveStage.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveStage();
      }
    });
    p.add(saveStage);
    
    StageControlPanel stagePanel = new StageControlPanel(stage, frame);
    p.add(stagePanel);
    
    return p;
  }
}
