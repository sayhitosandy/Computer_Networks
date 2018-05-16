## PA4: Implementation of TCP-like functionalities over UDP
Task:
1. You need to make a reliable version of UDP by implementing TCP like functionalities over a UDP connection. 
2. In order to achieve that, you will implement the following over a stream of UDP packets; implementation should be same as in the TCP (Reno):
	a. Acknowledgement: Please implement both single packet and cumulative ack.
	b. Sequence number: same as in TCP
	c. Retransmissions 
	d. Detecting duplicates and discarding them.
	e. Congestion control (TCP Reno)
	f. Flow Control
3. You will also have to develop other necessary programs that will let us test these functionalities. This may require the following (and some more):
	a. A UDP client and server
	b. a mechanism to generate a large number of UDP packets that act as both packet or acknowledgements depending on the need.
	c. a random way of deleting some packets so that you can simulate/show loss or delayed arrival. 

You are responsible for developing everything that is required to show the five major functionalities that are required. The aim of the assignment is to develop a deeper understanding of TCP.

Programming language: Java