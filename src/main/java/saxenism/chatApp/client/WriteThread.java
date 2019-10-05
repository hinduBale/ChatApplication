package saxenism.chatApp.client;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.net.*;

public class WriteThread extends Thread{
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    public WriteThread(Socket socket, ChatClient client)
    {
        this.socket = socket;
        this.client = client;

        try
        {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        }
        catch(IOException e)
        {
            System.out.println("Error getting output stream: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void run() {
        Console console = System.console();

        String userName = console.readLine("\nEnter your name: ");
        client.setUserName(userName);
        writer.println(userName);

        String text;
        do {
            text = console.readLine("[" + userName + "]: ");
            writer.println(text);
        } while (!text.equals("Bye") || !text.equals("bye") || !text.equals("BYE"));

        try {
            socket.close();
        }
        catch(IOException e)
        {
            System.out.println("Error writing to server: " + e.getMessage());
        }
    }

}

