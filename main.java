import java.util.Scanner;
import java.nio.file.Paths;

public class main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String flightDataFilePath = "./flightData.txt";

        myLinkedList<myLinkedList<city>> adjacencyGraph = new myLinkedList<>();

        createAdjacencyGraph(flightDataFilePath, adjacencyGraph);

        for (myLinkedList<city> cityList : adjacencyGraph) {
            for (city city : cityList) {
                if (!(cityList.getHead().element.equals(city))) {
                    System.out.println(
                            "Flight: " + cityList.getHead().element + " -> " + city + ": Cost: " + city.getCost() + " Time: "
                                    + city.getTime());
                }
            }

        }

        String requestedFlightFile = "./requestedFlights";

        bestFlights(adjacencyGraph, requestedFlightFile);

        myStack<String> newStack = new myStack<>();

        newStack.push("LOL");
        newStack.push("xD");
        newStack.push("rofl");
        newStack.push("lol");
        newStack.push("xD");

        System.out.println(newStack.pop());
        System.out.println(newStack.pop());
        System.out.println(newStack.pop());
        System.out.println(newStack.pop());
        System.out.println(newStack.pop());
        


        System.out.println("STOP!!XD");

    }

    public static void bestFlights(myLinkedList<myLinkedList<city>> adjacencyGraph, String filePath) {


        try (Scanner fileReader = new Scanner(Paths.get(filePath))) {

            int numberOfFlightsRequested = Integer.parseInt(fileReader.nextLine());

            while(fileReader.hasNextLine()) {

                int totalTime = 0;
                int totalCost = 0;

                
                
                
                String flightRequested = fileReader.nextLine();
                
                String[] flightRequestedData = flightRequested.split("\\|");
                
                myStack<city>[] flights = citySequence(adjacencyGraph, flightRequestedData[0], flightRequestedData[1]);
                



            }




        } catch (Exception error) {
            System.out.println(error);
        }

    }

    public static myStack<city>[] citySequence(myLinkedList<myLinkedList<city>> adjacencyGraph, String source, String dest) {


        myStack<city>[] returnArray = new myStack[3];

        myLinkedList<city> startNode = adjacencyGraph.get(new myLinkedList<city>(new city(source)));

            



        return returnArray;
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

                adjacencyGraph.get(firstSourceCity).addAtTail(new city(flightDataParts[1], Integer.parseInt(flightDataParts[2]), Integer.parseInt(flightDataParts[3])));

                adjacencyGraph.get(secondSoureCity).addAtTail(new city(flightDataParts[0], Integer.parseInt(flightDataParts[2]), Integer.parseInt(flightDataParts[3])));
            }
        } catch (Exception error) {
            System.out.println(error);
        }
    }
}
