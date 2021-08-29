package com.company;

import java.util.*;

import user.Clique;
import user.Graph;

import javax.swing.*;

public class Main {

    private static int numberOfMembers=0;
    private static int numberOfFriends=0;

    private static int numberOfClique=0;
    private static int numberOfMemberPerClique=0;

    private static long startTime;
    private static long endTime;
    private static Graph myGraph;

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {

            System.out.println("1. Generate a random social network, represented as a graph.");
            System.out.println("2. Generate a social network that has at least N communities and each community having at least M members. ");
            System.out.println("3. List all communities ");
            System.out.println("4. List all communities by using Heuristic algorithm");

            int chosen = scanner.nextInt();

            switch (chosen) {
                case 1:
                    System.out.print("Please give total number of member : ");
                    numberOfMembers = scanner.nextInt();
                    System.out.println();
                    System.out.print("Please give total number of required friends : ");
                    numberOfFriends = scanner.nextInt();
                    // Start time of execution
                    startTime = System.nanoTime();
                    myGraph=Graph.genRandomNetwork(numberOfMembers, numberOfFriends);
                    endTime = System.nanoTime();
                    System.out.println("-----total process: " + (endTime - startTime) + " ns !");
                    break;
                case 2:
                    System.out.print("Please give number of cliques : ");
                    numberOfClique = scanner.nextInt();// Set a maximum limit size of users (vertices)
                    System.out.println();
                    System.out.print("Please give number user per clique : ");
                    numberOfMemberPerClique = scanner.nextInt();
                    // Start time of execution
                    startTime = System.nanoTime();
                    myGraph=Graph.genNetwork(numberOfClique, numberOfMemberPerClique);
                    endTime = System.nanoTime();
                    System.out.println("-----total process: " + (endTime - startTime) + " nano second !");
                    break;
                case 3:
                    System.out.print("Please give maximum member of each clique : ");
                    numberOfMemberPerClique = scanner.nextInt();
                    System.out.println();
                    System.out.println("Listing all communities from the given Graph..... ");
                    Clique.genClique(myGraph,numberOfMemberPerClique);
                    System.out.println();
                    break;
                case 4:
                    System.out.println("4. List all communities by using Heuristic algorithm");
                    break;
                default:
                    System.out.println("Invalid command! program exited...!");
                    return;
            }
        }
    }
}