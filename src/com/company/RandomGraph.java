package com.company;

import java.util.*;

public class RandomGraph {
    private static Scanner scanner=new Scanner(System.in);

    public static void main(String[] args)
    {

        System.out.println("1. Generate a random social network, represented as a graph. ");
        System.out.println("2. Generate a social network that has at least N communities and each community having at least M members. ");
        System.out.println("3. List all communities ");
        System.out.println("4. List all communities by using Heuristic algorithm");

        int chosen=scanner.nextInt();

        switch(chosen) {
            case 1:
                System.out.println("Please give number of vertices");
                final int maxLimit = scanner.nextInt();// Set a maximum limit size of users (vertices)
                // Start time of execution
                long startTime = System.nanoTime();
                RandomGraph randomGraph = new RandomGraph(maxLimit);
                // Execution of graph generation ended
                long endTime = System.nanoTime();
                System.out.println("the random graph :");
                printGraph(randomGraph);

                System.out.println("It took " + (endTime - startTime)+" nano second !");
                break;
            case 2:
                System.out.println("2. Generate a social network that has at least N communities and each community having at least M members. ");
                break;
            case 3:
                System.out.println("3. List all communities. ");
                break;
            case 4:
                System.out.println("4. List all communities by using Heuristic algorithm");
                break;
            default:
                System.out.println("Invalid command! System exited...!");
        }
    }

    public int vertices;
    public int edges;

    // define a random instance to generate random values
    Random random = new Random();
    // define an adjacency list to represent a graph
    public List<List<Integer>> adjacencyList;

    // Creating the constructor
    public RandomGraph(int maxLimit)
    {
        // Set a maximum limit for the number of vertices
        this.vertices = random.nextInt(maxLimit) + 1;

        // compute the maximum possible number of edges
        // and randomly choose the number of edges less than
        // or equal to the maximum number of possible edges
        this.edges = random.nextInt(maxEdges(vertices)) + 1;

        // Creating an adjacency list representation for the random graph
        adjacencyList = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++)
            adjacencyList.add(new ArrayList<>());

        // A for loop to randomly generate edges
        for (int i = 0; i < edges; i++) {
            // randomly select two vertices to
            // create an edge between them
            int v = random.nextInt(vertices);
            int w = random.nextInt(vertices);

            // Check if there is already
            // an edge between v and w
            if (adjacencyList.get(v).contains(w)) {
                // Reduce the value of i
                // so that again v and w can be chosen
                // for the same edge count
                i = i - 1;
                continue;
            }
            // Add an edge between them if not previously created
            addEdge(v, w);
        }
    }
    // Method to compute the maximum number of possible edges for a given number of vertices
    int maxEdges(int numOfVertices)
    {
        // there can be at-most V*(V-1)/2 number of edges
        return numOfVertices * ((numOfVertices - 1) / 2);
    }
    // Method to add edges between given vertices
    void addEdge(int v, int w)
    {
        // Add w to v's adjacency list
        adjacencyList.get(v).add(w);
        // Add v to w's adjacency list if v is not equal to w
        if (v != w) //the condition then to avoid self-loops
            adjacencyList.get(w).add(v);
    }
    public static void printGraph(RandomGraph myGraph){
        for (int i = 0; i < myGraph.adjacencyList.size(); i++) {
            System.out.print(i + " -> { ");
            List<Integer> list
                    = myGraph.adjacencyList.get(i);
            if (list.isEmpty())
                System.out.print(" No adjacent vertices ");
            else {
                int size = list.size();
                for (int j = 0; j < size; j++) {
                    System.out.print(list.get(j));
                    if (j < size - 1)
                        System.out.print(" , ");
                }
            }
            System.out.println("}");
        }
    }
}
