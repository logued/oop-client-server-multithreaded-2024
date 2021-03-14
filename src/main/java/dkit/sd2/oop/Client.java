/** CLIENT                                                  February 2021
 * 
 * This Client program asks the user to input commands to be sent to the server.
 * 
 * There are only two valid commands in the protocol: "Time" and "Echo"
 * 
 * If user types "Time" the server should reply with the current server time.
 * 
 * If the user types "Echo" followed by a message, the server will echo back the message.
 * e.g. "Echo Nice to meet you"
 * 
 * If the user enters any other input, the server will not understand, and
 * will send back a message to the effect.
 * 
 * NOte: You must run the server before running this the client.
 * (Both the server and the client will be running together on this computer)
 */
package dkit.sd2.oop;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class Client 
{
    public static void main(String[] args)
    {
        Client client = new Client();
        client.start();
    }
    
    public void start()
    {
        Scanner in = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 8080);  // connect to server socket
            
            System.out.println("Client message: The Client is running and has connected to the server");
            
            System.out.println("Please enter a command:  (\"Time\" to get time, or \"Echo message\" to get echo) \n>");
            String command = in.nextLine();
            
            OutputStream os = socket.getOutputStream();
            PrintWriter out = new PrintWriter(os, true);

            out.write(command+"\n");  // write command to socket, and newline terminator
            out.flush();              // flush (force) the command over the socket
            
            Scanner inStream = new Scanner(socket.getInputStream());  // wait for, and retrieve the reply
            
            if(command.startsWith("Time"))   //we expect the server to return a time (in milliseconds)
            {
                String timeString = inStream.nextLine();
                System.out.println("Client message: Response from server Time: " + timeString);
            }
            else                            // the user has entered the Echo command or an invalid command
            {
                String input = inStream.nextLine();
                System.out.println("Client message: Response from server: \"" + input + "\"");
            }
            
            out.close();
            inStream.close();
            socket.close();
            
        } catch (IOException e) {
            System.out.println("Client message: IOException: "+e);
        }      
    }
}


//  LocalTime time = LocalTime.parse(timeString); // Parse String -> convert to LocalTime object if required