package multithreaded;

/**
 * CLIENT                                                  Mar 2025
 * This Client program asks the user to input commands to be sent to the server.
 * There are only two valid commands in the protocol: "time" and "echo xxxxxx"
 * If user types "time" then the string "time" is sent to the server,
 * and the server will respond by getting the current time and sending it back to the client.
 * -
 * If the user types "echo" followed by a space and a message, the server will echo back the message back to the client.
 * e.g. "echo Hi Tom"     -  will cause the server to respond with "Hi Tom"
 * -
 * If the user enters any other input, the server will not understand, and
 * will send back a message to the effect.
 * -
 * Note: You must run the server before running this the client.
 * (Both the server and the client will be running together on this computer)
 */

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class Client {

    final static int SERVER_PORT_NUMBER = 8888;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {

        try (   // Attempt to establish a connection with the server, and if successful
                // create a Socket for communication.
                Socket socket = new Socket("localhost", SERVER_PORT_NUMBER);

                // get the socket's input and output streams, and wrap them in writer and readers
                PrintWriter socketWriter= new PrintWriter(socket.getOutputStream(), true);
                BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            System.out.println("Client message: The Client is running and has connected to the server");
            //ask user to enter a command
            Scanner userInput = new Scanner(System.in);
            System.out.println("Valid commands are: \"time\" to get time, or \"echo <message>\" to get message echoed back, \"quit\"");
            System.out.println("Please enter a command: ");
            String userRequest = userInput.nextLine();

            while(true) {
                // send the command to the server on the socket
                socketWriter.println(userRequest);      // write the request to socket along with a newline terminator (which is required)
                // out.flush();                      // flushing buffer NOT necessary as auto flush is set to true

                // process the answer returned by the server
                //
                if (userRequest.startsWith("time"))   // if user asked for "time", we expect the server to return a time (in milliseconds)
                {
                    String timeString = socketReader.readLine();  // (blocks) waits for response from server, then input string terminated by a newline character ("\n")
                    System.out.println("Client message: Response from server after \"time\" request: " + timeString);
                }
                else if (userRequest.startsWith("echo")) // if the user has entered the "echo" command
                {
                    String response = socketReader.readLine();   // wait for response - expecting it to be the same message that we sent to server
                    System.out.println("Client message: Response from server: \"" + response + "\"");
                }
                else if (userRequest.startsWith("quit")) // if the user has entered the "quit" command
                {
                    String response = socketReader.readLine();   // wait for response -
                    System.out.println("Client message: Response from server: \"" + response + "\"");
                    break;  // break out of while loop, client will exit.
                }
                else {
                    System.out.println("Command unknown. Try again.");
                }

                userInput = new Scanner(System.in);
                System.out.println("Valid commands are: \"time\" to get time, or \"echo <message>\" to get message echoed back, \"quit\"");
                System.out.println("Please enter a command: ");
                userRequest = userInput.nextLine();
            }
        } catch (IOException e) {
            System.out.println("Client message: IOException: " + e);
        }
        // sockets and streams are closed automatically due to try-with-resources
        // so no finally block required here.

        System.out.println("Exiting client, but server may still be running.");
    }
}

//  LocalTime time = LocalTime.parse(timeString); // Parse String -> convert to LocalTime object if required LocalTime.parse(timeString); // Parse timeString -> convert to LocalTime object if required