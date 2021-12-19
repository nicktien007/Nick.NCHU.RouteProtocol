package com.nick;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OSPFMain {

    private final static Map<String, String> filePaths = Stream.of(new String[][]{
//            {"src/com/nick/input/OSPF_in_1.txt", "src/com/nick/output/OSPF_out_1.txt"},
            {"src/com/nick/input/OSPF_in_2.txt", "src/com/nick/output/OSPF_out_2.txt"},
//            {"src/com/nick/input/OSPF_in_3.txt", "src/com/nick/output/OSPF_out_3.txt"},
//            {"src/com/nick/input/OSPF_in_4.txt", "src/com/nick/output/OSPF_out_4.txt"},
//            {"src/com/nick/input/OSPF_in_5.txt", "src/com/nick/output/OSPF_out_5.txt"},
    }).collect(Collectors.toMap(d -> d[0], d -> d[1]));
    private static final StringBuilder fileContent = new StringBuilder();

    public static void main(String[] args)  {

        //case 1
//        testCase1();

        //case 2
//        testCase2();

        filePaths.forEach((k, v) -> {
            NodesAndSwitchId nodesAndSwitchId = getNodesAndSwitchId(k);
            Integer swId = nodesAndSwitchId.getSwitchId();
            List<Node> nodes = nodesAndSwitchId.getNodes();
            List<Integer> neighborIds = nodesAndSwitchId.getNeighborIds();
            Node selectNode = nodes.stream().filter(y -> y.id.equals(swId)).findAny().get();


            neighborIds.forEach(pathId ->{
                List<Path> paths = new LinkedList<>();
//                List<Integer> routePath = new LinkedList<>();

                List<List<Integer>> allPath = new LinkedList<>();

                selectNode.neighbors.forEach((nId, nCost) -> {
                    List<Integer> routePath = new LinkedList<>();

                    routePath.add(nId);
                    findRoute(selectNode, pathId, routePath, nodes);

                    allPath.add(routePath);

                    //選擇的node的 鄰居 node
//                    Node selectNodeNeighborNode = nodes.stream().filter(n -> n.id.equals(nId)).findAny().get();
//                    Integer c = selectNodeNeighborNode.neighbors.getOrDefault(pathId,0);

//                    System.out.println("To path id:" + pathId + " by:" + nextNodeNeighbor.id);
//                    System.out.println("cost:" + c + ",cost:" + nCost + ", total = " + (c + nCost));
//                    System.out.println("====");

//                    if (pathId.equals(swId)) {
//                        paths.add(new Path(pathId, pathId, 0));
//                        return;
//                    }
//                    paths.add(new Path(pathId, nId, c + nCost));
                });

//                System.out.println(paths.stream().min(Comparator.comparing(path -> path.cost)).get());

            });


                //剛剛還ok的版本
//            neighborIds.forEach(pathId ->{
//                List<Path> paths = new LinkedList<>();
//                selectNode.neighbors.forEach((nId, nCost) -> {
//
////                    Integer c = selectNode.getNeighbors().get(pathId);
//
//                    //下一個 node
//                    Node nextNodeNeighbor = nodes.stream().filter(n -> n.id.equals(nId)).findAny().get();
//                    Integer c = nextNodeNeighbor.neighbors.getOrDefault(pathId,0);
//
////                    System.out.println("To path id:" + pathId + " by:" + nextNodeNeighbor.id);
////                    System.out.println("cost:" + c + ",cost:" + nCost + ", total = " + (c + nCost));
////                    System.out.println("====");
//
//                    if (pathId.equals(swId)) {
//                        paths.add(new Path(pathId, pathId, 0));
//                        return;
//                    }
//                    paths.add(new Path(pathId, nId, c + nCost));
//                });
//
//                System.out.println(paths.stream().min(Comparator.comparing(path -> path.cost)).get());
//
//            });

//            neighborIds.forEach(id ->{
//                nodes.stream().filter(n -> n.id.equals(id)).findAny().ifPresent(nextNode->{
//
//
////                    nextNode.neighbors.forEach((nId, nCost) -> {
//                    selectNode.neighbors.forEach((nId, nCost) -> {
//
//                        Integer c = selectNode.getNeighbors().get(id);
//
//
//                        //下一個 node
//                        Node nextNodeNeighbor = nodes.stream().filter(n -> n.id.equals(nId)).findAny().get();
//                        System.out.println("path id:"+id+" to:"+nextNodeNeighbor.id);
//                        System.out.println("cost:"+c+",cost:"+nCost);
//                        System.out.println("====");
//                    });
//                });
//            });

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

    private static void findRoute(Node selectNode, Integer toId, List<Integer> routePath, List<Node> nodes){
        Integer lastRouteId = routePath.stream().reduce((f, s) -> s).orElse(0);
        Node lastNode = nodes.stream().filter(x -> x.id.equals(lastRouteId)).findAny().get();


        lastNode.neighbors.forEach((k, v) ->{
            //有找到，加入routhPath
            if (k.equals(toId)) {
                routePath.add(k);
            }
        } );
//        if (!lastNode.neighbors.containsKey(toId)) {
//            findRoute(selectNode,toId,routePath,nodes);
//        }

        lastNode.neighbors.forEach((k, v) ->{
            Node node = nodes.stream().filter(n -> n.id.equals(k)).findAny().get();

        } );
    }

    private static void findRouteUtilToid(Integer toId, List<Integer> routePath, List<Node> nodes){
        Integer lastRouteId = routePath.stream().reduce((f, s) -> s).orElse(0);
        Node lastNode = nodes.stream().filter(x -> x.id.equals(lastRouteId)).findAny().get();


        lastNode.neighbors.forEach((k, v) ->{
            //有找到，加入routhPath
            if (k.equals(toId)) {
                routePath.add(k);
                return;
            }

            Node node = nodes.stream().filter(n -> n.id.equals(k)).findAny().get();
        } );
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

    public static void testCase1(){
        //case 1
        List<Neighbor> neighbors = new LinkedList<>();
        neighbors.add(new Neighbor(10, 10, 100));
        neighbors.add(new Neighbor(12, 12, 25));

        Switch s = new Switch(50, new ArrayList<>(neighbors) , neighbors);

        List<Node> nodes = new LinkedList<>();
        Map<Integer, Integer> neighborForEigrp = new HashMap<>();
        neighborForEigrp.put(5, 50);
        neighborForEigrp.put(10, 200);
        neighborForEigrp.put(14, 50);
        neighborForEigrp.put(50, 25);
        nodes.add(new Node(12, neighborForEigrp));

        s.sayHello();

        nodes.forEach(s::received);

        printAndWriteContent(s.getDetail());
    }

    public static void testCase2(){
        //case 1
        List<Neighbor> neighbors = new LinkedList<>();
        neighbors.add(new Neighbor(2, 2, 150));
        neighbors.add(new Neighbor(15, 15, 275));
        neighbors.add(new Neighbor(12, 12, 250));
        neighbors.add(new Neighbor(9, 9, 175));

        Switch s = new Switch(6, new ArrayList<>(neighbors), neighbors);

        List<Node> nodes = new LinkedList<>();

        Map<Integer, Integer> neighborForEigrp1 = new HashMap<>();
        neighborForEigrp1.put(15, 150);
        neighborForEigrp1.put(6, 150);
        nodes.add(new Node(2, neighborForEigrp1));

        Map<Integer, Integer> neighborForEigrp2 = new HashMap<>();
        neighborForEigrp2.put(12, 25);
        neighborForEigrp2.put(6, 275);
        nodes.add(new Node(15, neighborForEigrp2));

        Map<Integer, Integer> neighborForEigrp3 = new HashMap<>();
        neighborForEigrp3.put(2, 200);
        neighborForEigrp3.put(6, 250);
        nodes.add(new Node(12, neighborForEigrp3));

        Map<Integer, Integer> neighborForEigrp4 = new HashMap<>();
        neighborForEigrp4.put(8, 300);
        neighborForEigrp4.put(2, 275);
        neighborForEigrp4.put(15, 200);
        neighborForEigrp4.put(6, 175);
        nodes.add(new Node(9, neighborForEigrp4));

        s.sayHello();

        nodes.forEach(s::received);

        printAndWriteContent(s.getDetail());
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


    static class Switch {

        public Switch(Integer id, List<Neighbor> initNeighbors, List<Neighbor> neighbors) {
            this.id = id;
            this.neighbors = neighbors;
            this.initNeighbors = initNeighbors;
        }

        private final Integer id;

        /**
         * 鄰居表
         */
        private final List<Neighbor> initNeighbors;
        private final List<Neighbor> neighbors;

        public void sayHello(){
            printAndWriteContent("Hello");
        }


        public void received(Node e){
            updateSwitch(e);
        }

        private void updateSwitch(Node e){
            Map<Integer, Integer> neighbors = e.getNeighbors();
            List<Neighbor> switchNeighbors = this.neighbors;

            Integer receivedEIGRP_Id = e.getId();
            Integer toNeighborCost = this.initNeighbors.stream().filter(s -> s.getId().equals(receivedEIGRP_Id)).findAny().get().getCost();

            final Boolean[] isHello = {false};

            neighbors.forEach((eigrpId, eigrpCost) -> {

                //不在switch 鄰居表，新增它
                if (switchNeighbors.stream().noneMatch(sn -> sn.getId().equals(eigrpId))) {
                    if (!isHello[0]) {
                        isHello[0] = true;
                    }
                    switchNeighbors.add(new Neighbor(eigrpId, receivedEIGRP_Id, eigrpCost + toNeighborCost));
                }


                //找到成本更低的路徑，更新它
                Integer totalEigrpCost = toNeighborCost + eigrpCost;
                switchNeighbors.stream()
                        .filter(sn -> sn.getId().equals(eigrpId)
                                        && sn.getCost() > totalEigrpCost
//                                && initNeighbors.stream().noneMatch(i->i.Id.equals(sn.Id) )
                        )
                        .findAny().ifPresent(sn -> {

                            if (!isHello[0]) {
                                isHello[0] = true;
                            }
                            sn.setCost(totalEigrpCost);
                            sn.setNextHop(receivedEIGRP_Id);
                        });


            });


            final Boolean isUpdate = switchNeighbors.stream().anyMatch(s -> neighbors.containsKey(s.id));

            if (isUpdate && isHello[0]) {
                printAndWriteContent("Update Hello");
            }
            else if(isUpdate){
                printAndWriteContent("Update"); //通知對方更新
            }
            else if(isHello[0]){
                printAndWriteContent("Hello");  //自己要更新，通知鄰居
            }
        }

        public String getDetail() {
            StringBuilder detail = new StringBuilder("Switch ID:" + this.id + "\n");
            detail.append("paths:\n");

            this.neighbors.stream().sorted(Comparator.comparing(Neighbor::getId))
                    .forEach(x -> detail.append("ID:").append(x.getId())
                            .append(", next hop:").append(x.getNextHop())
                            .append(", cost:").append(x.getCost())
                            .append("\n"));

            return detail.toString();
        }
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

    static class Path{
        public Path(Integer toPath, Integer nextHop, Integer cost) {
            this.toPath = toPath;
            this.nextHop = nextHop;
            this.cost = cost;
        }

        private final Integer toPath;
        private final Integer nextHop;
        private final Integer cost;

        @Override
        public String toString() {
            return "ID:" + toPath +
                    ", next hop:" + nextHop +
                    ", cost:" + cost;
        }
    }
}