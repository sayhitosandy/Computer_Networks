import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sender_2015085 {

	static InetAddress receiver_IPAddress;
	static int _MSS, dataSize, prevAck;

	static List<Integer> packets;
	static List<Long> timersOfPackets;
	static DatagramSocket senderSocket;

	static String receiver = "localhost";
	static int portNo = 1222;
	static int lossFlag = 0;

	static long time_out = 1000;
	static byte[] data;

	public Sender_2015085() throws Exception {
		packets = new ArrayList<Integer>();
		timersOfPackets = new ArrayList<Long>();
		senderSocket = new DatagramSocket();
		receiver_IPAddress = InetAddress.getByName(receiver);
		prevAck = 0;	//Initially, seqNo = 0
		_MSS = 1000;	//Packet can have a max of 1000 bytes
		dataSize = 100000;	//Total data to transmit is 100000 bytes
	}

	public static void sendPacket(int seqNo) {
		if (seqNo >= dataSize) { // Don't send. All the data has been
									// already sent.
			return;
		}

		if (seqNo == -1) { // Unnecessary Check (Debugging)
			seqNo = 0;
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream daos = new DataOutputStream(baos);
			daos.writeInt(seqNo);

			byte[] buff = new byte[_MSS];
			System.out.println("Adding SeqNo to packet and Sending: " + seqNo);
			System.arraycopy(data, seqNo, buff, 0, _MSS);
			daos.write(buff);
			byte[] buff2 = baos.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(buff2, buff2.length, receiver_IPAddress, portNo);

			if (lossFlag == 0) { // Send Packet
				senderSocket.send(sendPacket);
			} else if (lossFlag == 1) {
				if (Math.random() >= 0.1) { // Send Packet
					senderSocket.send(sendPacket);
				}
				else { // Drop Packet
					System.out.println("Packet with seqNo: " + seqNo + " dropped.");
				}
			}
			long packetTime = (new Date()).getTime();
			timersOfPackets.add(packetTime); // Start timer for packet
			packets.add(seqNo); // Add packet to packet list
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean timerExpired() {
		long currentTime = (new Date()).getTime();
		for (int i = 0; i < packets.size(); i++) {
			long timeDiff = Math.abs(timersOfPackets.get(i) - currentTime);
			if (timeDiff > time_out) {
				System.out.println("Timer expired for seqNo " + packets.get(i));
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if (args.length > 0) {
			receiver = args[0];
		}
		if (args.length > 1) {
			portNo = Integer.parseInt(args[1]);
		}
		if (args.length > 2) {
			lossFlag = Integer.parseInt(args[2]);
		}

		new Sender_2015085();

		String str = "";
		for (int i = 0; i < dataSize; i++) {
			str += "a";
		}

		data = str.getBytes();
		
		int windowSize = 1000; // Window Size
		sendPacket(prevAck); // Send 1st packet

		byte[] receiveData = new byte[1300];

		while (prevAck < (dataSize - _MSS)) { // For last
												// packet, seqNo
												// + MSS >
												// dataSize

			try {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				System.out.println("Waiting for ACK");

				senderSocket.setSoTimeout(100);
				senderSocket.receive(receivePacket);

				ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData());
				DataInputStream dais = new DataInputStream(bais);
				prevAck = dais.readInt();
				System.out.println("Recieved ACK with Acknowlegement No.:" + prevAck);

				for (int i = 0; i < packets.size(); i++) { // Clear the lists
					if (packets.get(i) < prevAck) {
						timersOfPackets.remove(i);
						packets.remove(i);
					}
				}

				if (timerExpired() == false) {
					windowSize += (_MSS * _MSS / windowSize); // increase
																// window
																// size
																// by
																// (MSS^2/W)

					for (int i = prevAck; (i + _MSS - prevAck) < windowSize;) {
						if (packets.contains(i) == false) { // if possible
															// with the new
															// window size,
															// send another
															// packet
							sendPacket(i);
						}
						i += _MSS;
					}
				} else {
					windowSize = _MSS; // TCP Reno - start from 1
					timersOfPackets.clear(); // Clear the lists
					packets.clear();
					sendPacket(prevAck);
				}
				System.out.println("New window size is " + windowSize);
			} catch (SocketTimeoutException e) { // Acknowledgement not
													// received, i.e., timeout
				for (int i = 0; i < packets.size(); i++) {
					if (packets.get(i) < prevAck) {
						timersOfPackets.remove(i);
						packets.remove(i);
					}
				}

				if (timerExpired() == true) {
					windowSize = _MSS; // TCP Reno - start from 1
					timersOfPackets.clear();
					packets.clear();
					sendPacket(prevAck);
				}
				System.out.println("Timeout Occurred");
			}
		}
		senderSocket.close();
	}
}
