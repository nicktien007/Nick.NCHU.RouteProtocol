package com.nick;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OSPFMain {

    private final static Map<String, String> filePaths = Stream.of(new String[][]{
            {"src/com/nick/input/OSPF_in_1.txt", "src/com/nick/output/OSPF_out_1.txt"},
            {"src/com/nick/input/OSPF_in_2.txt", "src/com/nick/output/OSPF_out_2.txt"},
            {"src/com/nick/input/OSPF_in_3.txt", "src/com/nick/output/OSPF_out_3.txt"},
            {"src/com/nick/input/OSPF_in_4.txt", "src/com/nick/output/OSPF_out_4.txt"},
            {"src/com/nick/input/OSPF_in_5.txt", "src/com/nick/output/OSPF_out_5.txt"},
    }).collect(Collectors.toMap(d -> d[0], d -> d[1]));

    public static void main(String[] args)  {

        //走訪並處理每個檔案
        filePaths.forEach((k, v) -> {

            NodesAndSwitchId nodesAndSwitchId = FileUtils3.getNodesAndSwitchId(k);
            List<Node> nodes = nodesAndSwitchId.getNodes();
            List<Integer> neighborIds = nodesAndSwitchId.getNeighborIds();

            Graph g = new Graph();

            //組裝 vertex, e.g:
            // g.addVertex("5", Arrays.asList(
            //        new Vertex("3", 300),
            //        new Vertex("4", 150),
            //        new Vertex("7", 150),
            //        new Vertex("8", 175)));
            nodes.forEach(node -> {
                List<Vertex> vx= new ArrayList<>();
                for (Map.Entry<Integer, Integer> entry : node.getNeighbors().entrySet()) {
                    Integer toId = entry.getKey();
                    Integer cost = entry.getValue();
                    vx.add(new Vertex(toId.toString(), cost));
                }

                g.addVertex(node.getId().toString(),vx);
            });

            final String start = nodesAndSwitchId.getSwitchId().toString();

            final StringBuilder fileContent = new StringBuilder();
            // 走訪start 和 end，取得最短路徑並打印匯出
            neighborIds.forEach(end -> g.getShortestPath(start, end.toString(), fileContent));
//            System.out.println(g.getShortestPath("98", "155"));

            FileUtils3.writeFile(v, fileContent);
        });
    }
}

class NodesAndSwitchId {

    public NodesAndSwitchId(Integer switchId, List<Node> nodes) {
        this.switchId = switchId;
        this.nodes = nodes;
    }

    private final Integer switchId;
    private final List<Node> nodes;


    public List<Integer> getNeighborIds(){
        return this.nodes.stream().map(Node::getId)
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

class Node {
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

class FileUtils3 {

    /**
     * 打印 並寫入StringBuilder
     * @param s
     * @param fileContent
     */
    public static void printAndWriteContent(String s, StringBuilder fileContent){
        System.out.println(s);
        fileContent.append(s).append("\n");
    }

    /**
     * 寫檔
     * @param fileName
     * @param fileContent
     */
    public static void writeFile(String fileName, StringBuilder fileContent){
        try (FileWriter writer = new FileWriter(fileName);
             BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(fileContent.toString());

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        fileContent.delete(0, fileContent.length());
    }

    /**
     * 解析輸入的檔案
     * @param filePath
     * @return Node 和 進行模擬的switch ID
     */
    public static NodesAndSwitchId getNodesAndSwitchId(String filePath)  {

        int swId = 0;
        List<Node> nodes = new ArrayList<>();

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
}

/**
 * 節點
 */
class Vertex implements Comparable<Vertex> {

    private String id;
    private Integer distance;

    public Vertex(String id, Integer distance) {
        super();
        this.id = id;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((distance == null) ? 0 : distance.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (distance == null) {
            if (other.distance != null)
                return false;
        } else if (!distance.equals(other.distance))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Vertex [id=" + id + ", distance=" + distance + "]";
    }

    @Override
    public int compareTo(Vertex o) {
        if (this.distance < o.distance)
            return -1;
        else if (this.distance > o.distance)
            return 1;
        else
            return this.getId().compareTo(o.getId());
    }

}

/**
 * Dijkstra 算法
 */
class Graph {

    private final Map<String, List<Vertex>> vertices;

    public Graph() {
        this.vertices = new HashMap<>();
    }

    public void addVertex(String character, List<Vertex> vertex) {
        this.vertices.put(character, vertex);
    }

    /**
     * 取得 最短路徑
     * @param start
     * @param end
     * @param fileContent
     * @return
     */
    public List<String> getShortestPath(String start, String end, StringBuilder fileContent) {
        final Map<String, Integer> distances = new HashMap<>();
        final Map<String, Vertex> previous = new HashMap<>();
        PriorityQueue<Vertex> nodes = new PriorityQueue<>();

        //處理 start 和 end 一樣的狀況
        if (Objects.equals(start, end)) {
            FileUtils3.printAndWriteContent("ID:" + start + ", next hop:" + start + ", cost:0", fileContent);
        }

        //資料前處理
        for(String vertex : vertices.keySet()) {
            if (Objects.equals(vertex, start)) {
                distances.put(vertex, 0);
                nodes.add(new Vertex(vertex, 0));
            } else {
                distances.put(vertex, Integer.MAX_VALUE);
                nodes.add(new Vertex(vertex, Integer.MAX_VALUE));
            }
            previous.put(vertex, null);
        }

        // Dijkstra 算法
        while (!nodes.isEmpty()) {
            Vertex smallest = nodes.poll();
            if (Objects.equals(smallest.getId(), end)) {
                final List<String> path = new ArrayList<>();

                StringBuilder content = new StringBuilder();
                String lastCost = "";

                while (previous.get(smallest.getId()) != null) {
                    path.add(smallest.getId());

                    //打印它
                    if (smallest.getId().equals(end)) {
                        content.append("ID:").append(smallest.getId()).append(", next hop:");
                        lastCost = smallest.getDistance().toString();
                    }

                    smallest = previous.get(smallest.getId());
                }

                if (!Objects.equals(start, end)) {
                    content.append(path.stream().reduce((x, y) -> y).orElse("---")).append(", cost:").append(lastCost);
                    FileUtils3.printAndWriteContent(content.toString(), fileContent);
                }

                return path;
            }

            if (distances.get(smallest.getId()) == Integer.MAX_VALUE) {
                break;
            }

            for (Vertex neighbor : vertices.get(smallest.getId())) {
                Integer alt = distances.get(smallest.getId()) + neighbor.getDistance();
                if (alt < distances.get(neighbor.getId()))
                {
                    distances.put(neighbor.getId(), alt);
                    previous.put(neighbor.getId(), smallest);

                    for (Vertex n : nodes) {
                        if (Objects.equals(n.getId(), neighbor.getId())) {
                            nodes.remove(n);
                            n.setDistance(alt);
                            nodes.add(n);
                            break;
                        }
                    }
                }
            }
        }

        return new ArrayList<>(distances.keySet());
    }
}