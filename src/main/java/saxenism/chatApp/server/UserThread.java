package saxenism.chatApp.server;

import java.util.*;
import java.text.*;
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

    DateFormat df = new SimpleDateFormat("dd_MM_yy_HH_mm_ss");
    Date dateobj = new Date();
    String filePath ="C:\\Users\\Rahul\\Desktop\\saxenismChat\\"+df.format(dateobj)+".txt";
    public void run()
    {
        try
        {
            File file = new File(filePath);
            FileWriter fileWriter = new FileWriter(file, true);
            PrintWriter pWrite = new PrintWriter(fileWriter);

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
                serverMessage = "[" + userName + "]: " + clientMessage;
                pWrite.println(serverMessage);
                server.broadcast(serverMessage, this);
            }while(!clientMessage.equals("bye"));

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

