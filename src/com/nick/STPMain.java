package com.nick;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class STPMain {

    private final static String filePath = "src/com/nick/input/STP_in_5.txt";

    public static void main(String[] args)  {

        STPService service = new STPService();
        //case 1
//        Switch s = new Switch(50, 32768);
//        System.out.println(s);
//
//        List<BDPU> bdpus = new LinkedList<>();
//        bdpus.add(new BDPU(30, 32768, 18, 4096, 4, 100));
//        bdpus.add(new BDPU(1, 32768, 11, 8192, 4, 1000));


        //case 2
//        Switch s = new Switch(50, 32768);
//        System.out.println(s);
//
//        List<BDPU> bdpus = new LinkedList<>();
//        bdpus.add(new BDPU(30, 4096, 30, 4096, 0, 100));
//        bdpus.add(new BDPU(1, 32768, 30, 4096, 12, 1000));
//        bdpus.add(new BDPU(5, 32768, 4, 32768, 19, 1000));
//        bdpus.add(new BDPU(4, 32768, 4, 32768, 0, 1000));
//        bdpus.add(new BDPU(30, 4096, 20, 4096, 4, 100));
//        bdpus.add(new BDPU(19, 8192, 20, 4096, 4, 1000));
//        bdpus.add(new BDPU(18, 8192, 20, 4096, 4, 1000));

        Switch s = getSwitch();
        System.out.println(s.getDetail());
        List<BDPU> bdpus = getBDPUs();

        s.sayHello();
        service.calcBDPUCost(bdpus);

        for (BDPU b : bdpus){
            s.received(b);
        }

        System.out.println("==========");
        System.out.println(s.getDetail());
        System.out.println("==========");
    }


    public static Switch getSwitch()  {

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


    public static List<BDPU> getBDPUs()  {

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

    public static class STPService {
        private final Map<Integer, Integer> bandwidthToCostMaps = Stream.of(new Integer[][]{
                {4, 250},
                {10, 100},
                {16, 62},
                {45, 39},
                {100, 19},
                {155, 14},
                {1000, 4},
                {10000, 2},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));


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
            System.out.println("Hello");
        }


        public void received(BDPU bdpu){

            //處理相等的情況
            if (bdpu.rootPriority.equals(this.rootPriority) && Objects.equals(bdpu.rootId, this.rootId)) {

                if (bdpu.getTotalCost() < this.cost) {
                    this.rootPortTo = bdpu.id;
                    this.cost = bdpu.getTotalCost();
                    System.out.println("NewCost:"+bdpu.getTotalCost()+", Hello");
                }
                else {
                    System.out.println("Ignore");
                }

                return;
            }

            if (bdpu.rootPriority < this.rootPriority
            || (bdpu.rootPriority.equals(this.rootPriority) && bdpu.rootId < this.rootId)) {

                this.rootId = bdpu.rootId;
                this.rootPriority = bdpu.rootPriority;
                this.rootPortTo = bdpu.id;
                this.cost = bdpu.getTotalCost();

                System.out.println("NewCost:"+bdpu.getTotalCost()+", Hello");

                return;
            }

            System.out.println("Ignore");
        }

        public String getDetail() {
            return "Switch{" +
                    "id=" + id +
                    ", priority=" + priority +
                    ", rootId=" + rootId +
                    ", rootPriority=" + rootPriority +
                    ", rootPortTo=" + rootPortTo +
                    ", cost=" + cost +
                    '}';
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