package user;

import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.List;

public class Clique {

    private static int MAX;

    // Stores the vertices

    private static int []store;
    private static int n;
    private static int m;

    // Graph
    private static int[][] myEdges;
    private static List<List<Integer>> tmpAdjacencyList;
    private static List<List<Integer>> tmpCliqueList;

    private static int [][]graph;
    // Degree of the vertices
    private static int []d;

    private static List<List<Edge>> edges;

    // vector of vertices in
    private static List<Vertex> vertex;

    private static int count;

    public static List<List<Integer>> getTmpCliqueList() {
        return tmpCliqueList;
    }

    public static void genClique (Graph myGraph, int k)
        {
            edges=myGraph.getEdges();
            vertex=myGraph.getVertices();

            MAX = vertex.size() * (vertex.size()-1);
            store = new int[MAX];
            graph = new int [MAX][MAX];
            d = new int[MAX];
            myEdges = new int[MAX][];
            tmpAdjacencyList = new ArrayList<>(vertex.size());
            for (int i = 0; i < vertex.size(); i++)
                tmpAdjacencyList.add(new ArrayList<>());

            int noPair=0;
            for (int i = 0; i < vertex.size(); i++)
                for (int j = 0; j < edges.get(i).size(); j++) {
                    if (tmpAdjacencyList.get(j).contains(edges.get(i).get(j).from().id()))
                        continue;
                    tmpAdjacencyList.get(i).add(edges.get(i).get(j).to().id());
                    myEdges[noPair] = new int[2];
                    myEdges[noPair][0] = i;
                    myEdges[noPair][1] = edges.get(i).get(j).to().id();
                    noPair++;
                }

        int size = myEdges.length;
        n = vertex.size();

        tmpCliqueList=new ArrayList<>(k);
        for (int i=0;i<=k;i++)
            tmpCliqueList.add(new ArrayList<>());

        for (int i = 0; i < size; i++)
        {
            if (myEdges[i] == null)
                continue;
            graph[myEdges[i][0]][myEdges[i][1]] = 1;
            graph[myEdges[i][1]][myEdges[i][0]] = 1;
            d[myEdges[i][0]]++;
            d[myEdges[i][1]]++;
        }
        for (m=3;m<=k;m++) {
            findCliques(0, 1, m);
        }
            for (int i=0;i<=k;i++){
                if (tmpCliqueList.get(i).size()>0) {
                    System.out.print("Cliques " + i + " members (" + tmpCliqueList.get(i).size()/i+") : ");
                    for (int j = 0; j < tmpCliqueList.get(i).size(); j=j+i) {
                        for (int l = j; l < j + i; l++)
//                            System.out.print(tmpCliqueList.get(i).get(l) + " ");
                            continue;
//                        System.out.print(" / ");
                    }
                    System.out.println();
                }
            }
    }
    static boolean is_clique(int b)
    {
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
    static void addToCliqueArray(int n,int m)
    {
        for (int i = 1; i < n; i++) {
            tmpCliqueList.get(m).add(store[i]);
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
                if (is_clique(l + 1))
                    if (l < s)
                        // Recursion to add vertices
                        findCliques(j, l + 1, s);
                    else {
                        addToCliqueArray(l + 1, m);
                    }
            }
    }
}
