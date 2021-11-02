package com.bham.fsd.assignments.jabberclient;

import com.bham.fsd.assignments.JabberMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class RegistrationController implements Initializable {

  ServerConnection connection = Controller.connection;

  @FXML
  private Label userlbl;

//  public Model registrationModel = new Model();

  @FXML
  private Label     isConnected;
  @FXML
  private TextField txtUsername;
  @FXML
  private TextField txtPassword;

  @FXML
  private TextField txtPassword2;


  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  public void getUser(String user) {
    userlbl.setText(user);
  }

  public void registration(ActionEvent event) throws Exception{
    boolean isValid = false;

    // Send request to server
    JabberMessage toServer = new JabberMessage("register "+txtUsername.getText());
    connection.writeToServer(toServer);

    // Put thread to sleep so server has time to reply
    Thread.sleep(100);

    if (connection.messageIn.getMessage().equals("signedin")) {
      isValid=true;
    }

    try {
      // This where we shall create to the class that hooks up to the server
      if (isValid) {
//
        // Show timeline
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage  = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Pane root   = loader.load(getClass().getResource("User.fxml").openStream());
        UserController uc = (UserController)loader.getController();
        uc.getUser(txtUsername.getText());
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        // Show 'successful'
        this.successful();

      }

    }

    catch(Exception ee) {
      ee.printStackTrace();

    }

  }

  public void successful() throws IOException {

    Stage stage  = new Stage();
    FXMLLoader loader = new FXMLLoader();
    Pane root   = loader.load(getClass().getResource("RegistrationSuccessful.fxml").openStream());
    Scene scene = new Scene(root);
    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    stage.setScene(scene);
    stage.show();
  }

}