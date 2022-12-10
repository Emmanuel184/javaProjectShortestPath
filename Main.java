import java.util.Comparator; //comparator helped be able to use priority queue and decide how to sort base on time or cost
import java.util.PriorityQueue; //for dijkstras
import java.util.Scanner; //file read
import java.nio.file.Paths; //file read

/*
 * The Iterative backtracking is implemented in the dijkstras function...
 */
public class Main {
    public static void main(String[] args) {
        System.out.println();

        myLinkedList<myLinkedList<city>> adjacencyGraph = new myLinkedList<>(); // initalize our graph, adjacency list

        createAdjacencyGraph("./flightData.txt", adjacencyGraph); // this will read our data file, and create our graph

        // this prints out our graph in the same manner as the project doc, thought it
        // would be nice to see
        for (myLinkedList<city> i : adjacencyGraph) {
            for (city j : i) {
                if ((i.getIndex(j)) != i.getLength() - 1) {
                    System.out.print(j + " - > ");
                } else {
                    System.out.print(j);
                }
            }
            if (adjacencyGraph.getIndex(i) != adjacencyGraph.getLength() - 1) {
                System.out.println("\n|\nv");
            } else {
                System.out.println("\n");
            }
        }

        // this will print the top 3 best flights, file that store request flights
        bestFlights(adjacencyGraph, "./flightsRequested.txt");

    }

    /*
     * This will iterate through each flight request and call print top 3 shortest
     * paths if they exist.
     */
    public static void bestFlights(myLinkedList<myLinkedList<city>> adjacencyGraph, String filePath) {

        int flightNumber = 1;

        try (Scanner fileReader = new Scanner(Paths.get(filePath))) {

            fileReader.nextLine();

            while (fileReader.hasNextLine()) {

                String flightRequested = fileReader.nextLine();

                if(flightRequested.equals("")) {
                    break;
                }

                String[] flightRequestedData = flightRequested.split("\\|");

                boolean sortByTime = false;

                if (flightRequestedData[2].equals("T")) {
                    sortByTime = true;
                    System.out.println("Flight " + flightNumber + ": " + flightRequestedData[0] + ", "
                            + flightRequestedData[1] + " (Time)");
                } else {
                    sortByTime = false;
                    System.out.println("Flight " + flightNumber + ": " + flightRequestedData[0] + ", "
                            + flightRequestedData[1] + " (Cost)");
                }

                // prints top 3 shortest paths if they exist
                printFlights(adjacencyGraph, flightRequestedData[0], flightRequestedData[1], sortByTime);
                System.out.println("\n");

                flightNumber++;

            }

        } catch (Exception error) {
            System.out.println(error);
        }

    }

    /*
     * This will take care of actually printing the shortest paths one by one, we
     * create 3 copies of our original adjacency graph
     * since we will be modifying them to find the next shortest path, we call
     * dijkstras which returns a stack that holds the shortest path with cities
     */

    public static void printFlights(myLinkedList<myLinkedList<city>> adjacencyGraph, String source, String dest,
            boolean sortByTime) {

        int pathNumber = 1;
        myLinkedList<myLinkedList<city>> copy1 = createCopy(adjacencyGraph); // create copies
        myLinkedList<myLinkedList<city>> copy2 = createCopy(adjacencyGraph);
        myStack<city> backTrack = dijkstras(adjacencyGraph, source, dest, sortByTime); // get path from dijkstras
        if (backTrack == null) {
            System.out.println("No Paths Exist for this flight"); // if backtrack == null here that means one of the
                                                                  // cities is not in our file or they cant be reached
            return;
        }

        // print path and return the edge to be removed to get the next shortest path
        city nextEdgeToRemove = printPathWithCosts(backTrack, adjacencyGraph, new city(source), pathNumber);

        pathNumber++;
        /*
         * Rest of code does same as the commented code above...
         */
        myLinkedList<city> firstCityListToRemove = createCopyOfCityList(
                createCopyOfCityList(copy1.get(new myLinkedList<>(new city(nextEdgeToRemove.getParentCityName())))));
        copy1.get(firstCityListToRemove).remove(firstCityListToRemove.getIndex(nextEdgeToRemove));
        copy2.get(firstCityListToRemove).remove(firstCityListToRemove.getIndex(nextEdgeToRemove));
        myStack<city> backTrack2 = dijkstras(copy1, source, dest, sortByTime);
        city nextEdgeToRemove2 = printPathWithCosts(backTrack2, copy1, new city(source), pathNumber);
        pathNumber++;
        myLinkedList<city> secondCityToRemove = createCopyOfCityList(
                copy2.get(new myLinkedList<>(new city(nextEdgeToRemove2.getParentCityName()))));
        copy2.get(secondCityToRemove).remove(secondCityToRemove.getIndex(nextEdgeToRemove2));
        myStack<city> backTrack3 = dijkstras(copy2, source, dest, sortByTime);
        printPathWithCosts(backTrack3, copy2, new city(source), pathNumber);

    }

    /*
     * Dijkstras function will return a stack where we have backtracked to store the
     * shortest path from source to dest
     */
    public static myStack<city> dijkstras(myLinkedList<myLinkedList<city>> adjacencyGraph, String source, String dest,
            boolean sortByTime) {

        // check if our cities in are in our graph if they are not return null
        if (!(adjacencyGraph.isInList(new myLinkedList<>(new city(dest))))
                || !(adjacencyGraph.isInList((new myLinkedList<>(new city(source)))))) {
            return null;
        }

        // comperator for priority queue
        Comparator<city> compareCity;

        // choose which comparator to use based on the dataFile read (T or C)
        if (sortByTime) {
            compareCity = new timeComparator();
        } else {
            compareCity = new costComparator();
        }

        // array where we will store the previous city that has gotten any perticular
        // node to the shortest path...We will use this for backtrack
        city[] prevCity = new city[adjacencyGraph.getLength()];

        // weight array...use for dijkstras algorithm
        int[] dist = new int[adjacencyGraph.getLength()];

        // creates sourceCityNode to be able to start dijkstras...
        myLinkedList<city> sourceCityNode = new myLinkedList<>(new city(source));

        // initalize all weights besides our source node's weight to inf(or 99999)
        for (int i = 0; i < dist.length; i++) {
            if (!(i == adjacencyGraph.getIndex(sourceCityNode))) {
                dist[i] = 99999;
            } else {
                dist[i] = 0;
            }
        }

        // city we will need this to put our first city in queue, we cant use top one
        // because we needed to make that first to be able to
        // get index at which our source node is at for weight
        city sourceCity = new city(source, dist[adjacencyGraph.getIndex(sourceCityNode)],
                dist[adjacencyGraph.getIndex(sourceCityNode)]);

        // visited array so that we dont visist node already visited
        boolean[] visited = new boolean[adjacencyGraph.getLength()];

        // priority queue for dijkstras give comparator to be able to compare cities
        // properly based on chosen sort value
        PriorityQueue<city> dijkstrasQueue = new PriorityQueue<>(compareCity);

        // put our source city in queue to be able to get dijkstras moving
        dijkstrasQueue.offer(sourceCity);

        // dijkstras will keep going aslong as there are elements in queue
        while (!(dijkstrasQueue.isEmpty())) {
            // pop queue and get cityList that holds all "edges", which is citys that are in
            // its list
            myLinkedList<city> currentCity = adjacencyGraph.get(new myLinkedList<>(dijkstrasQueue.poll()));

            // mark visited
            visited[adjacencyGraph.getIndex(currentCity)] = true;

            // iterate through each "edge" in our currentCity is connected to
            for (city i : currentCity) {
                // aslong as not visited we will "relax the edge", basically if the current
                // citys edge is more more than our parent city cost + current edge we will
                // change the value
                if (!(visited[adjacencyGraph.getIndex(new myLinkedList<>(i))])) {
                    // couldnt find a better way besides using if, we need to store in dist[] either
                    // cost or time
                    if (sortByTime) {
                        // current edege + parents cost so far < the current cost so far of current edge
                        // we will change the value
                        if ((i.getTime() + dist[adjacencyGraph.getIndex(currentCity)]) < dist[adjacencyGraph
                                .getIndex(new myLinkedList<>(i))]) {
                            dist[adjacencyGraph.getIndex(new myLinkedList<>(i))] = i.getTime()
                                    + dist[adjacencyGraph.getIndex(currentCity)];
                            prevCity[adjacencyGraph.getIndex(new myLinkedList<>(i))] = currentCity.getHead().element;
                        }
                        dijkstrasQueue
                                .offer(new city(i.getCityName(), dist[adjacencyGraph.getIndex(new myLinkedList<>(i))],
                                        dist[adjacencyGraph.getIndex(new myLinkedList<>(i))]));
                    } else {
                        if ((i.getCost() + dist[adjacencyGraph.getIndex(currentCity)]) < dist[adjacencyGraph
                                .getIndex(new myLinkedList<>(i))]) {
                            dist[adjacencyGraph.getIndex(new myLinkedList<>(i))] = i.getCost()
                                    + dist[adjacencyGraph.getIndex(currentCity)];
                            prevCity[adjacencyGraph.getIndex(new myLinkedList<>(i))] = currentCity.getHead().element;
                        }

                        // queue each city
                        dijkstrasQueue
                                .offer(new city(i.getCityName(), dist[adjacencyGraph.getIndex(new myLinkedList<>(i))],
                                        dist[adjacencyGraph.getIndex(new myLinkedList<>(i))]));
                    }
                }
            }
        }

        // stack where our path will be stored
        myStack<city> backTrackPath = new myStack<>();

        // we will start at our dest city
        city currentCity = new city(dest);

        // push to stack
        backTrackPath.push(new city(dest));

        // backtracking applied here, basically go to our dest index in our prevCity
        // array, and push whatever city is there
        // keep doing that for each city, you will eventually hit the source node which
        // will have a value of null
        while (prevCity[adjacencyGraph.getIndex(new myLinkedList<>(currentCity))] != null) {
            currentCity = prevCity[adjacencyGraph.getIndex(new myLinkedList<>(currentCity))];
            city nextCity = new city(currentCity.getCityName());
            backTrackPath.push(nextCity);
        }

        // return the stack
        return backTrackPath;

    }

    /*
     * Creates adjacency Graph by reading file and adding edges, intial cities into
     * our graph
     */
    public static void createAdjacencyGraph(String filePath, myLinkedList<myLinkedList<city>> adjacencyGraph) {

        try (Scanner fileReader = new Scanner(Paths.get(filePath))) {

            fileReader.nextLine();

            while (fileReader.hasNextLine()) {
                

                String flightData = fileReader.nextLine();

                if(flightData.equals("")) {
                    break;
                }

                String[] flightDataParts = flightData.split("\\|");

                myLinkedList<city> firstSourceCity = new myLinkedList<>(new city(flightDataParts[0]));

                myLinkedList<city> secondSoureCity = new myLinkedList<>(new city(flightDataParts[1]));

                if (!(adjacencyGraph.isInList(firstSourceCity))) {

                    adjacencyGraph.addAtTail(firstSourceCity);
                }

                if (!(adjacencyGraph.isInList(secondSoureCity))) {
                    adjacencyGraph.addAtTail(secondSoureCity);
                }

                adjacencyGraph.get(firstSourceCity)
                        .addAtTail(new city(flightDataParts[1], firstSourceCity.getHead().element.getCityName(),
                                Integer.parseInt(flightDataParts[2]), Integer.parseInt(flightDataParts[3])));

                adjacencyGraph.get(secondSoureCity)
                        .addAtTail(new city(flightDataParts[0], secondSoureCity.getHead().element.getCityName(),
                                Integer.parseInt(flightDataParts[2]), Integer.parseInt(flightDataParts[3])));
            }
        } catch (Exception error) {
            System.out.println(error);
        }
    }

    /*
     * This function will print shortest path this will print our our path, using
     * the stack where the path is stored
     * because od the way I implemented the shortest path algo, We also calculate
     * the cost of the path here. we just get next element
     * from stack and print it/get its cost&time and sum them.
     */
    public static city printPathWithCosts(myStack<city> backTrackPath, myLinkedList<myLinkedList<city>> adjacencyGraph,
            city source, int pathNumber) {

        int cost = 0;
        int time = 0;

        city prevCity = new city(source.getCityName());
        if (adjacencyGraph.get(new myLinkedList<>(prevCity)).get(backTrackPath.peek()) != null) {
            System.out.print("Path " + pathNumber + ": ");
        }
        city currentCity = null;
        while (!(backTrackPath.isEmpty())) {

            currentCity = adjacencyGraph.get(new myLinkedList<>(prevCity)).get(backTrackPath.pop());
            myLinkedList<city> cityLiinkedList = adjacencyGraph.get(new myLinkedList<>(prevCity));
            city i = cityLiinkedList.get(currentCity);
            if (currentCity != null) {

                if (!(backTrackPath.isEmpty())) {
                    System.out.print(i + " -> ");
                    if (!(i.equals(source))) {
                        cost += i.getCost();
                        time += i.getTime();
                        prevCity = currentCity;
                    }

                } else {
                    System.out.print(i + ". ");
                    cost += i.getCost();
                    time += i.getTime();
                }
            }

        }

        if (currentCity != null) {
            System.out.print("Time: " + time + " Cost: " + cost + "\n");
        }

        return currentCity;

    }

    /*
     * creates copy of graph I needed this because I needed to modify graph without
     * messing original graph
     * 
     */
    public static myLinkedList<myLinkedList<city>> createCopy(myLinkedList<myLinkedList<city>> adjacencyGraph) {

        myLinkedList<myLinkedList<city>> returnGraph = new myLinkedList<>();

        for (myLinkedList<city> i : adjacencyGraph) {

            returnGraph.addAtTail(new myLinkedList<>(i.getHead().element));

        }

        for (myLinkedList<city> i : returnGraph) {

            for (city j : adjacencyGraph.get(i)) {

                if (!(i.getHead().element.equals(j))) {
                    i.addAtTail(new city(j.getCityName(), i.getHead().element.getCityName(), j.getCost(), j.getTime()));
                }
            }
        }

        return returnGraph;

    }

    /*
     * same as copy for graph, needed to be ably to create copy of lists in our
     * graph so that the original graph is not modified
     * 
     */
    public static myLinkedList<city> createCopyOfCityList(myLinkedList<city> listFromOriginalGraph) {
        myLinkedList<city> returnList = new myLinkedList<>(
                new city(listFromOriginalGraph.getHead().element.getCityName()));

        for (city i : listFromOriginalGraph) {
            if (!(i.equals(returnList.getHead().element))) {
                returnList.addAtTail(i);
            }
        }

        return returnList;

    }

}

/*
 * Comparators for city we pass these to priority queue so that they know what
 * to compare cities on, cost or time.
 */
class timeComparator implements Comparator<city> {
    @Override
    public int compare(city a, city b) {
        return a.getTime() - b.getTime();
    }
}

class costComparator implements Comparator<city> {
    @Override
    public int compare(city a, city b) {
        return a.getCost() - b.getCost();
    }
}
