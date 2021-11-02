// This is the Server class which should be run
// so that the server starts listening for new clients.


package com.bham.fsd.assignments.jabberserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class JabberServer implements Runnable {

    private static final int PORT_NUMBER = 44444;
    private ServerSocket serverSocket;

    public JabberServer() {
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            serverSocket.setReuseAddress(true);
            serverSocket.setSoTimeout(300);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // start the Server thread form the constructor
//        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println("<< Server thread has started and it is waiting for connections... >>");

        while (true) {

            Socket clientSocket = null;

            try {
//                System.out.println("<< Server is waiting for connection... >>");
                // Blocking method -- Server listens for a connection
                clientSocket = serverSocket.accept();

                if (clientSocket.isConnected()) {

                    System.out.println("-> New Client Connected: " + clientSocket);

                    // Assign new thread for this connection
                    // Pass new database object and clientSocket onto the ClientConnection constructor
                    JabberDatabase database = new JabberDatabase();
                    // New Client Connection Thread is started
                    Runnable connection = new ClientConnection(clientSocket, database);
                    Thread connectionThread = new Thread(connection);
                    connectionThread.start();

                }

            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println("<< Timed out... >>");
            }

            // Put thread to sleep so it allows other threads to continue
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public static void main(String[] args) {
        // Start Server Thread
        Runnable server = new JabberServer();
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}

