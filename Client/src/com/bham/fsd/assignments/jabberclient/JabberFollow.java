package com.bham.fsd.assignments.jabberclient;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class JabberFollow {

  private final SimpleStringProperty  userFollow;
  private ImageView addImage;

  public JabberFollow() {
    this.userFollow = new SimpleStringProperty("");
    this.addImage = null;
  }

  public JabberFollow(String userFollow, ImageView addImage) {
    super();
    this.userFollow = new SimpleStringProperty(userFollow);
    this.addImage = addImage;
  }

  public JabberFollow(String userFollow) {
    super();
    this.userFollow = new SimpleStringProperty(userFollow);
  }

  public String getUserFollow() {
    return userFollow.get();
  }

  public ImageView getAddImage() {
    return addImage;
  }

}

