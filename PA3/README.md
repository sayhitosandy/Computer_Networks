## PA3: Extending Functionality of Multi-threaded Chat Server & Client
Building on your PA2 assignment, you will add following capabilities to make it a full-fledged chat application like Whatsapp. 

Tasks:
A client, say client `0`, should be able to send following types of messages:
a) `Client X: Message` - the message will be delivered to Client `X`.
b) `All: Message` - the message will be delivered to all present clients.
c) `Client X,Y: Message` - the message will be delivered to Client `X` and `Y` only
d) `Server: "List All"` - the message will go to server which will then reply with the list of all the clients that are currently connected.
e) If a client sends a message to another client which does not exist then the server should reply back saying the client does not exist.