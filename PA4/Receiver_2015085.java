import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Receiver_2015085 {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int portNo = 1222;
		InetAddress address = InetAddress.getByName("localhost");

		// System.out.println(args.length);

		if (args.length > 0) {
			portNo = Integer.parseInt(args[0]);
		}
		System.out.println("UDP Server active at " + address + ", Port: " + portNo);

		int prevAck = 0; // cummulative ack
		List<Integer> packets = new ArrayList<Integer>(); // packets received
															// list

		byte[] recBuff = new byte[1300];
		byte[] sendBuff = new byte[1300];

		try {
			DatagramSocket receiverSocket = new DatagramSocket(portNo);

			while (true) {// Server remains always on
				DatagramPacket receivePacket = new DatagramPacket(recBuff, recBuff.length);
				System.out.println("Waiting for Packet from Sender");
				receiverSocket.receive(receivePacket);

				ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData());
				DataInputStream dais = new DataInputStream(bais);
				int seqNo = dais.readInt();
				packets.add(seqNo); // Add sequence number to list
				int packetSize = receivePacket.getLength();
				System.out.println("Recieved Packet size is " + packetSize);
				System.out.println("Sequence number is " + seqNo);

				Collections.sort(packets);
				boolean flag = false;
				int i = 0;
				for (; i < 100000;) {
					if (packets.contains(i) == false) { // Packet Missing?
						flag = true;
						break;
					}
					i += 1000;
				}
				if (flag == true) {
					prevAck = i; // send prevAck
				}

				InetAddress IPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();
				System.out.println("From: " + IPAddress + " :" + port);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream daos = new DataOutputStream(baos);
				daos.writeInt(prevAck);
				sendBuff = baos.toByteArray();
				System.out.println("Send ACK with Acknowledgement No: " + prevAck);
				DatagramPacket sendPacket = new DatagramPacket(sendBuff, sendBuff.length, IPAddress, port);
				receiverSocket.send(sendPacket);
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
