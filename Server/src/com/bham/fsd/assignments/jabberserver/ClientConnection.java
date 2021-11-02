package com.bham.fsd.assignments.jabberserver;

import com.bham.fsd.assignments.JabberMessage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientConnection implements Runnable {

    private Socket clientSocket = null;
    private JabberDatabase database = null;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;

    // These will change once a user has logged in
    private boolean signedIn = false;
    private String connectionUsername = null;
    private int connectionUserID = -1;


    public ClientConnection(Socket clientSocket, JabberDatabase database) throws IOException {
        // Initialize Socket and JabberDatabase attributes
        this.clientSocket = clientSocket;
        this.database = database;


        // Initialize object streams
        this.output = new ObjectOutputStream(clientSocket.getOutputStream());
        this.input = new ObjectInputStream(clientSocket.getInputStream());

//        // Start thread from constructor
//        new Thread(this).start();
    }

    @Override
    public void run() {

        System.out.println("<< The connection thread <" + Thread.currentThread().getName() + "> has started. >>");
        JabberMessage messageOut = null;
        JabberMessage messageIn = null;

        while (true) {

            try {
                messageIn = (JabberMessage) input.readObject();

                // Check if connection should be exited
                if (messageIn.getMessage().equals("signout")) {
                    System.out.println("<< Client has requested that connection " + clientSocket + " be closed. >>");
                    this.clientSocket.close();
                    System.out.println("<< Connection has been closed. >>");
                    break;
                }

                // Print client message
                System.out.println("-> Client has requested: " + messageIn.getMessage());

                // Send response to client
                // Build messageOut by parsing messageIn
                messageOut = parse(messageIn);
                output.writeObject(messageOut);
                output.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("<< Class Not Found Exception. >>");
                e.printStackTrace();
            }

        }

        // Close object streams
        try {
            this.input.close();
            this.output.close();
        } catch (IOException e) {
            System.out.println("<< Exception thrown whilst trying to close object streams >>");
            e.printStackTrace();
        }
    }

    public JabberMessage parse(JabberMessage in) {
        // Generic response to all requests BUT the valid ones
        JabberMessage out = new JabberMessage("Invalid request.");
        // Client only sends message (no data)
        String messageIn = in.getMessage();

        // Split message request by whitespaces
        // Save result into an ArrayList
        String[] splitMessageArray = messageIn.split(" ");
        ArrayList<String> splitMessage = new ArrayList<String>(Arrays.asList(splitMessageArray));

        // Identify request type
        String requestType = splitMessage.get(0);

        switch (requestType) {
            case ("signin"):
                // For when user forgets to add the username
                if (splitMessage.size() == 1) {
                    return out;
                } else
                    out = processSignIn(splitMessage);
                break;
            case ("register"):
                // For when user forgets to add the username
                if (splitMessage.size() == 1) {
                    return out;
                } else
                    out = processRegister(splitMessage);
                break;
            case("timeline"):
                // Process request only if client is signed in
                if (this.signedIn==true) {
                    out = processTimeline();
                }
                break;
            case("users"):
                // Process request only if client is signed in
                if (this.signedIn==true) {
                    out = processUsersNotFollowed();
                }
                break;
            case("post"):
                // Process request only if client is signed AND the jab is not empty
                if (this.signedIn==true && splitMessage.size()>1) {
                    out = processPostJab(splitMessage);
                }
                break;
            case("like"):
                // Process request only if client is signed in AND the jab to like is mentioned
                if (this.signedIn==true && splitMessage.size()>1) {
                    out = processLike(splitMessage);
                }
                break;
            case("follow"):
                // Process request only if client is signed in AND the user to follow is mentioned
                if (this.signedIn==true && splitMessage.size()>1) {
                    out = processFollow(splitMessage);
                }

        }


        return out;
    }

    private JabberMessage processFollow(ArrayList<String> splitMessage) {
        String userToFollow = this.rebuildStringWithoutFirstWord(splitMessage);

        // Follow user only if they exist
        if (this.database.getUserID(userToFollow)!=-1) {
        this.database.addFollower(this.connectionUserID,userToFollow);
        return new JabberMessage("posted"); }
        else {
            return new JabberMessage("Invalid request.");
        }
    }

    private JabberMessage processLike(ArrayList<String> splitMessage) {
        String stringID = splitMessage.get(1);

        // We want to catch an error such as "like wow" -- "wow" cannot be converted to int
        try {
            // Like the jab
            int jabID = Integer.valueOf(stringID);
            this.database.addLike(this.connectionUserID, jabID);
        } catch (Exception e) {
            return new JabberMessage("Invalid request.");
        }
        return new JabberMessage("posted");
    }

    private JabberMessage processPostJab(ArrayList<String> splitMessage) {
        String jab = this.rebuildStringWithoutFirstWord(splitMessage);
        this.database.addJab(this.connectionUsername, jab);

        return new JabberMessage("posted");
    }

    private JabberMessage processUsersNotFollowed() {
        ArrayList<ArrayList<String>> usersNotFollowed = this.database.getUsersNotFollowed(this.connectionUserID);
        return new JabberMessage("users", usersNotFollowed);
    }

    private JabberMessage processTimeline() {
        ArrayList<ArrayList<String>> timeline = this.database.getTimelineOfUserEx(this.connectionUserID);
        return new JabberMessage("timeline",timeline);
    }


    public JabberMessage processSignIn(ArrayList<String> splitMessage) {
        // Get username
        String userName = splitMessage.get(1);
        // See if user exists in the database
        int userID = database.getUserID(userName);
        if (userID != -1) {
            // Update relevant attributes
            this.signedIn = true;
            this.connectionUsername = userName;
            this.connectionUserID = userID;
            // Print to server terminal (for testing purposes)
            this.printUserDetails();
            return new JabberMessage("signedin");
        }
        // If user doesn't exist in database
        return new JabberMessage("unknown-user");
    }

    public JabberMessage processRegister(ArrayList<String> splitMessage) {
        // Get username & fabricate email
        String userName = this.rebuildStringWithoutFirstWord(splitMessage);
        String userEmail = userName + "@gmail.com";

        // See if user already exists in the database, and if so, just sign them in
        int userID = database.getUserID(userName);
        if (userID != -1) {
            // Update relevant attributes
            this.signedIn = true;
            this.connectionUsername = userName;
            this.connectionUserID = userID;
            // Print to server terminal (for testing purposes)
            this.printUserDetails();
        } else {
        // Add user to database & sign them in
        database.addUser(userName,userEmail);
        this.signedIn = true;
        this.connectionUsername = userName;
        // Get user id
        userID = database.getUserID(userName);
        this.connectionUserID = userID;

        // Print to server terminal (for testing purposes)
        this.printUserDetails(); }

        return new JabberMessage("signedin");
    }

    // Tested -- it works
    public String rebuildStringWithoutFirstWord(ArrayList<String> splitMessage) {
        String toReturn = "";
        // from i=1 because position 0 contains the request type
        for (int i = 1; i < splitMessage.size(); i++) {
            if (i == splitMessage.size() - 1)
                toReturn += splitMessage.get(i);
            else
                toReturn += splitMessage.get(i) + " ";
        }
        return toReturn;
    }

    public void printUserDetails () {
        System.out.println("<< User [ Name: " + this.connectionUsername + ", ID: " + this.connectionUserID
                + " ] is signed in on "
                + Thread.currentThread().getName() + " >>");
    }
}
