import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client_2015085 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//default host and port		
		String host = "localhost";
		int port = 1222;

		if (args.length != 2) {
			System.err.println("Considering localhost and Port 1222");
			// System.exit(1);
		}
		else {
			host = args[0];
			port = Integer.parseInt(args[1]);
		}

		try {
			Socket socket = new Socket(host, port);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

			String inp;
			while ((inp = stdIn.readLine()) != null) {
				out.println(inp);
				System.out.println("Received from Server: " + reverseString(in.readLine()));
			}

			in.close();
			out.close();
			socket.close();
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

	public static String reverseString(String s) {
        StringBuilder rev = new StringBuilder(s.length() + 1);
        String[] words = s.split(" ");
        for (int i = words.length - 1; i >= 0; i--) {
            rev.append(words[i]);
            rev.append(" ");
        }
        rev.setLength(rev.length() - 1);
        String rever = rev.toString();
        return (rever);
    }

}
