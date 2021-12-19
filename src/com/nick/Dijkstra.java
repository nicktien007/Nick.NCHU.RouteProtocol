package com.nick;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Dijkstra {
    private static final GraphTest2.Edge[] GRAPH = {
              //case 1
//            new Graph.Edge("5", "3", 300),
//            new Graph.Edge("5", "4", 150),
//            new Graph.Edge("5", "7", 150),
//            new Graph.Edge("5", "8", 175),
//
//            new Graph.Edge("8", "3", 275),
//            new Graph.Edge("8", "5", 175),
//            new Graph.Edge("8", "7", 200),
//
//            new Graph.Edge("7", "3", 225),
//            new Graph.Edge("7", "4", 25),
//            new Graph.Edge("7", "5", 150),
//            new Graph.Edge("7", "8", 200),
//
//            new Graph.Edge("3", "4", 125),
//            new Graph.Edge("3", "5", 300),
//            new Graph.Edge("3", "7", 225),
//            new Graph.Edge("3", "8", 275),
//
//            new Graph.Edge("4", "3", 125),
//            new Graph.Edge("4", "5", 150),
//            new Graph.Edge("4", "7", 25),

            //case 2
            new GraphTest2.Edge("9", "7", 12),
            new GraphTest2.Edge("9", "13", 1),


            new GraphTest2.Edge("8", "5", 13),
            new GraphTest2.Edge("8", "7", 4),
            new GraphTest2.Edge("8", "13", 3),
            new GraphTest2.Edge("8", "30", 15),

            new GraphTest2.Edge("5", "7", 5),
            new GraphTest2.Edge("5", "8", 13),
            new GraphTest2.Edge("5", "30", 4),

            new GraphTest2.Edge("13", "7", 9),
            new GraphTest2.Edge("13", "8", 3),
            new GraphTest2.Edge("13", "9", 1),

            new GraphTest2.Edge("30", "5", 4),
            new GraphTest2.Edge("30", "8", 15),

            new GraphTest2.Edge("7", "5", 5),
            new GraphTest2.Edge("7", "8", 4),
            new GraphTest2.Edge("7", "9", 12),
            new GraphTest2.Edge("7", "13", 9),
    };
    private static final String START = "9";
    private static final String END = "30";

    public static void main(String[] args) {
        GraphTest2 g = new GraphTest2(GRAPH);
        g.dijkstra(START);
        g.printPath(END);
//        System.out.println("=======");
//        g.printAllPaths();
    }
}


/**
 * ref : https://stackoverflow.com/questions/37566922/dijkstras-algorithm-finding-all-possible-shortest-paths
 */
class GraphTest2 {
    private final Map<String, Vertex>
            graph; // mapping of vertex names to Vertex objects, built from a set of Edges

    /** One edge of the graph (only used by Graph constructor) */
    public static class Edge {
        public final String v1, v2;
        public final int dist;

        public Edge(String v1, String v2, int dist) {
            this.v1 = v1;
            this.v2 = v2;
            this.dist = dist;
        }
    }

    /** One vertex of the graph, complete with mappings to neighbouring vertices */
    public static class Vertex implements Comparable<Vertex> {
        public final String name;
        public int dist = Integer.MAX_VALUE; // MAX_VALUE assumed to be infinity
        public Vertex previous = null;
        public final Map<Vertex, Integer> neighbours = new HashMap<>();

        public Vertex(String name) {
            this.name = name;
        }

        private void printPath() {
            if (this == this.previous) {
                System.out.printf("%s", this.name);
            } else if (this.previous == null) {
                System.out.printf("%s(unreached)", this.name);
            } else {
                this.previous.printPath();
                System.out.printf(" -> %s(%d)", this.name, this.dist);
            }
        }

        public int compareTo(Vertex other) {
            return Integer.compare(dist, other.dist);
        }
    }

    /** Builds a graph from a set of edges */
    public GraphTest2(Edge[] edges) {
        graph = new HashMap<>(edges.length);

        //one pass to find all vertices
        for (Edge e : edges) {
            if (!graph.containsKey(e.v1)) graph.put(e.v1, new Vertex(e.v1));
            if (!graph.containsKey(e.v2)) graph.put(e.v2, new Vertex(e.v2));
        }

        //another pass to set neighbouring vertices
        for (Edge e : edges) {
            graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
            //graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); // also do this for an undirected graph
        }
    }

    /** Runs dijkstra using a specified source vertex */
    public void dijkstra(String startName) {
        if (!graph.containsKey(startName)) {
            System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
            return;
        }
        final Vertex source = graph.get(startName);
        NavigableSet<Vertex> q = new TreeSet<>();

        // set-up vertices
        for (Vertex v : graph.values()) {
            v.previous = v == source ? source : null;
            v.dist = v == source ? 0 : Integer.MAX_VALUE;
            q.add(v);
        }

        dijkstra(q);
    }

    /** Implementation of dijkstra's algorithm using a binary heap. */
    private void dijkstra(final NavigableSet<Vertex> q) {
        Vertex u, v;
        while (!q.isEmpty()) {

            u = q.pollFirst(); // vertex with shortest distance (first iteration will return source)
            if (u.dist == Integer.MAX_VALUE)
                break; // we can ignore u (and any other remaining vertices) since they are unreachable

            //look at distances to each neighbour
            for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet()) {
                v = a.getKey(); //the neighbour in this iteration

                final int alternateDist = u.dist + a.getValue();
                if (alternateDist < v.dist) { // shorter path to neighbour found
                    q.remove(v);
                    v.dist = alternateDist;
                    v.previous = u;
                    q.add(v);
                } else if (alternateDist == v.dist) {
                    // Here I Would do something
                }
            }
        }
    }

    /** Prints a path from the source to the specified vertex */
    public void printPath(String endName) {
        if (!graph.containsKey(endName)) {
            System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
            return;
        }

        graph.get(endName).printPath();
        System.out.println();
    }
    /** Prints the path from the source to every vertex (output order is not guaranteed) */
    public void printAllPaths() {
        for (Vertex v : graph.values()) {
            v.printPath();
            System.out.println();
        }
    }

    public void printAllPaths2() {
        graph.get("e").printPath();
        System.out.println();
    }
}