package com.bham.fsd.assignments.jabberclient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.bham.fsd.assignments.JabberMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UserController implements Initializable, Runnable {

    public ServerConnection connection = Controller.connection;
    public boolean listener = false;
    public boolean whoToFollowListener = false;
    public boolean signedOut = false;
    public boolean refreshSelected = false;


    //******************* TIMELINE **************************//
    //define table properties
    @FXML
    private TableView<JabberPost> jabTable;
    @FXML
    private TableColumn<JabberPost, String> username;
    @FXML
    private TableColumn<JabberPost, String> jab;
    @FXML
    private TableColumn<JabberPost, Integer> noLikes;
    @FXML
    private TableColumn<JabberPost, ImageView> heartImage;

    @FXML
    private TextField writeJab;
    @FXML
    private Button postBtn;
    @FXML
    private Button refreshButton;

    //create a JabberPost class to represent each user on timeline

    //define data in the observable list
    public ObservableList<JabberPost> timeline = FXCollections.observableArrayList();
    //Don't forget to add heart image at end


    //******************* TIMELINE **************************//


    //******************* WHO TO FOLLOW **************************//
    //define table properties
    @FXML
    private TableView<JabberFollow> followTable;
    @FXML
    private TableColumn<JabberFollow, String> userFollow;
    @FXML
    private TableColumn<JabberFollow, ImageView> addImage;

    //create a JabberFollow class to represent each non-followed user

    //define data in the observable list
    public ObservableList<JabberFollow> whoToFollow = FXCollections.observableArrayList();
    //Don't forget to add image at end

    //******************* WHO TO FOLLOW **************************//

    @FXML
    private Label userlbl; //Indicates that the user has logged in

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createTimeline();
        whoToFollow();

        new Thread(this).start();

    }

    public void getUser(String user) {
        userlbl.setText(user);
    }

    public ArrayList<ArrayList<String>> getTimelineData() //Sample timeline data
    {
        // Send request for timeline to server
        connection.writeToServer(new JabberMessage("timeline"));

        // Allow some time for server to reply
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<ArrayList<String>> response = connection.messageIn.getData();

        //Timeline
//		ArrayList<String> ted = new ArrayList<String>();
//		ted.add("ted");
//		ted.add("excellent");
//		ted.add("56");
//		ted.add("3");
//
//		ArrayList<String> bill = new ArrayList<String>();
//		bill.add("bill");
//		bill.add("dude");
//		bill.add("58");
//		bill.add("2");
//
//		response.add(ted);
//		response.add(bill);

        return response;
    }

    public void createTimeline() {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {

                ArrayList<ArrayList<String>> timelineData = getTimelineData();
                ArrayList<JabberPost> newPosts = new ArrayList<JabberPost>();


                for (ArrayList row : timelineData) //Adds data onto timeline
                {

                    InputStream heartStream = null;
                    try {
                        heartStream = new FileInputStream("heart.png");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Image heartDisplay = new Image(heartStream);
                    //Creating the image view
                    ImageView heartView = new ImageView();

                    //Setting image to the image view
                    heartView.setImage(heartDisplay);
                    heartView.setFitWidth(28);
                    heartView.setFitHeight(26);

                    newPosts.add(new JabberPost(row.get(0).toString(), row.get(1).toString()
                            , Integer.parseInt(row.get(3).toString()), heartView,
                            Integer.parseInt(row.get(2).toString())));

                }
                try {
                    timeline = FXCollections.observableArrayList(newPosts);
                } catch (Exception e) {
                    System.out.println("weird error");
                }
                //associate data with columns
                //the username, Jab, NoLikes, heartImage are from the JabberPost class
                // fields
                username.setCellValueFactory(new PropertyValueFactory<JabberPost, String>("username"));
                jab.setCellValueFactory(new PropertyValueFactory<JabberPost, String>("jab"));
                noLikes.setCellValueFactory(new PropertyValueFactory<JabberPost, Integer>("noLikes"));
                heartImage.setCellValueFactory(new PropertyValueFactory<JabberPost, ImageView>("heartImage"));

                //add data to the table
                jabTable.setItems(timeline);
                jabTable.setEditable(true);

            }
        });

        if (listener == false) {
            this.createTimelineListener();
            listener = true;
        }

//        ArrayList<ArrayList<String>> timelineData = getTimelineData();
//        ArrayList<JabberPost> newPosts = new ArrayList<JabberPost>();
//
//
//        System.out.println("Up to here");
//        for (ArrayList row : timelineData) //Adds data onto timeline
//        {
//
//            InputStream heartStream = null;
//            try {
//                heartStream = new FileInputStream("heart.png");
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            Image heartDisplay = new Image(heartStream);
//            //Creating the image view
//            ImageView heartView = new ImageView();
//
//            //Setting image to the image view
//            heartView.setImage(heartDisplay);
//            heartView.setFitWidth(28);
//            heartView.setFitHeight(26);
//
//            newPosts.add(new JabberPost(row.get(0).toString(), row.get(1).toString()
//                    , Integer.parseInt(row.get(3).toString()), heartView,
//                    Integer.parseInt(row.get(2).toString())));
//
//        }
//        try {
//            timeline = FXCollections.observableArrayList(newPosts);
//        } catch (Exception e)
//        {
//            System.out.println("weird error");
//        }
//        //associate data with columns
//        //the username, Jab, NoLikes, heartImage are from the JabberPost class
//        // fields
//        username.setCellValueFactory(new PropertyValueFactory<JabberPost, String>("username"));
//        jab.setCellValueFactory(new PropertyValueFactory<JabberPost, String>("jab"));
//        noLikes.setCellValueFactory(new PropertyValueFactory<JabberPost, Integer>("noLikes"));
//        heartImage.setCellValueFactory(new PropertyValueFactory<JabberPost, ImageView>("heartImage"));
//
//        //add data to the table
//        jabTable.setItems(timeline);
//        jabTable.setEditable(true);
//
//        if (listener == false) {
//            this.createTimelineListener();
//            listener = true;
//        }


    }

    public void createTimelineListener() {

        jabTable.getSelectionModel().setCellSelectionEnabled(true);

        final ObservableList<TablePosition> selectedCells = jabTable.getSelectionModel().getSelectedCells();


        selectedCells.addListener(new ListChangeListener<TablePosition>() {

            @Override
            public void onChanged(ListChangeListener.Change change) {
                for (TablePosition pos : selectedCells) {
//                    System.out.println("Cell selected in row " + pos.getRow() + " and column " + pos.getTableColumn().getText());

                    if (pos.getTableColumn().getText().equals("<3")) {
//                        System.out.println("Sending jabid " + timeline.get(pos.getRow()).getJabid());

                        // Send like request to server
                        connection.writeToServer(new JabberMessage("like " + timeline.get(pos.getRow()).getJabid()));
                        refreshSelected = true;
                        createTimeline();
                    }

                }
            }

        });

//        if(refreshSelected == true)
//        {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            refreshSelected = false;
//            this.createTimeline();
//        }

    }

    public void postButton(ActionEvent event) {
//		JabberPost post = new JabberPost();
//		post.setUsername((userlbl.getText().toString()));
//		post.setJab(writeJab.getText());
//		post.setNoLikes(0);
//		//Don't forget to add heart image after
//
//		jabTable.getItems().add(post);
//		writeJab.clear();

        // Send request to post to the server
        this.connection.writeToServer(new JabberMessage("post " + writeJab.getText()));
        // Refresh timeline
        this.createTimeline();
    }

    public void clickRefresh(ActionEvent event) {
        this.createTimeline();
        this.whoToFollow();
    }

    public ArrayList<ArrayList<String>> getWhoToFollowData() //Sample WhoToFollowdata
    {
        this.connection.writeToServer(new JabberMessage("users"));

        // Allow some time for server to reply
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<ArrayList<String>> toReturn = this.connection.messageIn.getData();

        return toReturn;
    }

    public void whoToFollow() {


        Platform.runLater(new Runnable() {
                              @Override
                              public void run() {

                                  ArrayList<ArrayList<String>> whoToFollowData = getWhoToFollowData();

                                  ArrayList<JabberFollow> newFollows = new ArrayList<JabberFollow>();

                                  for (int i = 0; i < whoToFollowData.size(); i++) //Adds data onto timeline
                                  {
                                      InputStream greenStream = null;
                                      try {
                                          greenStream = new FileInputStream("green.png");
                                      } catch (FileNotFoundException e) {
                                          e.printStackTrace();
                                      }
                                      Image greenDisplay = new Image(greenStream);
                                      //Creating the image view
                                      ImageView greenView = new ImageView();
                                      //Setting image to the image view
                                      greenView.setImage(greenDisplay);
                                      greenView.setFitWidth(28);
                                      greenView.setFitHeight(26);

                                      newFollows.add(new JabberFollow(whoToFollowData.get(i).get(0), greenView));

                                  }

                                  whoToFollow = FXCollections.observableArrayList(newFollows);

                                  //associate data with columns
                                  //the username,addImage are from the JabberFollow class fields
                                  userFollow.setCellValueFactory(new PropertyValueFactory<JabberFollow, String>("userFollow"));
                                  addImage.setCellValueFactory(new PropertyValueFactory<JabberFollow, ImageView>("addImage"));

                                  followTable.getSelectionModel().setCellSelectionEnabled(true);
                                  //add data to the table
                                  followTable.setItems(whoToFollow);
                                  followTable.setEditable(true);

                                  // Make sure that only one listener is created for the 'Who to follow...' table
                                  if (whoToFollowListener == false) {
                                      final ObservableList<TablePosition> selectedCells = followTable.getSelectionModel().getSelectedCells();
                                      selectedCells.addListener(new ListChangeListener<TablePosition>() {
                                          @Override
                                          public void onChanged(Change change) {
                                              for (TablePosition pos : selectedCells) {
//                                                  System.out.println("Cell selected in row " + pos.getRow() + " and column " + pos.getTableColumn().getText());

                                                  if (pos.getTableColumn().getText().equals("Follow")) {
//                                                      System.out.println("follow " + whoToFollow.get(pos.getRow()).getUserFollow());

                                                      // Send request to server
                                                      connection.writeToServer(new JabberMessage("follow " + whoToFollow.get(pos.getRow()).getUserFollow()));

                                                      // Re-generate 'Who to follow table'
                                                      whoToFollow();

                                                      // Re-generate 'Timeline' table
                                                      createTimeline();
                                                  }
                                              }
                                          }
                                      });

                                      // Point that the listener has already been created
                                      whoToFollowListener = true;
                                  }


                              }
                          }
        );


//        this.connection.writeToServer(new JabberMessage("users"));
//
//        // Allow some time for server to reply
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ArrayList<ArrayList<String>> whoToFollowData = this.connection.messageIn.getData();
//
//
//        ArrayList<JabberFollow> newFollows = new ArrayList<JabberFollow>();
//
//        for (int i = 0; i < whoToFollowData.size(); i++) //Adds data onto timeline
//        {
//            InputStream greenStream = null;
//            try {
//                greenStream = new FileInputStream("green.png");
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            Image greenDisplay = new Image(greenStream);
//            //Creating the image view
//            ImageView greenView = new ImageView();
//            //Setting image to the image view
//            greenView.setImage(greenDisplay);
//            greenView.setFitWidth(28);
//            greenView.setFitHeight(26);
//
//            newFollows.add(new JabberFollow(whoToFollowData.get(i).get(0), greenView));
//
//        }
//
//        whoToFollow = FXCollections.observableArrayList(newFollows);
//
//        //associate data with columns
//        //the username,addImage are from the JabberFollow class fields
//        userFollow.setCellValueFactory(new PropertyValueFactory<JabberFollow, String>("userFollow"));
//        addImage.setCellValueFactory(new PropertyValueFactory<JabberFollow, ImageView>("addImage"));
//
//        followTable.getSelectionModel().setCellSelectionEnabled(true);
//        //add data to the table
//        followTable.setItems(whoToFollow);
//        followTable.setEditable(true);
//
//        // Make sure that only one listener is created for the 'Who to follow...' table
//        if (whoToFollowListener==false) {
//            final ObservableList<TablePosition> selectedCells = followTable.getSelectionModel().getSelectedCells();
//            selectedCells.addListener(new ListChangeListener<TablePosition>() {
//                @Override
//                public void onChanged(Change change) {
//                    for (TablePosition pos : selectedCells) {
//                        System.out.println("Cell selected in row " + pos.getRow() + " and column " + pos.getTableColumn().getText());
//
//                        if (pos.getTableColumn().getText().equals("Follow")) {
//                            System.out.println("follow " + whoToFollow.get(pos.getRow()).getUserFollow());
//
//                            // Send request to server
//                            connection.writeToServer(new JabberMessage("follow " + whoToFollow.get(pos.getRow()).getUserFollow()));
//
//                            // Re-generate 'Who to follow table'
//                            whoToFollow();
//
//                            // Re-generate 'Timeline' table
//                            createTimeline();
//                        }
//                    }
//                }
//            });
//
//            // Point that the listener has already been created
//            whoToFollowListener=true;
//        }

    }

    public void signOut(ActionEvent event) {

        connection.writeToServer(new JabberMessage("signout"));

        this.signedOut=true;

        ((Node) event.getSource()).getScene().getWindow().hide();

    }

    @Override
    public void run() {

        while (this.signedOut==false) {
            this.createTimeline();
            this.whoToFollow();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}