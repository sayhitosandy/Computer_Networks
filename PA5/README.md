## PA5: Implementation of Routing Protocols
Tasks:
1. Your task is to implement Link-state and Bellman-ford algorithms.
2. The input would be a network topology given in the form of edge values between nodes e.g. a network of three nodes with edge values of 1, 2, and 5 will be given as `A C 1 A B 2 B C 5` which indicates that edge between A & C is of value 1 and so on.
3. The input will be in the form of a string with "spaces" to be used for parsing the string and the string will be ended by "#" i.e. `A C 1#` indicates a valid network of two nodes with edge of 1. 
4. The output of your algorithm must show each step of calculation i.e. just showing the final routing table is not sufficient.
5. After calculating the routing table, the algorithm must stop but not end; the implementation should be ready to accept a change in edge value and calculate routing tables again.

Programming language: Java