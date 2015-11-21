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

public class Sketch extends PApplet {
  public static final int WIDTH = 1920;
  public static final int HEIGHT = 1080;
  
  private static final String STAGE_FILENAME = "stage.json";

  private Stage stage;
  private JFrame frame;
  private AudioPlayer audio;
  private ActionListener setupListener;
  private boolean recording;
  
  PGraphics canvas;
  SyphonServer server;
  
  public Sketch(ActionListener setupListener, JFrame frame) {
    this.setupListener = setupListener;
    this.frame = frame;
    
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
  }
  
  public void setup() {
    size(this.width, this.height, P3D);
    smooth();
    frameRate(30);
    
    canvas = createGraphics(WIDTH, HEIGHT, P3D);

    // Create syhpon server to send frames out.
    server = new SyphonServer(this, "Processing Syphon");
    
    Minim minim = new Minim(this);
    audio = minim.loadFile("reverie.mp3");

    stage.init(audio, canvas);
    stage.gotoScene(0);
    audio.play();

    setupListener.actionPerformed(null);
  }

  public void draw() {
    canvas.beginDraw();  
    stage.update();
    canvas.endDraw();
    server.sendImage(canvas);

    g.image(canvas, 0, 0, this.width, this.height);
    
  }

  public void playAudio() {
    audio.play();
  }
  
  public void pauseAudio() {
    audio.pause();
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

  public void startRecord() {
    recording = true;
  }

  public void stopRecord() {
    recording = false;
  }
}