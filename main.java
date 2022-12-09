import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.nio.file.Paths;

public class main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String flightDataFilePath = "./flightData.txt";

        myLinkedList<myLinkedList<city>> adjacencyGraph = new myLinkedList<>();

        createAdjacencyGraph(flightDataFilePath, adjacencyGraph);

        // for (myLinkedList<city> cityList : adjacencyGraph) {
        // for (city city : cityList) {
        // if (!(cityList.getHead().element.equals(city))) {
        // System.out.println(
        // "Flight: " + cityList.getHead().element + " -> " + city + ": Cost: " +
        // city.getCost()
        // + " Time: "
        // + city.getTime());
        // }
        // }

        // }

        System.out.println("\n------------------");

        String flightRequestedPath = "./flightsRequested";

        bestFlights(adjacencyGraph, flightRequestedPath);

    }

    public static void bestFlights(myLinkedList<myLinkedList<city>> adjacencyGraph, String filePath) {

        int flightNumber = 1;

        try (Scanner fileReader = new Scanner(Paths.get(filePath))) {

            int numberOfFlightsRequested = Integer.parseInt(fileReader.nextLine());

            while (fileReader.hasNextLine()) {

                String flightRequested = fileReader.nextLine();

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

                printFlights(adjacencyGraph, flightRequestedData[0], flightRequestedData[1], sortByTime);

                System.out.println("\n\n--------------------------------------");

            }

        } catch (Exception error) {
            System.out.println(error);
        }

    }

    public static void printFlights(myLinkedList<myLinkedList<city>> adjacencyGraph, String source, String dest,
            boolean sortByTime) {

        System.out.print("Path 1: ");
        myStack<city> backTrack = dijkstras(adjacencyGraph, source, dest, sortByTime);
        printPathWithCosts(backTrack, adjacencyGraph, sortByTime, new city(source));

    }

    public static myStack<city> dijkstras(myLinkedList<myLinkedList<city>> adjacencyGraph, String source, String dest,
            boolean sortByTime) {

        Comparator<city> compareCity;

        if (sortByTime) {
            compareCity = new timeComparator();
        } else {
            compareCity = new costComparator();
        }

        city[] prevCity = new city[adjacencyGraph.getLength()];

        int[] dist = new int[adjacencyGraph.getLength()];

        myLinkedList<city> sourceCityNode = new myLinkedList<>(new city(source));

        for (int i = 0; i < dist.length; i++) {
            if (!(i == adjacencyGraph.getIndex(sourceCityNode))) {
                dist[i] = 99999;
            } else {
                dist[i] = 0;
            }
        }

        city sourceCity = new city(source, dist[adjacencyGraph.getIndex(sourceCityNode)],
                dist[adjacencyGraph.getIndex(sourceCityNode)]);

        boolean[] visited = new boolean[adjacencyGraph.getLength()];

        PriorityQueue<city> dijkstrasQueue = new PriorityQueue<>(compareCity);

        dijkstrasQueue.offer(sourceCity);

        while (!(dijkstrasQueue.isEmpty())) {
            myLinkedList<city> currentCity = adjacencyGraph.get(new myLinkedList<>(dijkstrasQueue.poll()));

            visited[adjacencyGraph.getIndex(currentCity)] = true;

            for (city i : currentCity) {
                if (!(visited[adjacencyGraph.getIndex(new myLinkedList<>(i))])) {
                    if (sortByTime) {
                        if ((i.getTime() + dist[adjacencyGraph.getIndex(currentCity)]) < dist[adjacencyGraph
                                .getIndex(new myLinkedList<>(i))]) {
                            dist[adjacencyGraph.getIndex(new myLinkedList<>(i))] = i.getTime()
                                    + dist[adjacencyGraph.getIndex(currentCity)];
                            prevCity[adjacencyGraph.getIndex(new myLinkedList<>(i))] = currentCity.getHead().element;
                        }
                        dijkstrasQueue.offer(new city(i.cityName, dist[adjacencyGraph.getIndex(new myLinkedList<>(i))],
                                dist[adjacencyGraph.getIndex(new myLinkedList<>(i))]));
                    } else {
                        if ((i.getCost() + dist[adjacencyGraph.getIndex(currentCity)]) < dist[adjacencyGraph
                                .getIndex(new myLinkedList<>(i))]) {
                            dist[adjacencyGraph.getIndex(new myLinkedList<>(i))] = i.getCost()
                                    + dist[adjacencyGraph.getIndex(currentCity)];
                            prevCity[adjacencyGraph.getIndex(new myLinkedList<>(i))] = currentCity.getHead().element;
                        }
                        dijkstrasQueue.offer(new city(i.cityName, dist[adjacencyGraph.getIndex(new myLinkedList<>(i))],
                                dist[adjacencyGraph.getIndex(new myLinkedList<>(i))]));
                    }
                }
            }
        }

        myStack<city> backTrackPath = new myStack<>();

        city currentCity = new city(dest);

        backTrackPath.push(new city(dest));

        while (prevCity[adjacencyGraph.getIndex(new myLinkedList<>(currentCity))] != null) {
            currentCity = prevCity[adjacencyGraph.getIndex(new myLinkedList<>(currentCity))];
            city nextCity = new city(currentCity.cityName);
            backTrackPath.push(nextCity);
        }

        return backTrackPath;

    }

    public static void createAdjacencyGraph(String filePath, myLinkedList<myLinkedList<city>> adjacencyGraph) {

        try (Scanner fileReader = new Scanner(Paths.get(filePath))) {

            int numberOfFlights = Integer.parseInt(fileReader.nextLine());

            while (fileReader.hasNextLine()) {

                String flightData = fileReader.nextLine();

                String[] flightDataParts = flightData.split("\\|");

                myLinkedList<city> firstSourceCity = new myLinkedList<>(new city(flightDataParts[0]));

                myLinkedList<city> secondSoureCity = new myLinkedList<>(new city(flightDataParts[1]));

                if (!(adjacencyGraph.isInList(firstSourceCity))) {

                    adjacencyGraph.addAtTail(firstSourceCity);
                }

                if (!(adjacencyGraph.isInList(secondSoureCity))) {
                    adjacencyGraph.addAtTail(secondSoureCity);
                }

                adjacencyGraph.get(firstSourceCity).addAtTail(new city(flightDataParts[1],
                        Integer.parseInt(flightDataParts[2]), Integer.parseInt(flightDataParts[3])));

                adjacencyGraph.get(secondSoureCity).addAtTail(new city(flightDataParts[0],
                        Integer.parseInt(flightDataParts[2]), Integer.parseInt(flightDataParts[3])));
            }
        } catch (Exception error) {
            System.out.println(error);
        }
    }

    public static void printPathWithCosts(myStack<city> backTrackPath, myLinkedList<myLinkedList<city>> adjacencyGraph,
            boolean sortByTime, city source) {

        int cost = 0;
        int time = 0;

        city prevCity = new city(source.cityName);
        while (!(backTrackPath.isEmpty())) {

            city currentCity = backTrackPath.pop();
            myLinkedList<city> cityLiinkedList = adjacencyGraph.get(new myLinkedList<>(prevCity));
            city i = cityLiinkedList.get(currentCity);
            if (!(backTrackPath.isEmpty())) {
                System.out.print(i + " -> ");
                if (!(i.equals(source))) {
                    cost += i.getCost();
                    time += i.getTime();
                }

            } else {
                System.out.print(i + ". ");
                cost += i.getCost();
                time += i.getTime();
            }

            prevCity = currentCity;
        }

        System.out.print("Time: " + time + " Cost: " + cost);

    }

}

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
