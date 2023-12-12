import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

import java.net.Socket;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * the client class with two threads in order to let
 * the client read and write without blocking.
 */
public class Client {
    public static void main(String[] args) {
        try(Socket client=new Socket(InetAddress.getLocalHost().getHostName(),1234)) {
            ExecutorService es=Executors.newFixedThreadPool(2);
            Scanner readFromInput=new Scanner(System.in);
            Scanner readFromServer=new Scanner(client.getInputStream(),StandardCharsets.UTF_8);
            PrintWriter writeToServer=new PrintWriter(client.getOutputStream(),true,StandardCharsets.UTF_8);

            System.out.print("Enter username > ");
            String username=readFromInput.nextLine();
            writeToServer.println(username);

            es.execute(()->{
                while (true){
                    try {
                        String response=readFromServer.nextLine();
                        if (response.equalsIgnoreCase("quit")){
                            es.shutdownNow();
                            return;
                        }
                        System.out.print(response+"\n> ");
                    }catch (NoSuchElementException e){
                        return;
                    }
                }
            });
            while (true){
                System.out.print("> ");
                String request=readFromInput.nextLine();
                if (request.equalsIgnoreCase("quit")){
                    es.shutdownNow();
                    break;
                }
                writeToServer.println(request);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
