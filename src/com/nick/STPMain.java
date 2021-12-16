package com.nick;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class STPMain {

    private final static Map<String, String> filePaths = Stream.of(new String[][]{
            {"src/com/nick/input/STP_in_1.txt", "src/com/nick/output/STP_out_1.txt"},
            {"src/com/nick/input/STP_in_2.txt", "src/com/nick/output/STP_out_2.txt"},
            {"src/com/nick/input/STP_in_3.txt", "src/com/nick/output/STP_out_3.txt"},
            {"src/com/nick/input/STP_in_4.txt", "src/com/nick/output/STP_out_4.txt"},
            {"src/com/nick/input/STP_in_5.txt", "src/com/nick/output/STP_out_5.txt"},
    }).collect(Collectors.toMap(d -> d[0], d -> d[1]));
    private static final StringBuilder fileContent = new StringBuilder();

    public static void main(String[] args)  {

        //case 1
        //testCase1();

        //case 2
        //testCase2();

        filePaths.forEach((k, v) -> {
            Switch s = getSwitch(k);
            List<BDPU> bdpus = getBDPUs(k);

            s.sayHello();

            BDPUService bdpuService = new BDPUService();
            bdpuService.calcBDPUCost(bdpus);

            bdpus.forEach(s::received);

            printAndWriteContent("==========");
            printAndWriteContent(s.getDetail());
            printAndWriteContent("==========");

            writeFile(v);
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

    public static void testCase1(){
        //case 1
        Switch s = new Switch(50, 32768);

        List<BDPU> bdpus = new LinkedList<>();
        bdpus.add(new BDPU(30, 32768, 18, 4096, 4, 100));
        bdpus.add(new BDPU(1, 32768, 11, 8192, 4, 1000));

        s.sayHello();

        BDPUService service = new BDPUService();
        service.calcBDPUCost(bdpus);

        for (BDPU b : bdpus){
            s.received(b);
        }

        printAndWriteContent("==========");
        printAndWriteContent(s.getDetail());
        printAndWriteContent("==========");
    }

    public static void testCase2(){
        //case 2
        Switch s = new Switch(50, 32768);

        List<BDPU> bdpus = new LinkedList<>();
        bdpus.add(new BDPU(30, 4096, 30, 4096, 0, 100));
        bdpus.add(new BDPU(1, 32768, 30, 4096, 12, 1000));
        bdpus.add(new BDPU(5, 32768, 4, 32768, 19, 1000));
        bdpus.add(new BDPU(4, 32768, 4, 32768, 0, 1000));
        bdpus.add(new BDPU(30, 4096, 20, 4096, 4, 100));
        bdpus.add(new BDPU(19, 8192, 20, 4096, 4, 1000));
        bdpus.add(new BDPU(18, 8192, 20, 4096, 4, 1000));

        s.sayHello();

        BDPUService service = new BDPUService();
        service.calcBDPUCost(bdpus);

        for (BDPU b : bdpus){
            s.received(b);
        }

        printAndWriteContent("==========");
        printAndWriteContent(s.getDetail());
        printAndWriteContent("==========");
    }

    public static Switch getSwitch(String filePath)  {

        Switch s = null;
        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));

             s = new Switch(Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine()));

            br.close();
            return s;
        }
        catch(Exception e){
            System.err.println("Error: Target File Cannot Be Read");
        }

        return s;
    }


    public static List<BDPU> getBDPUs(String filePath)  {

        List<BDPU> bdpus = new LinkedList<>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

            br.readLine();
            br.readLine();

            String n = br.readLine();

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                bdpus.add(new BDPU(Integer.parseInt(line),
                        Integer.parseInt(br.readLine()),
                        Integer.parseInt(br.readLine()),
                        Integer.parseInt(br.readLine()),
                        Integer.parseInt(br.readLine()),
                        Integer.parseInt(br.readLine())));
            }

            br.close();
        }
        catch(Exception e){
            System.err.println("Error: Target File Cannot Be Read");
        }

        return bdpus;
    }

    public static class BDPUService {
        private final Map<Integer, Integer> bandwidthToCostMaps = Stream.of(new Integer[][]{
                {4, 250},
                {10, 100},
                {16, 62},
                {45, 39},
                {100, 19},
                {155, 14},
                {1000, 4},
                {10000, 2},
        }).collect(Collectors.toMap(d -> d[0], d -> d[1]));


        public void calcBDPUCost(List<BDPU> bdpus) {
            for (BDPU b : bdpus){
                b.cost = calcCost(b);
            }
        }

        private Integer calcCost(BDPU b){
            return this.bandwidthToCostMaps.get(b.bandWidth);
        }
    }

    static class Switch {

        public Switch(Integer id, Integer priority) {
            this.id = id;
            this.priority = priority;

            this.rootId = id;
            this.rootPriority = priority;
        }

        private final Integer id;

        private final Integer priority;

        private Integer rootId;

        private Integer rootPriority;

        private Integer rootPortTo;

        private Integer cost;

        public Integer getId() {
            return id;
        }

        public Integer getCost() {
            return cost;
        }

        public void setCost(Integer cost) {
            this.cost = cost;
        }

        public void sayHello(){
            printAndWriteContent("Hello");
        }


        public void received(BDPU bdpu){

            //處理相等的情況
            if (bdpu.rootPriority.equals(this.rootPriority) && Objects.equals(bdpu.rootId, this.rootId)) {

                if (bdpu.getTotalCost() < this.cost) {
                    this.rootPortTo = bdpu.id;
                    this.cost = bdpu.getTotalCost();
                    printAndWriteContent("NewCost:" + bdpu.getTotalCost() + ", Hello");
                }
                else {
                    printAndWriteContent("Ignore");
                }

                return;
            }

            if (bdpu.rootPriority < this.rootPriority
            || (bdpu.rootPriority.equals(this.rootPriority) && bdpu.rootId < this.rootId)) {

                this.rootId = bdpu.rootId;
                this.rootPriority = bdpu.rootPriority;
                this.rootPortTo = bdpu.id;
                this.cost = bdpu.getTotalCost();

                printAndWriteContent("NewCost:"+bdpu.getTotalCost()+", Hello");

                return;
            }

            printAndWriteContent("Ignore");
        }

        public String getDetail() {
            return "ID:" + id + "\n" +
                    "Priority:" + priority + "\n" +
                    "RootID:" + rootId + "\n" +
                    "RootPriority:" + rootPriority + "\n" +
                    "RootPortTo:" + rootPortTo + "\n" +
                    "Cost:" + cost;
        }
    }

    static class BDPU{
        public BDPU(Integer id, Integer priority, Integer rootId, Integer rootPriority, Integer costToRoot, Integer bandWidth) {
            this.id = id;
            this.priority = priority;
            this.rootId = rootId;
            this.rootPriority = rootPriority;
            this.costToRoot = costToRoot;
            this.bandWidth = bandWidth;
        }

        private final Integer id;

        private final Integer priority;

        private final Integer rootId;

        private final Integer rootPriority;

        private final Integer costToRoot;

        private final Integer bandWidth;

        private Integer cost = 0;

        public Integer getTotalCost(){
            return this.costToRoot + this.cost;
        }
    }
}