package saxenism.chatApp.server;

import java.util.*;
import java.net.*; //Contains all the Socket class and other related shit!!
import java.io.*;

public class chatServer {
    private int port;
    private Set <String> userNames = new HashSet<>();  //A set of usernames, since it is implemented using HashSets, it removes duplicates but does not sort entries.
    private Set <UserThread> userThreads = new HashSet<>(); //A set of instances of userThreads -> basically each instance is for a new user.

    public chatServer(int port){
        this.port = port;
    } //this.port refers to parameter wala port. Aaj phir kitthe chali aaye morni ban k!!

    public void execute()
    {
        try(ServerSocket serverSocket = new ServerSocket(port)) //trying to see whether an instance of ServerSocket is created or not
        {
            System.out.println("Chat server would be listening on port: " + port);
            while(true)
            {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(socket, this); //Now, since the new user is connected an instance of UserThread is created and initialised with socket and ChatServer instance.
                userThreads.add(newUser); //Add the recently created instance of userThread to the list of userNames.
                newUser.start();
            }
        }
        catch(IOException ioex)
        {
            System.out.println("Error on the server side: " + ioex.getMessage());
            ioex.printStackTrace(); //A method of the class IOException.
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-server>");
            System.exit(0); //Means close the application if no parameter is passed while calling the main function
        }
        int port = Integer.parseInt(args[0]); //args is the array of Strings mentioned in the main function
        chatServer server = new chatServer(port);
        server.execute();
    }

    void broadcast(String message, UserThread excludeUser)
    {
        for(UserThread aUser: userThreads)
        {
            if(aUser != excludeUser)
                aUser.sendMessage(message);
        }
    }

    void addUserName (String userName)
    {
        userNames.add(userName);
    }

    void removeUser (String userName, UserThread aUser)
    {
        boolean removed = userNames.remove(userName);
        if(removed)
        {
            userThreads.remove(userName);
            System.out.println("The user "+userName+" exited the chat"); //This message will be for the server admin
        }
    }

    Set <String> getUserNames()
    {
        return this.userNames;
    }

    boolean hasUsers()
    {
        return !this.userNames.isEmpty();
    }
}

