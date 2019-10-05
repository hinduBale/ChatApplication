package saxenism.chatApp.server;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.util.*;
import java.net.*;
import java.io.*;

public class UserThread extends Thread {
    private Socket socket;
    private chatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, chatServer server)
    {
        this.socket = socket;
        this.server = server;
    }
    public void run() 
    {
        try
        {
            InputStream input = socket.getInputStream();//The getInputStream method of the Java socket class returns an input stream for the given socket.
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine(); //reader(an instant  of BufferedReader) reads the name of the new user willing to connect.
            server.addUserName(userName);

            String serverMessage = "Welcome "+userName+" to the chat";
            server.broadcast(serverMessage, this);

            String clientMessage;

            do
            {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]" + clientMessage;
                server.broadcast(serverMessage, this);
            }while(!clientMessage.equals("bye") || !clientMessage.equals("BYE") || !clientMessage.equals("Bye"));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " has left the chat :("; //This message will be for all other users in the chat.
            server.broadcast(serverMessage, this);
        }
        catch(IOException e)
        {
            System.out.println("Error in UserThread: "+ e.getMessage());
            e.printStackTrace();
        }
    }

    void printUsers()
    {
        if(server.hasUsers())
            writer.println("Connected users are: " + server.getUserNames());
        else
            writer.println("No other users are online right now.");
    }

    void sendMessage(String message)
    {
        writer.println(message);
    }
}

