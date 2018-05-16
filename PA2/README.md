## PA2: Implementation of Multi-threaded Chat Server & Client
Tasks:
1. You will develop a simple multi-threaded Server in Java that is capable of processing multiple requests i.e. more than one client should be able to connect to the server.
2. Your server should be able to maintain connections with client as long as the client wants to maintain it.

Evaluation:
1. You will start your server at a certain port. (1 mark)
2. Multiple clients can be started to connect to the server at the given port. Every time a client connects to the server, the server displays that a new client is connected and assigns it a number . e.g.: `Client 1 connected` (1 mark)
3. A client may send a string message. The message will be displayed at the Server . e.g.: `Received from Client 1: ...` (1 mark)
4. The Server will return the String by reversing it word wise i.e. if the original string is `hello Hi`, the reply from the server will be `Hi hello`. The client will display the string received . e.g.: `Received from Server: ...` (1 mark)
5. When a client disconnects, the server disconnects and displays the message . e.g.: `Client 1 disconnected` (1 mark)