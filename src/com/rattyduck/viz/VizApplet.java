package com.rattyduck.viz;

import processing.core.PApplet;
import ddf.minim.Minim;
import ddf.minim.AudioPlayer;
import com.rattyduck.viz.scenes.SetupScene;
import com.rattyduck.viz.scenes.SpinningStarScene;

public class VizApplet extends PApplet {
  Stage stage;
  AudioPlayer audio;
  
  public void setup() {
    size(200, 200, P3D);
    
    Minim minim = new Minim(this);
    audio = minim.loadFile("reverie.mp3");
    stage = new Stage(audio, g);
    
    stage.addScene(new SetupScene(this.width, this.height, g));
    stage.addScene(new SpinningStarScene(this.width, this.height, g));
    
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
