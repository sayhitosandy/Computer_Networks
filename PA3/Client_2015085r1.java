import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client_2015085r1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//default host and port		
		String host = "localhost";
		int port = 1222;

		if (args.length != 2) {
			System.err.println("Considering localhost and Port 1222");
			// System.exit(1);
		}
		else {		//input host and port as command line arguments
			host = args[0];
			port = Integer.parseInt(args[1]);
		}

		try {
			Socket socket = new Socket(host, port);

			ClientJobs thr = new ClientJobs(socket);		//listen on socket for any incoming msg from server
			thr.start();

			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

			String inp;
			while ((inp = stdIn.readLine()) != null) {		//read data from standard input and send to server
				out.println(inp);							//output data received from server
			}

			in.close();
			out.close();
			// socket.close();
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}

class ClientJobs extends Thread {			//listen for any incoming msgs from server
	private Socket socket = null;

	public ClientJobs(Socket sk) {
		socket = sk;
	}

	public void run() {
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			String inp;
			while ((inp = in.readLine()) != null) { 
				System.out.println(inp);
			}
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
