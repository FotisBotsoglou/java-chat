import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * The server class
 */
public class Server {
    private  ServerSocket serverSocket;
    private Set<Andler> users=Collections.synchronizedSet(new HashSet<>());
    public Server(int port){
        try {
            serverSocket=new ServerSocket(port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void run(){
        ExecutorService es=Executors.newFixedThreadPool(1000);
        System.out.println("Server is running...");
        while (true){
            try {
                Socket server=serverSocket.accept();
                Andler andler=new Andler(server,users);
                users.add(andler);

                es.execute(andler);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
