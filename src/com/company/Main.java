package com.company;

import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
public class Main {

    public static void main(String[] args) {

        Graph g = new Graph();
        g = new Graph();
        g = Graph.genNetwork(10, 500);
        Graph.bronKerbosch_cliques(g);
    }
}
