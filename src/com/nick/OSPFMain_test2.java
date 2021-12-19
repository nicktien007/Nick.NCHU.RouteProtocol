package com.nick;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OSPFMain_test2 {

    private final static Map<String, String> filePaths = Stream.of(new String[][]{
//            {"src/com/nick/input/OSPF_in_1.txt", "src/com/nick/output/OSPF_out_1.txt"},
//            {"src/com/nick/input/OSPF_in_2.txt", "src/com/nick/output/OSPF_out_2.txt"},
            {"src/com/nick/input/OSPF_in_3.txt", "src/com/nick/output/OSPF_out_3.txt"},
//            {"src/com/nick/input/OSPF_in_4.txt", "src/com/nick/output/OSPF_out_4.txt"},
//            {"src/com/nick/input/OSPF_in_5.txt", "src/com/nick/output/OSPF_out_5.txt"},
    }).collect(Collectors.toMap(d -> d[0], d -> d[1]));
    private static final StringBuilder fileContent = new StringBuilder();

//    private static final DijkstraGraph.Edge[] GRAPH = {
//            //case 1
////            new Graph.Edge("5", "3", 300),
////            new Graph.Edge("5", "4", 150),
////            new Graph.Edge("5", "7", 150),
////            new Graph.Edge("5", "8", 175),
////
////            new Graph.Edge("8", "3", 275),
////            new Graph.Edge("8", "5", 175),
////            new Graph.Edge("8", "7", 200),
////
////            new Graph.Edge("7", "3", 225),
////            new Graph.Edge("7", "4", 25),
////            new Graph.Edge("7", "5", 150),
////            new Graph.Edge("7", "8", 200),
////
////            new Graph.Edge("3", "4", 125),
////            new Graph.Edge("3", "5", 300),
////            new Graph.Edge("3", "7", 225),
////            new Graph.Edge("3", "8", 275),
////
////            new Graph.Edge("4", "3", 125),
////            new Graph.Edge("4", "5", 150),
////            new Graph.Edge("4", "7", 25),
//
//            //case 2
//            new DijkstraGraph.Edge("9", "7", 12),
//            new DijkstraGraph.Edge("9", "13", 1),
//
//
//            new DijkstraGraph.Edge("8", "5", 13),
//            new DijkstraGraph.Edge("8", "7", 4),
//            new DijkstraGraph.Edge("8", "13", 3),
//            new DijkstraGraph.Edge("8", "30", 15),
//
//            new DijkstraGraph.Edge("5", "7", 5),
//            new DijkstraGraph.Edge("5", "8", 13),
//            new DijkstraGraph.Edge("5", "30", 4),
//
//            new DijkstraGraph.Edge("13", "7", 9),
//            new DijkstraGraph.Edge("13", "8", 3),
//            new DijkstraGraph.Edge("13", "9", 1),
//
//            new DijkstraGraph.Edge("30", "5", 4),
//            new DijkstraGraph.Edge("30", "8", 15),
//
//            new DijkstraGraph.Edge("7", "5", 5),
//            new DijkstraGraph.Edge("7", "8", 4),
//            new DijkstraGraph.Edge("7", "9", 12),
//            new DijkstraGraph.Edge("7", "13", 9),
//    };
//    private static final String START = "9";
//    private static final String END = "30";

    public static void main(String[] args)  {

        //case 1
//        testCase1();

        //case 2
//        testCase2();

//        final DijkstraGraph.Edge[] GRAPH = {
//                //case 1
////            new Graph.Edge("5", "3", 300),
////            new Graph.Edge("5", "4", 150),
////            new Graph.Edge("5", "7", 150),
////            new Graph.Edge("5", "8", 175),
////
////            new Graph.Edge("8", "3", 275),
////            new Graph.Edge("8", "5", 175),
////            new Graph.Edge("8", "7", 200),
////
////            new Graph.Edge("7", "3", 225),
////            new Graph.Edge("7", "4", 25),
////            new Graph.Edge("7", "5", 150),
////            new Graph.Edge("7", "8", 200),
////
////            new Graph.Edge("3", "4", 125),
////            new Graph.Edge("3", "5", 300),
////            new Graph.Edge("3", "7", 225),
////            new Graph.Edge("3", "8", 275),
////
////            new Graph.Edge("4", "3", 125),
////            new Graph.Edge("4", "5", 150),
////            new Graph.Edge("4", "7", 25),
//
//                //case 2
//                new DijkstraGraph.Edge("9", "7", 12),
//                new DijkstraGraph.Edge("9", "13", 1),
//
//
//                new DijkstraGraph.Edge("8", "5", 13),
//                new DijkstraGraph.Edge("8", "7", 4),
//                new DijkstraGraph.Edge("8", "13", 3),
//                new DijkstraGraph.Edge("8", "30", 15),
//
//                new DijkstraGraph.Edge("5", "7", 5),
//                new DijkstraGraph.Edge("5", "8", 13),
//                new DijkstraGraph.Edge("5", "30", 4),
//
//                new DijkstraGraph.Edge("13", "7", 9),
//                new DijkstraGraph.Edge("13", "8", 3),
//                new DijkstraGraph.Edge("13", "9", 1),
//
//                new DijkstraGraph.Edge("30", "5", 4),
//                new DijkstraGraph.Edge("30", "8", 15),
//
//                new DijkstraGraph.Edge("7", "5", 5),
//                new DijkstraGraph.Edge("7", "8", 4),
//                new DijkstraGraph.Edge("7", "9", 12),
//                new DijkstraGraph.Edge("7", "13", 9),
//        };




//        DijkstraGraph g = new DijkstraGraph(GRAPH);
//        g.dijkstra(START);
//        g.printPath(END);

        filePaths.forEach((k, v) -> {

            NodesAndSwitchId nodesAndSwitchId = getNodesAndSwitchId(k);
            List<Node> nodes = nodesAndSwitchId.getNodes();
            List<Integer> neighborIds = nodesAndSwitchId.getNeighborIds();


            List<DijkstraGraph.Edge> edges = new ArrayList<>();
            nodes.forEach(node -> {
                node.getNeighbors().forEach((toId, cost) -> {
                    edges.add(new DijkstraGraph.Edge(node.getId().toString(), toId.toString(), cost));
                });
            });

            final String start = nodesAndSwitchId.switchId.toString();
            DijkstraGraph dijkstraGraph = new DijkstraGraph(edges);
            dijkstraGraph.dijkstra(start);

            neighborIds.forEach(end -> dijkstraGraph.printPath(end.toString()));

//
//            s.sayHello();
//
//            nodes.forEach(s::received);
//
//            printAndWriteContent(s.getDetail());
//
//            writeFile(v);
        });
    }


    private static void printAndWriteContent(String s){
        System.out.println(s);
        fileContent.append(s).append("\n");
    }

    private static void writeFile(String fileName){
        try (FileWriter writer = new FileWriter(fileName);
             BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(fileContent.toString());

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        fileContent.delete(0, fileContent.length());
    }

    public static NodesAndSwitchId getNodesAndSwitchId(String filePath)  {

        Integer swId = 0;
        List<Node> nodes = new LinkedList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            int nodeNumber = Integer.parseInt(br.readLine());

            for (int i = 0; i < nodeNumber; i++) {
                Integer id = Integer.parseInt(br.readLine());
                int c = Integer.parseInt(br.readLine());

                Map<Integer, Integer> neighbors = new HashMap<>();
                for (int j = 0; j < c; j++) {
                    String line = br.readLine();
                    String[] s1 = line.split(" ");
                    neighbors.put(Integer.parseInt(s1[0]), Integer.parseInt(s1[1]));
                }

                nodes.add(new Node(id, neighbors));
            }

            swId = Integer.parseInt(br.readLine());
            br.close();
        } catch (Exception e) {
            System.err.println("Error: Target File Cannot Be Read");
        }

        return new NodesAndSwitchId(swId, nodes);
    }

    static class Neighbor{

        public Neighbor(Integer id, Integer nextHop, Integer cost) {
            this.id = id;
            this.nextHop = nextHop;
            this.cost = cost;
        }

        private Integer id;

        private Integer nextHop;
        private Integer cost;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getNextHop() {
            return nextHop;
        }

        public void setNextHop(Integer nextHop) {
            this.nextHop = nextHop;
        }

        public Integer getCost() {
            return cost;
        }

        public void setCost(Integer cost) {
            this.cost = cost;
        }
    }

    static class Node {
        public Node(Integer id, Map<Integer, Integer> neighbors) {
            this.id = id;
            this.neighbors = neighbors;
        }

        private final Integer id;

        /**
         * 鄰居表 K:switchID, V:cost
         */
        private final Map<Integer, Integer> neighbors;

        public Integer getId() {
            return id;
        }

        public Map<Integer, Integer> getNeighbors() {
            return neighbors;
        }
    }

    static class NodesAndSwitchId {

        public NodesAndSwitchId(Integer switchId, List<Node> nodes) {
            this.switchId = switchId;
            this.nodes = nodes;
        }

        private final Integer switchId;
        private final List<Node> nodes;


        public List<Integer> getNeighborIds(){
            return this.nodes.stream().map(x -> x.id)
                    .sorted(Comparator.comparing(Integer::new))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        public Integer getSwitchId() {
            return switchId;
        }

        public List<Node> getNodes() {
            return nodes;
        }
    }
}

class DijkstraGraph {
    private final Map<String, Vertex> graph; // mapping of vertex names to Vertex objects, built from a set of Edges

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
    public DijkstraGraph(List<Edge> edges) {
        graph = new HashMap<>(edges.size());

        //one pass to find all vertices
        for (Edge e : edges) {
            if (!graph.containsKey(e.v1)) graph.put(e.v1, new Vertex(e.v1));
            if (!graph.containsKey(e.v2)) graph.put(e.v2, new Vertex(e.v2));
        }

        //another pass to set neighbouring vertices
        for (Edge e : edges) {
            graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
            graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); // also do this for an undirected graph
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