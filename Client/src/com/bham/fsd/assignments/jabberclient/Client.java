package com.bham.fsd.assignments.jabberclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static void main(String argv[]) {
		String MsgReceived;
		PrintWriter ToServer = null;
		BufferedReader FromServer = null;
		// Create a socket to connect to the server

		try (Socket clientSocket = new Socket("127.0.0.1", 44444)) {

			System.out.println("Client Running ........");

			Scanner inFromUserKeyboard = new Scanner(System.in);
			// writing to server
			ToServer = new PrintWriter(clientSocket.getOutputStream(), true);

			// reading from server
			FromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			System.out.println("Enter your Budget:");

			double budget = inFromUserKeyboard.nextDouble();

			System.out.println("You entered " + budget);

			// Send Msg to the server
			ToServer.println(budget);
			ToServer.flush();
			System.out.println("Budget sent to Server. Now wait for response");
			// Receive Msg from the server
			MsgReceived = FromServer.readLine();
			// displaying server reply
			System.out.println("MSG FROM SERVER: Number of titles " + MsgReceived);

			// closing the scanner object
			inFromUserKeyboard.close();

		} catch (IOException e) {
			e.printStackTrace();
		}  

	}
}
