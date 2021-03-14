/** SERVER                                 February 2021
 * 
 * Server accepts client connections, reads requests from clients,
 * and sends replies to clients, all in accordance with the rules of the protocol.
 * 
 * The following PROTOCOL is implemented:
 * 
 * If ( the Server receives the request "Time", from a Client )
 *      then : the server will send back the current time
 * 
 * If ( the Server receives the request "Echo message", from a Client )
 *      then : the server will send back the message 
 * 
 * If ( the Server receives the request it does not recognize  )
 *      then : the server will send back the message "Sorry, I don't understand"
 * 
 * This is an example of a simple protocol, where the server's response is
 * based on the client's request.
 */
package dkit.sd2.oop;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class Server 
{      
    public static void main(String[] args)
    {
        Server server = new Server();
        server.start();
    }
    
    public void start()
    {
        try {
            ServerSocket ss = new ServerSocket(8080);
            System.out.println("Server Message: Server started. Listening for connections...");
            
            while(true)
            {
                Socket socket = ss.accept();  // wait for client to connect, and open a socket with the client
                
                System.out.println("Server Message: A Client has connected.");
                
                Scanner in = new Scanner(socket.getInputStream());
                String command = in.nextLine();
                
                System.out.println("Server message: Received from client : \"" + command + "\"");
                           
                OutputStream os = socket.getOutputStream();
                PrintWriter out = new PrintWriter(os, true);
                if(command.startsWith("Time"))
                {
                    LocalTime time =  LocalTime.now();
                    out.print(time);
                }
                else if(command.startsWith("Echo"))
                {
                    command = command.substring(5); // strip off the 'Echo ' part
                    out.print( command+"\n");
                }
                else
                {
                    out.print("I'm sorry i don't understand :(\n");
                }
                out.flush();  // force the response to be sent
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Server Message: IOException: " + e);
        }
    }
    
}
