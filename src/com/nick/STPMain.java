package com.nick;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class STPMain {

    public static void main(String[] args) {

        STPService service = new STPService();
        //case 1
//        Switch s = new Switch(50, 32768);
//        System.out.println(s);
//
//        List<BDPU> bdpus = new LinkedList<>();
//        bdpus.add(new BDPU(30, 32768, 18, 4096, 4, 100));
//        bdpus.add(new BDPU(1, 32768, 11, 8192, 4, 1000));


        //case 2
        Switch s = new Switch(50, 32768);
        System.out.println(s);

        List<BDPU> bdpus = new LinkedList<>();
        bdpus.add(new BDPU(30, 4096, 30, 4096, 0, 100));
        bdpus.add(new BDPU(1, 32768, 30, 4096, 12, 1000));
        bdpus.add(new BDPU(5, 32768, 4, 32768, 19, 1000));
        bdpus.add(new BDPU(4, 32768, 4, 32768, 0, 1000));
        bdpus.add(new BDPU(30, 4096, 20, 4096, 4, 100));
        bdpus.add(new BDPU(19, 8192, 20, 4096, 4, 1000));
        bdpus.add(new BDPU(18, 8192, 20, 4096, 4, 1000));


        s.sayHello();
        service.calcBDPUCost(bdpus);

        for (BDPU b : bdpus){
            s.received(b);
        }

        System.out.println(s.getDetail());
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

        public Integer getPriority() {
            return priority;
        }

        public Integer getRootId() {
            return rootId;
        }

        public void setRootId(Integer rootId) {
            this.rootId = rootId;
        }

        public Integer getRootPriority() {
            return rootPriority;
        }

        public void setRootPriority(Integer rootPriority) {
            this.rootPriority = rootPriority;
        }

        public Integer getRootPortTo() {
            return rootPortTo;
        }

        public void setRootPortTo(Integer rootPortTo) {
            this.rootPortTo = rootPortTo;
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
            if (bdpu.rootPriority < this.rootPriority
            || (bdpu.rootPriority.equals(this.rootPriority) && bdpu.rootId < this.id)) {
                //update Info

                this.rootId = bdpu.rootId;
                this.rootPriority = bdpu.rootPriority;
                this.rootPortTo = bdpu.id;
                this.cost = bdpu.getTotalCost();

                System.out.println("NewCost:"+bdpu.getTotalCost()+", Hello");


            }else {
                System.out.println("Ignore");
            }
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
