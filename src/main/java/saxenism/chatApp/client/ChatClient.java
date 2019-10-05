package saxenism.chatApp.client;

import java.net.*;
import java.io.*;

public class ChatClient {
    private String hostname;
    private int port;
    private String userName;

    public ChatClient(String hostname, int port)
    {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute()
    {
        try
        {
            Socket socket = new Socket(hostname, port);
            System.out.println("You are now connected to the saxenismChat");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();
        }
        catch(UnknownHostException ex)
        {
            System.out.println("Please enter correct host " + ex.getMessage());
        }
        catch(IOException e)
        {
            System.out.println("I/O Error " + e.getMessage());
        }
    }

    void setUserName(String userName)
    {
        this.userName = userName;
    }

    String getUserName()
    {
        return this.userName;
    }

    public static void main(String[] args)
    {
        if(args.length < 2)
            return;
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }
}
