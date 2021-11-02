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

import java.net.URL;
import java.util.ResourceBundle;



public class Controller implements Initializable {

//  public Model loginModel = new Model();
  //This is where the client will go
  public static ServerConnection connection = null;

  @FXML
  private Label     isConnected;
  @FXML
  private TextField txtUsername;
  @FXML
  private TextField txtPassword;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
      // Initialise connection to server
      connection = new ServerConnection();
  }

  public void login(ActionEvent event) throws Exception{
    boolean isValid = false;

    // Send request to server
    JabberMessage toServer = new JabberMessage("signin "+txtUsername.getText());
    connection.writeToServer(toServer);

    // Put thread to sleep so server has time to reply
    Thread.sleep(100);

    if (connection.messageIn.getMessage().equals("signedin")) {
        isValid=true;
    }

    try {
      //if (loginModel.isLogin(txtUsername.getText(),txtPassword.getText()))
      // { This where we shall create to the class that hooks up to the server
        if (isValid)
        {

//        isConnected.setText("Username/Password Correct");
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage          stage  = new Stage();
        FXMLLoader     loader = new FXMLLoader();
        Pane           root   = loader.load(getClass().getResource("User.fxml").openStream());
        UserController uc     = (UserController)loader.getController();
        uc.getUser(txtUsername.getText());
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
      }
      else
        {
            Stage          stage  = new Stage();
            FXMLLoader     loader = new FXMLLoader();
            Pane           root   = loader.load(getClass().getResource("Error.fxml").openStream());
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        }

    }
//    catch(SQLException se) {

//    }
    catch(Exception ee) {

    }

  }

  public void registration(ActionEvent event) throws Exception{
    try
    {
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage stage  = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("Register.fxml").openStream());
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    catch(Exception ee)
    {

    }

  }

}