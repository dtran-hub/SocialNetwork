package com.company;
import java.lang.reflect.Array;
import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;
import java.io.*;


public class Graph {
    // adjacency list of edges
    private List<List<Edge>> edges;

    // vector of vertices in
    private List<Vertex> vertices;

    private static Random rand = new Random();

    public Graph() {
        edges = new ArrayList<>();
        vertices = new ArrayList<>();
    }

    public void addVertex(Vertex v) {
        vertices.add(v);
    }

    public void addEdge(Vertex from, Vertex to, double weight) {
        for (Edge e : edges.get(from.id())) {
            if (e.from() == from && e.to() == to || e.from() == to && e.to() == from) {
                return; // already has an edge, return
            }
        }

        edges.get(from.id()).add(new Edge(from, to, weight));
        edges.get(to.id()).add(new Edge(to, from, weight));
    }

    public void printGraph() {
        for (List<Edge> vList : edges) {
            if (vList.size() > 0) {
                System.out.print("" + vList.get(0).from().id());
                for (Edge edge : vList) {
                    System.out.print(String.format(" (%d, %d, %.0f)", edge.from().id(), edge.to().id(), edge.weight()));
                }
                System.out.println();
            }
        }
    }

    public static Graph genRandomNetwork(int nUsers, int nFriends) {
        nFriends = Math.min(nUsers - 1, nFriends);

        System.out.println(String.format("genRandomNetwork(nUsers = %d, nFriends = %d)", nUsers, nFriends));
        long curTime = Instant.now().toEpochMilli();
        Graph g = new Graph();

        // step 1: create vertices (users)
        IntStream.range(0, nUsers).forEach(i -> {
            g.addVertex(new Vertex(i));
            g.edges.add(new ArrayList<>());
        });
        System.out.println("  -> Create vertices takes: " + (Instant.now().toEpochMilli() - curTime) + " ms");

        // step 2: add edges (friends)
        curTime = Instant.now().toEpochMilli();
        for (Vertex v : g.vertices) {
            IntStream.range(0, nFriends).forEach(i -> {
                int idx = rand.nextInt(nUsers);
                while (idx == v.id()) {
                    idx = rand.nextInt(nUsers);
                }
                g.addEdge(v, g.vertices.get(idx), 1.0);
            });
        }
        System.out.println("  -> Add edges takes: " + (Instant.now().toEpochMilli() - curTime) + " ms");
        return g;
    }

    public static Graph genNetwork(int nCommunity, int nMember) {
        System.out.println(String.format("createGraph(nCommunity = %d, nMember = %d)", nCommunity, nMember));
        long curTime = Instant.now().toEpochMilli();
        Graph g = new Graph();

        // step 1: add mandatory requirement (n communities, each has m members)
        IntStream.range(0, nCommunity).forEach(i -> {
            IntStream.range(0, nMember).forEach(j -> {
                Vertex v = new Vertex(i * nMember + j);
                g.addVertex(v);
                g.edges.add(new ArrayList<>());
            });

            for (int t = 0; t < nMember; t++) {
                for (int t2 = 0; t2 < nMember; t2++) {
                    if (t != t2) {
                        g.addEdge(g.vertices.get(i * nMember + t), g.vertices.get(i * nMember + t2), 1.0);
                    }
                }
            }
        });
        System.out.println("  -> Add links takes: " + (Instant.now().toEpochMilli() - curTime) + " ms");

        // step 2: add random links between those community
        curTime = Instant.now().toEpochMilli();
        IntStream.range(0, nCommunity).forEach(i -> {
            g.addEdge(g.vertices.get(rand.nextInt(i * nMember + nMember)),
                    g.vertices.get(rand.nextInt(nCommunity * nMember)), 1.0);
        });

        System.out.println("  -> Add more links takes: " + (Instant.now().toEpochMilli() - curTime) + " ms");
        return g;
    }
    /*
    ---------
    Greedy Algorithm to check cliques
    ---------
    */
    public static List<List<Integer>> subset_graph(Graph graph) throws IOException {
        //Convert vertices list to array
        int verticesID[] = new int[graph.vertices.size()];
        int i = 0;
        for (Vertex v : graph.vertices) {
            verticesID[i] = v.id();
            i += 1;
        }
        //Extract subset of vertices array and write to file
        String subset_filename = "subset.txt";
        FileWriter fileWriter = new FileWriter(subset_filename);
        for (int j = 2; j < i + 1; j++) {
            int data[]=new int[j];
            graph.combinationUtil(verticesID, data, 0, i - 1, 0, j, fileWriter);
        }
        fileWriter.close();

        List<List<Integer>> subset = new ArrayList<>();
        List<Integer> combination = new ArrayList<>();
        String str_node = "";
        FileReader fileReader = new FileReader(subset_filename);
        int ch = fileReader.read();
        while(ch != -1) {
            //System.out.print((char)ch);
            if ((char)ch >=48 && (char)ch <= 57){
                str_node += (char)ch;
            }
            else if ((char)ch == ' '){
                combination.add(Integer.parseInt(str_node));
                str_node = "";
            }
            else if ((char)ch == '\n'){
                List<Integer> addItem = new ArrayList<>(combination);
                subset.add(addItem);
                combination.clear();
            }
            ch = fileReader.read();
        }

        //Print for debug
//		for (List<Integer> combList: subset) {
//			System.out.println(combList);
//		}
        return subset;
    }

    public  void combinationUtil(int arr[], int data[], int start, int end, int index, int r, FileWriter fileWriter) throws IOException {
        // Current combination is ready to be printed, print it
        if (index == r)
        {
            for (int j=0; j<r; j++) {
                //System.out.print(data[j] + " ");
                fileWriter.write(data[j] + " ");
            }
            fileWriter.write("\n");
        }
        else {
            // replace index with all possible elements. The condition
            // "end-i+1 >= r-index" makes sure that including one element
            // at index will make a combination with remaining elements
            // at remaining positions
            for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
                data[index] = arr[i];
                combinationUtil(arr, data, i + 1, end, index + 1, r, fileWriter);
            }
        }
    }

    public static boolean is_clique(Graph graph, List<Integer> combination){
        Boolean result = true;
        for (Integer node: combination) {
            for (Integer adjaacent: combination) {
                if (node != adjaacent) {
                    Vertex from = new Vertex(node);
                    Vertex to = new Vertex(adjaacent);
                    Edge edge = new Edge(from, to, 1.0);
                    //System.out.println(String.format(" (%d, %d, %.0f)", edge.from().id(), edge.to().id(), edge.weight()));
                    //Check if edge exist at node "from" in graph
                    List<Edge> edgeList = new ArrayList<>(graph.edges.get(from.id()));
                    Boolean edge_exist = false;
                    for (Edge e: edgeList) {
                        //System.out.println(String.format(" (%d, %d, %.0f)", e.from().id(), e.to().id(), e.weight()));
                        if (e.to().id() == edge.to().id()) {
                            edge_exist = true;
                            break;
                        }
                    }

                    if (!edge_exist) {
                        result = false;
                        return result;
                    }
                }
            }
        }

        return result;
    }

    public static void greedy_cliques(Graph graph) throws IOException {
        long curTime = Instant.now().toEpochMilli();
        List<List<Integer>> subset = new ArrayList<>(subset_graph(graph));
        List<List<Integer>> cliques = new ArrayList<>();
        //System.out.println(subset);
        for (List<Integer> combination: subset) {
            //Check if combination is a clique
            if (is_clique(graph, combination)){
                List<Integer> addItem = new ArrayList<>(combination);
                cliques.add(addItem);
            }
        }
        int countGreedyclique = 0;
        if (cliques.size() > 0) {
            System.out.println("Cliques:");
            for (List<Integer> combList: cliques) {
                if (combList.size() >= 3) {
                    countGreedyclique++;
                    //System.out.println(combList.size());
                    System.out.println(combList);
                }
            }
        }
        else{
            System.out.println("No Clique found.");
        }
        System.out.println("  -> Number of community found: " + countGreedyclique);
        System.out.println("  -> greedy_cliques takes: " + (Instant.now().toEpochMilli() - curTime) + " ms");
    }

    /*
    ---------
    Bron-Kerbosch Algorithm to check cliques
    ---------
    */
    public static Boolean vertexExists(List<Vertex> vertices_list, Vertex vertex){
        Boolean exist_flag = false;
        for (Vertex v: vertices_list) {
            if (v.id() == vertex.id()){
                exist_flag = true;
                break;
            }
        }
        return exist_flag;
    }
    public static Boolean edgeExists(List<Edge> edges_list, Edge edge){
        Boolean exist_flag = false;
        for (Edge e: edges_list) {
            if ((e.from().id() == edge.from().id())&&(e.to().id() == edge.to().id())){
                exist_flag = true;
                break;
            }
        }
        return exist_flag;
    }
    public static List<Vertex> set_union(List<Vertex> set_a, List<Vertex> set_b){
        List<Vertex> c = new ArrayList<>();
        for (Vertex v: set_a) {
            if (!vertexExists(c, v)){
                c.add(v);
            }
        }
        for (Vertex v: set_b) {
            if (!vertexExists(c, v)){
                c.add(v);
            }
        }
        return c;
    }

    public static List<Vertex> set_intersection(List<Vertex> set_a, List<Vertex> set_b){
        List<Vertex> c = new ArrayList<>();
        if (set_a.size() < set_b.size()) {
            for (Vertex va : set_a) {
                if (vertexExists(set_b, va)) {
                    c.add(va);
                }
            }
        }
        else {
            for (Vertex vb: set_b) {
                if (vertexExists(set_a, vb)){
                    c.add(vb);
                }
            }
        }
        return c;
    }

    public static List<Vertex> set_difference(List<Vertex> set_a, List<Vertex> set_b){
        List<Vertex> c = new ArrayList<>();

        for (Vertex va: set_a) {
            if (!vertexExists(set_b, va)) {
                c.add(va);
            }
        }
        return c;
    }

    public static List<Vertex> neighbors(Graph graph, Vertex vertex){
        List<Vertex> result = new ArrayList<>();
        List<Edge> neighbors_edges = new ArrayList<>();

        //neighbors_edges.addAll(graph.edges.get(v.id()));
        for (Edge e: graph.edges.get(vertex.id())) {
            if (e.to().id() != vertex.id()){
                neighbors_edges.add(e);
            }
        }
//		Boolean vfound = false;
//		for (List<Edge> eList: graph.edges) {
//			for (Edge e: eList) {
//				if (e.from().id() == vertex.id()){
//					neighbors_edges.add(e);
//					vfound = true;
//				}
//				else {
//					break;
//				}
//				if (vfound == true){
//					break;
//				}
//			}
//		}
        for (Edge e: neighbors_edges) {
            Vertex neighbor = e.to();
            result.add(neighbor);
        }
        return result;
    }

    public static void print_clique(List<Vertex> R) {
        for (Vertex v: R) {
            System.out.print(v.id() + " ");
        }
        System.out.println();
    }
    public static void bronKerbosch1(Graph graph, List<Vertex> R, List<Vertex> P, List<Vertex> X){

        if (P.isEmpty() && X.isEmpty()) {
            if (R.size() >= 3) {
                System.out.print("Clique found: ");
                print_clique(R);
                //countBronKerboschcliques++;
            }
        }

        for (Vertex v: P) {
            List<Vertex> singleton = new ArrayList<>();
            singleton.add(v);
            List<Vertex> v_neighbors = new ArrayList<>(neighbors(graph, v));

            bronKerbosch1(graph, set_union(R, singleton), set_intersection(P, v_neighbors), set_intersection(X, v_neighbors));

            P = set_difference(P, singleton);
            X = set_union(X, singleton);

        }
    }
    static int countBronKerboschcliques = 0;
    public static void bronKerbosch(Graph graph, List<Vertex> R, List<Vertex> P, List<Vertex> X){

        if (P.isEmpty() && X.isEmpty()) {
            if (R.size() >= 3) {
                System.out.print("Clique found: ");
                print_clique(R);
                countBronKerboschcliques++;
            }
        }

        //Vertex v = new Vertex(-1);
        Vertex v = P.get(-1);
        if (!P.isEmpty()) {
            v = P.get(0);
        }

        while (!P.isEmpty() && (v.id() != P.get(P.size() - 1).id())) {
            System.out.println("Print something");
            List<Vertex> singleton = new ArrayList<>();
            singleton.add(v);
            List<Vertex> v_neighbors = new ArrayList<>(neighbors(graph, v));

            bronKerbosch(graph, set_union(R, singleton), set_intersection(P, v_neighbors), set_intersection(X, v_neighbors));

            P = set_difference(P, singleton);
            X = set_union(X, singleton);

            if (!P.isEmpty()) {
                v = P.get(0);
            }
        }
    }

    public static void bronKerbosch_cliques(Graph graph) {
        List<Vertex> P = new ArrayList<>(graph.vertices);
        List<Vertex> R = new ArrayList<>();
        List<Vertex> X = new ArrayList<>();
        long curTime = Instant.now().toEpochMilli();
        bronKerbosch1(graph, R, P, X);
        bronKerbosch(graph, R, P, X);
        System.out.println("  -> Number of BronKerbosch community: " + countBronKerboschcliques);
        System.out.println("  -> bronKerbosch_cliques takes: " + (Instant.now().toEpochMilli() - curTime) + " ms");
    }
    /*
    ---------
    Random Walk Algorithm to check cliques
    ---------
    */
    public static void randomWalk_cliques(Graph graph){

    }
}
