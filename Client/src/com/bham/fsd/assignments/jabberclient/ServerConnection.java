package com.bham.fsd.assignments.jabberclient;
import com.bham.fsd.assignments.JabberMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection implements Runnable {

    ObjectOutputStream output = null;
    ObjectInputStream input = null;
    Socket serverSocket = null;

    private static final int PORT_NUMBER = 44444;
    public JabberMessage messageOut=null;
    public JabberMessage messageIn=null;

    public ServerConnection() {
        // Open socket to server
        try {
            serverSocket = new Socket("localhost", PORT_NUMBER);
            // Create object streams
            this.output = new ObjectOutputStream(serverSocket.getOutputStream());
            this.input = new ObjectInputStream(serverSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("<< Connection to server "+serverSocket+" established>>");

        // Start thread
        new Thread(this).start();
    }

    @Override
    public void run() {

        try {
            // Wait and read server responses
            while (true) {
                if (this.messageOut!=null&&this.messageOut.getMessage().equals("signout"))
                    break;

                // Read message from server
                messageIn = (JabberMessage) input.readObject();
                printMessage(messageIn);
            }
            // Close streams
            input.close();
        } catch (IOException e) {
            System.out.println("<< Socket is closed on "+Thread.currentThread().getName()+" >>");
//            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void writeToServer(JabberMessage message) {
        // Create message object to be sent to server
        this.messageOut=message;

        System.out.print("Message to the server: ");

        // Send Object
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(this.messageOut.getMessage());

        // Check if connection is meant to be ended
        if (this.messageOut.getMessage().equals("signout")) {
            System.out.println("<< Closing Server Connection. >>");
            try {
                output.close();
                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            System.out.println("<< Connection has been closed. >>");
        }

        // Reset messageOut to null
        this.messageOut=null;

    }

    public static void printMessage(JabberMessage message) {
        System.out.print("-> Server reply message: "+message.getMessage() +"\n");
        System.out.println("-> Server reply data: "+message.getData());
    }

}
