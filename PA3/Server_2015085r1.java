import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server_2015085r1 {

    private static Socket client = null;				//socket for client
    private static ServerSocket socket = null;			//socket for server
    public static TreeMap<Integer, ClientThread> mapp;	//Hash Map to map client ID with client thread sorted on client ID

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        //default port
        int port = 1222;

        if (args.length != 1) {
            System.err.println("Considering Port 1222");
            // System.exit(1);
        }
        else {		//input port no as command line argument
            port = Integer.parseInt(args[0]);
        }

        try {
            socket = new ServerSocket(port);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        int countClients = 0;
        mapp = new TreeMap<> ();
        while(true) {
            try {
                client = socket.accept();
                countClients++;
                ClientThread thr = new ClientThread(client, countClients);			//create new thread for every new client
                mapp.put(countClients, thr);
                thr.start();

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}

class ClientThread extends Thread {
    private Socket client = null;		//Socket
    private int clientNo = 0;			//Client ID
    public BufferedReader inp = null;	//Input to Client
    public PrintWriter outp = null;		//Output to Client

    public ClientThread(Socket socket, int n) throws IOException{
        clientNo = n;
        client = socket;
        inp = new BufferedReader(new InputStreamReader(client.getInputStream()));
        outp = new PrintWriter(client.getOutputStream(), true);
    }

    public void run() {
        try {
            System.out.printf("Client %d Connected\n", clientNo);
                
            String input;
            String[] words;
            while ((input = inp.readLine()) != null) {
                
            	words = input.split(" ");
            	
            	if (words[0].equals("Client") == true) {			//Msg of type -> Client X,Y: Msg
            		try {
	            		String clients = words[1].substring(0, words[1].indexOf(":"));		//Find clients
	            		String[] clientsNo = clients.split(",");
	            		for (String i : clientsNo) {
	            			int no = Integer.parseInt(i);
							if (Server_2015085r1.mapp.containsKey(no) == true) {
	            				PrintWriter targetOut = Server_2015085r1.mapp.get(no).outp;
	            				targetOut.printf("Received from Client %d: %s\n", clientNo, input.substring(input.indexOf(":")+2, input.length()));
	            			}
	            			else {
	            				outp.printf("Server: Client %d does not exist\n", no);
	            			}
	            		}
	            	}
            		catch (Exception e) {
            			outp.println("Server: Invalid Input...Correct Format -> Client X,Y,Z: Msg");
            		}
            		
            	}

       			else if (words[0].contains("All") == true) {		//Send msg to all the acitve clients
       				PrintWriter targetOut;
       				Set <Map.Entry<Integer, ClientThread> > values = Server_2015085r1.mapp.entrySet();

       				for (Map.Entry<Integer, ClientThread> me : values) {
       					targetOut = me.getValue().outp;
       					targetOut.printf("Received from Client %d: %s\n", clientNo, input.substring(input.indexOf(":")+2, input.length()));
       				}
       			}

       			else if (words[0].contains("Server") == true && words[1].equals("List") == true && words[2].equals("All") == true) {		//Ask server to list the active clients
       				Set <Map.Entry<Integer, ClientThread> > values = Server_2015085r1.mapp.entrySet();

       				String clients = "";
       				for (Map.Entry<Integer, ClientThread> me : values) {
       					clients += me.getKey() + ", ";
       				}	
       				outp.println("Server: " + clients.substring(0, clients.length()-2));		//print comma separated list of active clients
       			}

       			else {
       				outp.println("Server: Invalid Input");
       			}
            }

            inp.close();
            outp.close();
            System.out.printf("Client %d Disconnected\n", clientNo);
            client.close();
            Server_2015085r1.mapp.remove(clientNo);			//remove the client once the connection is closed
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
