package com.bham.fsd.assignments.jabberclient;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class JabberPost {

  private final SimpleStringProperty  username;
  private final SimpleStringProperty  jab;
  private SimpleIntegerProperty noLikes;
  private ImageView heartImage;
  private int jabid;

  public JabberPost() {
    this.username = new SimpleStringProperty("");
    this.jab = new SimpleStringProperty("");
    this.noLikes = new SimpleIntegerProperty(0);
    this.heartImage = null;
    this.jabid = -1;
  }

  public JabberPost(String username, String jab, int noLikes,
                    ImageView heartImage,int jabid) {
    super();
    this.username = new SimpleStringProperty(username);
    this.jab = new SimpleStringProperty(jab);
    this.noLikes = new SimpleIntegerProperty(noLikes);
    this.heartImage = heartImage;
    this.jabid = jabid;

  }

  public int getJabid() {
    return jabid;
  }

  public String getUsername() {
    return username.get();
  }

  public String getJab() {
    return jab.get();
  }

  public int getNoLikes() {
    return noLikes.get();
  }

  public ImageView getHeartImage() {
    return heartImage;
  }

  public void setHeartImage(ImageView heartImage) {
    this.heartImage = heartImage;
  }

  public void setUsername(String username) {
    this.username.set(username);
  }
  public void setJab(String jab) {
    this.jab.set(jab);
  }

  public void setNoLikes(int noLikes) {
    this.noLikes.set(noLikes);
  }

}

