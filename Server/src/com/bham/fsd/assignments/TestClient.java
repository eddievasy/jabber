// This class has been used for testing purposes as follows:
// It mimics what the GUI does by taking the String input entered
// into the terminal and wrapping it in a JabberMessage object.
// It thereafter sends the request over to the Server.
// This class also prints the message/data received back from the server.

package com.bham.fsd.assignments;

import com.bham.fsd.assignments.JabberMessage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TestClient {
    private static final int PORT_NUMBER = 44444;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            // Open socket to server
            Socket serverSocket = new Socket("localhost", PORT_NUMBER);
            System.out.println("<< Connection to server "+serverSocket+" established>>");

            // Create object streams
            ObjectOutputStream output = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(serverSocket.getInputStream());


            // Message objects
            JabberMessage messageIn;
            JabberMessage messageOut;
            String message;


            // Loop where communication takes place
            while (true) {
                // Create message object to be sent to server
                System.out.print("Message to the server: ");
                message = scanner.nextLine();
                messageOut = new JabberMessage(message);

                // Send Object
                output.writeObject(messageOut);
                output.flush();

                // Check if connection is meant to be ended
                if (message.equals("signout")) {
                    System.out.println("<< Closing Server Connection. >>");
                    serverSocket.close();
                    System.out.println("<< Connection has been closed. >>");
                    break;
                }

                // Read message from server
                messageIn = (JabberMessage) input.readObject();
                printMessage(messageIn);

            }
            // Close streams and scanner
            scanner.close();
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void printMessage(JabberMessage message) {
        System.out.print("-> Server reply message: "+message.getMessage() +"\n");
        System.out.println("-> Server reply data: "+message.getData());
    }
}
