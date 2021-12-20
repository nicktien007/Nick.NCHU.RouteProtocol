package com.nick;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EIGRPMain {

    private final static Map<String, String> filePaths = Stream.of(new String[][]{
            {"src/com/nick/input/EIGRP_in_1.txt", "src/com/nick/output/EIGRP_out_1.txt"},
            {"src/com/nick/input/EIGRP_in_2.txt", "src/com/nick/output/EIGRP_out_2.txt"},
            {"src/com/nick/input/EIGRP_in_3.txt", "src/com/nick/output/EIGRP_out_3.txt"},
            {"src/com/nick/input/EIGRP_in_4.txt", "src/com/nick/output/EIGRP_out_4.txt"},
            {"src/com/nick/input/EIGRP_in_5.txt", "src/com/nick/output/EIGRP_out_5.txt"},
    }).collect(Collectors.toMap(d -> d[0], d -> d[1]));

    public static void main(String[] args)  {

        //case 1
        //testCase1();

        //case 2
        //testCase2();

        //走訪每一個檔案
        filePaths.forEach((k, v) -> {
            SwitchAndEIGRP switchAndEigrps = FileUtils2.getSwitchAndEIGRPs(k);
            Switch s = switchAndEigrps.getS();
            List<EIGRP> eigrps = switchAndEigrps.getEigrps();

            final StringBuilder fileContent = new StringBuilder();

            s.sayHello(fileContent);

            //switch 接收EIGRP封包
            eigrps.forEach(e -> s.received(e, fileContent));

            FileUtils2.printAndWriteContent(s.getDetail(),fileContent);

            FileUtils2.writeFile(v, fileContent);
        });
    }

    public static void testCase1(){
        //case 1
        List<Neighbor> neighbors = new LinkedList<>();
        neighbors.add(new Neighbor(10, 10, 100));
        neighbors.add(new Neighbor(12, 12, 25));

        Switch s = new Switch(50, new ArrayList<>(neighbors) , neighbors);

        List<EIGRP> eigrps = new LinkedList<>();
        Map<Integer, Integer> neighborForEigrp = new HashMap<>();
        neighborForEigrp.put(5, 50);
        neighborForEigrp.put(10, 200);
        neighborForEigrp.put(14, 50);
        neighborForEigrp.put(50, 25);
        eigrps.add(new EIGRP(12, neighborForEigrp));

        final StringBuilder fileContent = new StringBuilder();

        s.sayHello(fileContent);

        eigrps.forEach(e -> s.received(e, fileContent));

        FileUtils2.printAndWriteContent(s.getDetail(), fileContent);
    }

    public static void testCase2(){
        //case 1
        List<Neighbor> neighbors = new LinkedList<>();
        neighbors.add(new Neighbor(2, 2, 150));
        neighbors.add(new Neighbor(15, 15, 275));
        neighbors.add(new Neighbor(12, 12, 250));
        neighbors.add(new Neighbor(9, 9, 175));

        Switch s = new Switch(6, new ArrayList<>(neighbors), neighbors);

        List<EIGRP> eigrps = new LinkedList<>();

        Map<Integer, Integer> neighborForEigrp1 = new HashMap<>();
        neighborForEigrp1.put(15, 150);
        neighborForEigrp1.put(6, 150);
        eigrps.add(new EIGRP(2, neighborForEigrp1));

        Map<Integer, Integer> neighborForEigrp2 = new HashMap<>();
        neighborForEigrp2.put(12, 25);
        neighborForEigrp2.put(6, 275);
        eigrps.add(new EIGRP(15, neighborForEigrp2));

        Map<Integer, Integer> neighborForEigrp3 = new HashMap<>();
        neighborForEigrp3.put(2, 200);
        neighborForEigrp3.put(6, 250);
        eigrps.add(new EIGRP(12, neighborForEigrp3));

        Map<Integer, Integer> neighborForEigrp4 = new HashMap<>();
        neighborForEigrp4.put(8, 300);
        neighborForEigrp4.put(2, 275);
        neighborForEigrp4.put(15, 200);
        neighborForEigrp4.put(6, 175);
        eigrps.add(new EIGRP(9, neighborForEigrp4));

        final StringBuilder fileContent = new StringBuilder();

        s.sayHello(fileContent);

        eigrps.forEach(e -> s.received(e, fileContent));

        FileUtils2.printAndWriteContent(s.getDetail(), fileContent);
    }
}

/**
 * 檔案管理
 */
class FileUtils2 {

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
    public static SwitchAndEIGRP getSwitchAndEIGRPs(String filePath)  {

        Integer swId = 0;
        List<Neighbor> neighbors = new LinkedList<>();
        List<EIGRP> eigrps = new LinkedList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            swId = Integer.parseInt(br.readLine());
            int neighborNumber = Integer.parseInt(br.readLine());

            for (int i = 0; i < neighborNumber; i++) {

                String line = br.readLine();
                String[] s1 = line.split(" ");

                Integer nId = Integer.parseInt(s1[0]);
                Integer cost = Integer.parseInt(s1[1]);
                neighbors.add(new Neighbor(nId, nId, cost));
            }

            neighbors.add(new Neighbor(swId, swId, 0));


            int eigrpNumber = Integer.parseInt(br.readLine());

            for (int i = 0; i < eigrpNumber; i++) {
                Integer id = Integer.parseInt(br.readLine());
                int c = Integer.parseInt(br.readLine());

                Map<Integer, Integer> neighborForEigrp = new HashMap<>();
                for (int j = 0; j < c; j++) {
                    String line = br.readLine();
                    String[] s1 = line.split(" ");
                    neighborForEigrp.put(Integer.parseInt(s1[0]), Integer.parseInt(s1[1]));
                }

                eigrps.add(new EIGRP(id, neighborForEigrp));
            }

            br.close();
        } catch (Exception e) {
            System.err.println("Error: Target File Cannot Be Read");
        }

        return new SwitchAndEIGRP(new Switch(swId, new ArrayList<>(neighbors), neighbors), eigrps);
    }
}

/**
 * Switch
 */
class Switch {

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

    public void sayHello(StringBuilder fileContent){
        FileUtils2.printAndWriteContent("Hello", fileContent);
    }


    /**
     * 接收EIGRP封包
     * @param e
     * @param fileContent
     */
    public void received(EIGRP e, StringBuilder fileContent){
        updateSwitch(e ,fileContent);
    }

    private void updateSwitch(EIGRP e, StringBuilder fileContent){
        Map<Integer, Integer> neighbors = e.getNeighbors();
        List<Neighbor> switchNeighbors = this.neighbors;

        Integer receivedEIGRP_Id = e.getId();
        Integer toNeighborCost = this.initNeighbors.stream().filter(s -> s.getId().equals(receivedEIGRP_Id)).findAny().get().getCost();

        //紀錄是否需要 hello
        final Boolean[] isHello = {false};

        //走訪EIGRP 鄰居表
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
                    )
                    .findAny().ifPresent(sn -> {

                        if (!isHello[0]) {
                            isHello[0] = true;
                        }
                        sn.setCost(totalEigrpCost);
                        sn.setNextHop(receivedEIGRP_Id);
                    });
        });

        //是否需要 update
        final Boolean isUpdate = switchNeighbors.stream().anyMatch(s -> neighbors.containsKey(s.getId()));

        printHelloAndUpdate(fileContent, isHello[0], isUpdate);
    }

    private void printHelloAndUpdate(StringBuilder fileContent, Boolean isHello, Boolean isUpdate) {
        if (isUpdate && isHello) {
            FileUtils2.printAndWriteContent("Update Hello", fileContent);
        }
        else if(isUpdate){
            FileUtils2.printAndWriteContent("Update", fileContent); //通知對方更新
        }
        else if(isHello){
            FileUtils2.printAndWriteContent("Hello", fileContent);  //自己要更新，通知鄰居
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

class Neighbor{

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

class EIGRP {
    public EIGRP(Integer id, Map<Integer, Integer> neighbors) {
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

class SwitchAndEIGRP {
    public SwitchAndEIGRP(Switch s, List<EIGRP> eigrps) {
        this.s = s;
        this.eigrps = eigrps;
    }

    private final Switch s;
    private final List<EIGRP> eigrps;

    public Switch getS() {
        return s;
    }

    public List<EIGRP> getEigrps() {
        return eigrps;
    }
}