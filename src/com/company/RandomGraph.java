package com.company;

import com.sun.source.tree.WhileLoopTree;

import java.util.*;

public class RandomGraph {
    private static Scanner scanner = new Scanner(System.in);
    private static int maxLimit;

    private static int MAX;
    // Stores the vertices
    private static int []store;
    private static int n;

    // Graph
    private static int [][]graph;

    // define an adjacency list to represent a graph
    private static List<List<Integer>> adjacencyList;
    // Degree of the vertices
    private static int []d ;
    private static int vertices=0;
    private static int edges=0;
    private static int numberOfMembers=0;
    private static int numberOfClique=0;
    private static int numberOfMemberPerClique=0;
    private static int maxEdges=0;
    private static long startTime;
    private static long endTime;

    public static void main(String[] args) {

        System.out.println("1. Generate a random social network, represented as a graph. ");
        System.out.println("2. Generate a social network that has at least N communities and each community having at least M members. ");
        System.out.println("3. List all communities ");
        System.out.println("4. List all communities by using Heuristic algorithm");

        int chosen = scanner.nextInt();

        switch (chosen) {
            case 1:
                System.out.println("Please give total number of member");
                numberOfMembers = scanner.nextInt();// Set a maximum limit size of users (vertices)
                maxEdges = (numberOfMembers*(numberOfMembers-1))/2;
                store = new int[numberOfMembers];
                graph = new int [numberOfMembers][numberOfMembers];
                d = new int[numberOfMembers];
                n=numberOfMembers;
                // Start time of execution
                startTime = System.nanoTime();
                RandomGraph randomGraph = new RandomGraph(numberOfMembers);
                endTime = System.nanoTime();
                System.out.println("the random graph :");
                printGraph(randomGraph);
                System.out.println("-----" + (endTime - startTime) + " nano second !");
                int[][] myEdges = edgePairGraph(randomGraph);
                generateClique(myEdges,numberOfMembers);
                break;
            case 2:
                System.out.println("Please give number of members");
                numberOfMembers = scanner.nextInt();// Set a maximum limit size of users (vertices)
                maxEdges = (numberOfMembers*(numberOfMembers-1))/2;
                while(true) {
                    System.out.println("Please give number of cummunities, or [0] to exit");
                    numberOfClique=scanner.nextInt();
                    if (numberOfClique==0)
                        break;
                    if (numberOfClique>maxEdges)
                        System.out.println("Number of edges could not be greater than maximum possible edge");
                    else
                        break;
                }
                while(true) {
                    System.out.println("Please give number of member per each community, or [0] to exit");
                    numberOfMemberPerClique=scanner.nextInt();
                    if (numberOfMemberPerClique==0)
                        break;
                    if (numberOfClique>MAX)
                        System.out.println("Number of edges could not be greater than maximum member");
                    else
                        break;
                }

                System.out.println("Generating a social network that has at least " + numberOfClique + " communities and ");
                break;
            case 3:
                System.out.println("3. List all communities. ");
                break;
            case 4:
                System.out.println("4. List all communities by using Heuristic algorithm");
                break;
            default:
                System.out.println("Invalid command! program exited...!");
        }
    }
    // define a random instance to generate random values
    Random random = new Random();
    // Creating the constructor
    public RandomGraph(int maxLimit) {
        // Set a maximum limit for the number of vertices
//        this.vertices = random.nextInt(maxLimit) + 1;
        this.vertices = maxLimit;

        // and choose the number of edges less than or equal to the maximum number of possible edges
//        this.edges = random.nextInt(maxEdges(vertices)) + 1;
        this.edges = ((this.vertices) * ((this.vertices) - 1)) / 2;

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
            if (v == w) {
                continue;
            }
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

    // add edges between given vertices
    void addEdge(int v, int w) {
        // Add w to v's adjacency list
        adjacencyList.get(v).add(w);
        // Add v to w's adjacency list if v is not equal to w
        if (v != w) //the condition then to avoid self-loops
            adjacencyList.get(w).add(v);
    }

    public static void printGraph(RandomGraph myGraph) {
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

    public static int[][] edgePairGraph(RandomGraph myGraph) {

        n = myGraph.adjacencyList.size();
        int edges[][] = new int[(n * (n - 1)) / 2][];
//        int edges[][] = new int[0][];
        int r = 0;
        for (int i = 0; i < n; i++) {
            List<Integer> list
                    = myGraph.adjacencyList.get(i);
            if (list.isEmpty()) {
                continue;
            }
            for (int j = 0; j < list.size(); j++) {
                int record=1;
                for (int m = 0; m < r; m++) {
                    if (edges[m][0] == i)
                        if (edges[m][1] == list.get(j)){
                            record=0;
                            m=r;
                        }
                    if (edges[m][0] == list.get(j))
                        if (edges[m][1] == i){
                            record=0;
                            m=r;
                        }
                }
                if (record==1) {
                    edges[r] = new int[2];
                    edges[r][0] = i;
                    edges[r][1] = list.get(j);
                    r++;
                }
            }
        }
        return edges;
    }
////////////////    find clique   ////////////////////////////////////////////////////////////////////////
    public static void generateClique(int[][] edges,int n){

        int size = edges.length;

        for (int i = 0; i < size; i++)
        {
            if (edges[i] != null) {
                graph[edges[i][0]][edges[i][1]] = 1;
                graph[edges[i][1]][edges[i][0]] = 1;
                d[edges[i][0]]++;
                d[edges[i][1]]++;
            }
        }
        for (int k=2;k<edges.length-1;k++) {
            System.out.print("Clique " + k + " vertices : ");
            findCliques(0, 1, k);
            System.out.println();
        }
    }

    // Function to check if the given set of vertices
    // in store array is a clique or not
    static boolean is_clique(int b)
    {
        // Run a loop for all the set of edges
        // for the select vertex
        for (int i = 1; i < b; i++)
        {
            for (int j = i + 1; j < b; j++)

                // If any edge is missing
                if (graph[store[i]][store[j]] == 0)
                    return false;
        }
        return true;
    }

    // Function to print the clique
    static void print(int n)
    {
        for (int i = 1; i < n; i++) {
            System.out.print(store[i] + " ");
        }
    }
    // Function to find all the cliques of size s
    static void findCliques(int i, int l, int s)
    {
        // Check if any vertices from i+1 can be inserted
        for (int j = i + 1; j <= n - (s - l); j++)
            // If the degree of the graph is sufficient
            if (d[j] >= s - 1)
            {
                // Add the vertex to store
                store[l] = j;
                // If the graph is not a clique of size k
                // then it cannot be a clique
                // by adding another edge
                if (is_clique(l + 1)) {
                    // If the length of the clique is
                    // still less than the desired size
                    if (l < s)
                        // Recursion to add vertices
                        findCliques(j, l + 1, s);
                        // Size is met
                    else {
                        print(l + 1);
                        System.out.print(" / ");
                    }
                }
            }
    }
}

