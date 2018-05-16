#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>
#include <fcntl.h>
#include <sys/poll.h>

int receiveData(int);
void sendData(int);

int main(int argc, char const *argv[])
{
	if (argc < 2) {
		fprintf(stderr, "%s\n", "Invalid Input");
		exit(1);
	}

	if (argv[2] == NULL) {
		argv[2] = "23";		//default port
	}

	char *host = (char *) argv[1];
	char *port = (char *) argv[2];

	struct addrinfo *ip, hints;
	memset(&hints, 0, sizeof(hints));
	
	//ip stores the IP address of the server
	//hints add restrictions to the communication between the telnet client and server
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_family = AF_UNSPEC;
	
	//find the ip address of the server from its domain name using DNS service
	if (getaddrinfo(host, port, &hints, &ip) == -1) {
		fprintf(stderr, "%s\n", "Cannot find the host");
		exit(1);
	}
	
	//create socket
	int sck = socket(ip->ai_family, ip->ai_socktype, ip->ai_protocol);
	
	if (sck == -1) {
		fprintf(stderr, "%s\n", "Unable to create socket");
		exit(1);
	}
	
	//connect to the socket
	if (connect(sck, ip->ai_addr, ip->ai_addrlen) == -1) {
		fprintf(stderr, "%s\n", "Unable to connect to socket");
		exit(1);
	}	
	freeaddrinfo(ip);

	struct sockaddr_in *ip_val;
	ip_val = (struct sockaddr_in *)ip->ai_addr;
	
	printf("-----------------------------------------------------------------------------------\nConnection established with %s... Use Ctrl+C to close the connection!\n-----------------------------------------------------------------------------------\n", inet_ntoa((struct in_addr)ip_val->sin_addr));

	// fcntl(sck, F_SETFL, O_NONBLOCK);

	while (1) {
		while (receiveData(sck) != -1) {
		}
		// printf("hi\n");
		fflush(stdin);
		fflush(stdout);
		sendData(sck);
		fflush(stdin);
		fflush(stdout);
	}
	close(sck);		//close the socket

	return 0;
}

int receiveData(int sockett) {
	// printf("in receiveData\n");
	char buff[256];
	
	int rv;
	struct pollfd ufds[1];
	ufds[0].events = POLLIN;
	ufds[0].fd = sockett;

	rv = poll(ufds, 1, 5000);		//wait for 5s for the server to send the data
	if (rv == 0) {			//if no data after 5s, stop reading from the server
		return -1;
	}
	if (rv == -1) {
		fprintf(stderr, "%s\n", "Polling error");
		exit(0);
	}
	
	int bytes = recv(sockett, buff, 255, 0);
	// perror("recv");
	if (bytes > 0) {
		buff[bytes] = '\0';
		printf("%s",buff);
	}
	if (bytes == -1) {
		fprintf(stderr, "%s\n", "Nothing to read");	
		exit(1);	
	}
	return bytes;
}

void sendData(int sockett) {
	// printf("in sendData\n");
	char *s;
	s = (char *)malloc(sizeof (char)*1000);
	fgets(s, 900, stdin);
	fflush(stdout);
	fflush(stdin);
	if (s[strlen(s)-1] != '\n'){
		strcat(s, "\r\n");
	}
	else {
		int len = strlen(s);
		s[len-1] = '\r';
		s[len] = '\n';
		s[len+1] = '\0';
	}
	 
	if (send(sockett, s, strlen(s), 0) == -1) {
		fprintf(stderr, "%s\n", "Invalid input or Unable to talk to server");
		exit(1);
	}
}