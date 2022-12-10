javac main.java city.java myLinkedList.java myStack.java
java main
    |
    v
runs program fine on different machines.

This project involves creting a weighted graph from data that involves flights between cities, and then finding the 3 shorted path between two cities. We are creating a graph using an adjacency list that will be created using our own linked list class. this adjacency list will be a linked list of linked list, where the first linked list will have n, number of nodes where n is the total number of cities in our data and each node is another linked list. Each of these linked list will have as head the city it represents. The inner linked list represnts all the cities the parent city is connected to. This program reads the data from a ./flightData.txt file in the same directory as the main program.

Flight data should follow following format for program to work:

# of flights in graph
Source|Dest|Cost|Time
.
.
.

Once we have our completed graph from our data file, we will read the requested flight file. This file will contain which city we want to start with and which to end with. We then get the 3 shortest path between these two cities.

 This file should contain its data in the following format for this program to work

# of flights requested
Source|Dest|T or Source|Dest|C (depending on if want shortest path based on cost or time, C = cost, T = Time)
.
.
.

The way we will be finding the 3 shortest path between two nodes, is by implementing dijkstras algorithm to find the shortest path. Once we find this shortest path we will use our stack class and iterative backtracking to be able to get the path from source to our dest using an array where we store which is the previous node that gives us the shortest path to our current node in our backtrack.

The way I found the next shortest path was by removing the last edge, in our path and running dijkstras algorithm again, and doing this once more for our third path.

Wont work if graph involves a vertex having more than 1 edge to a another vertex, because we use getIndex() method alot and if two edges are added to the graph and in some parts of our program we dont pass the actual "city" we create instances of their objects and abuse the equal method in all of our classes.

