import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

/**
 * the handler class to handle the communication between  server and client side
 */
public class Andler implements Runnable{
    private Socket client;
    private Set<Andler> users;
    private Scanner readFromClient;
    private PrintWriter writeToClient;
    public Andler(Socket client,Set<Andler> users){
        this.client=client;
        this.users=users;
        try {
            readFromClient=new Scanner(client.getInputStream(),StandardCharsets.UTF_8);
            writeToClient=new PrintWriter(client.getOutputStream(),true,StandardCharsets.UTF_8);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String username=readFromClient.nextLine();
        broadcast(users,username,"logged in.");
        try {
            while (true){
                String message=readFromClient.nextLine();
                if (message.equalsIgnoreCase("quit")){
                    break;
                }
                broadcast(users,username,message);
            }
            broadcast(users,username,"logged out.");
        }catch (NoSuchElementException e){
            users.remove(this);
            broadcast(users,username,"logged out.");
        }
    }

    private PrintWriter getOutputStream(){
        return writeToClient;
    }
    private static synchronized void broadcast(Set<Andler> users,String username,String message){
        for (var user:users){
            user.getOutputStream().println("["+username+"] "+message);
        }
        System.out.println("["+username+"] "+message);
    }
}
