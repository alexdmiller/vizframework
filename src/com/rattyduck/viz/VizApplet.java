package com.rattyduck.viz;

import processing.core.PApplet;
import ddf.minim.Minim;
import ddf.minim.AudioPlayer;
import com.rattyduck.viz.scenes.SetupScene;

public class VizApplet extends PApplet {
  Stage stage;
  AudioPlayer audio;
  
  public void setup() {
    Minim minim = new Minim(this);
    audio = minim.loadFile("/Users/miller/Documents/workspace/Shaprece/bin/data/reverie.mp3");
    stage = new Stage(audio, g);
    
    stage.addScene(new SetupScene(this.width, this.height, g));
    
    stage.goToScene(0);
  }

  public void draw() {
    stage.update();
  }
  
  public void keyPressed() {
    if (key == ' ') {
      audio.play();
      stage.nextScene();
    }
    if (keyCode == LEFT) {
      stage.prevScene();
    }
    if (keyCode == RIGHT) {
      stage.nextScene();
    }
  }
}
