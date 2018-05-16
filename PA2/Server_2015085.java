import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_2015085 {

    private static Socket client = null;
    private static ServerSocket socket = null;

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        //default port
        int port = 1222;

        if (args.length != 1) {
            System.err.println("Considering Port 1222");
            // System.exit(1);
        }
        else {
            port = Integer.parseInt(args[0]);
        }

        try {
            socket = new ServerSocket(port);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        int countClients = 0;
        while(true) {
            try {
                client = socket.accept();
                countClients++;
                ClientThread thr = new ClientThread(client, countClients);
                thr.start();

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}

class ClientThread extends Thread {
    private Socket client = null;
    private int clientNo = 0;

    public ClientThread(Socket socket, int n) {
        clientNo = n;
        client = socket;
    }

    public void run() {
        try {
            System.out.printf("Client %d Connected\n", clientNo);
                
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            String input;
            while ((input = in.readLine()) != null) {
                System.out.printf("Received from client %d: %s\n", clientNo, input);
                out.println(input);
            }

            in.close();
            out.close();
            System.out.printf("Client %d Disconnected\n", clientNo);
            client.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
