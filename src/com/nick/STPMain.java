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

    public static void main(String[] args)  {

        //case 1
        //testCase1();

        //case 2
        //testCase2();

        //走訪每一個檔案
        filePaths.forEach((k, v) -> {
            SwitchEx1 s = FileUtils1.getSwitch(k);
            List<BDPU> bdpus = FileUtils1.getBDPUs(k);
            final StringBuilder fileContent = new StringBuilder();

            s.sayHello(fileContent);

            BDPUService bdpuService = new BDPUService();
            bdpuService.calcBDPUsCost(bdpus);

            //switch 接收BDPU封包
            bdpus.forEach(b -> s.received(b, fileContent));

            FileUtils1.printAndWriteContent("==========", fileContent);
            FileUtils1.printAndWriteContent(s.getDetail(), fileContent);
            FileUtils1.printAndWriteContent("==========", fileContent);

            FileUtils1.writeFile(v, fileContent);
        });
    }

    public static void testCase1(){
        //case 1
        SwitchEx1 s = new SwitchEx1(50, 32768);

        List<BDPU> bdpus = new LinkedList<>();
        bdpus.add(new BDPU(30, 32768, 18, 4096, 4, 100));
        bdpus.add(new BDPU(1, 32768, 11, 8192, 4, 1000));

        final StringBuilder fileContent = new StringBuilder();
        s.sayHello(fileContent);

        BDPUService service = new BDPUService();
        service.calcBDPUsCost(bdpus);

        bdpus.forEach(b -> s.received(b, fileContent));

        FileUtils1.printAndWriteContent("==========", fileContent);
        FileUtils1.printAndWriteContent(s.getDetail(), fileContent);
        FileUtils1.printAndWriteContent("==========", fileContent);
    }

    public static void testCase2(){
        //case 2
        SwitchEx1 s = new SwitchEx1(50, 32768);

        List<BDPU> bdpus = new LinkedList<>();
        bdpus.add(new BDPU(30, 4096, 30, 4096, 0, 100));
        bdpus.add(new BDPU(1, 32768, 30, 4096, 12, 1000));
        bdpus.add(new BDPU(5, 32768, 4, 32768, 19, 1000));
        bdpus.add(new BDPU(4, 32768, 4, 32768, 0, 1000));
        bdpus.add(new BDPU(30, 4096, 20, 4096, 4, 100));
        bdpus.add(new BDPU(19, 8192, 20, 4096, 4, 1000));
        bdpus.add(new BDPU(18, 8192, 20, 4096, 4, 1000));

        final StringBuilder fileContent = new StringBuilder();
        s.sayHello(fileContent);

        BDPUService service = new BDPUService();
        service.calcBDPUsCost(bdpus);

        bdpus.forEach(b -> s.received(b, fileContent));

        FileUtils1.printAndWriteContent("==========", fileContent);
        FileUtils1.printAndWriteContent(s.getDetail(), fileContent);
        FileUtils1.printAndWriteContent("==========", fileContent);
    }
}

/**
 * 封裝BDPU 邏輯
 */
class BDPUService {
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


    /**
     * 計算BDPU 成本
     * @param bdpus
     */
    public void calcBDPUsCost(List<BDPU> bdpus) {
        for (BDPU b : bdpus){
            b.setCost(calcCost(b));
        }
    }

    private Integer calcCost(BDPU b){
        return this.bandwidthToCostMaps.get(b.getBandWidth());
    }
}

class SwitchEx1 {

    public SwitchEx1(Integer id, Integer priority) {
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

    public void sayHello(StringBuilder fileContent){
        FileUtils1.printAndWriteContent("Hello", fileContent);
    }


    /**
     * 接收BDPU封包
     * @param bdpu
     * @param fileContent
     */
    public void received(BDPU bdpu, StringBuilder fileContent){

        //處理相等的情況
        if (bdpu.getRootPriority().equals(this.rootPriority) && Objects.equals(bdpu.getRootId(), this.rootId)) {

            if (bdpu.getTotalCost() < this.cost) {
                this.rootPortTo = bdpu.getId();
                this.cost = bdpu.getTotalCost();
                FileUtils1.printAndWriteContent("NewCost:" + bdpu.getTotalCost() + ", Hello", fileContent);
            }
            else {
                FileUtils1.printAndWriteContent("Ignore", fileContent);
            }

            return;
        }

        //更高優先權的BDPU封包，更新它
        if (bdpu.getRootPriority() < this.rootPriority
                || (bdpu.getRootPriority().equals(this.rootPriority) && bdpu.getRootId() < this.rootId)) {

            this.rootId = bdpu.getRootId();
            this.rootPriority = bdpu.getRootPriority();
            this.rootPortTo = bdpu.getId();
            this.cost = bdpu.getTotalCost();

            FileUtils1.printAndWriteContent("NewCost:" + bdpu.getTotalCost() + ", Hello", fileContent);

            return;
        }

        FileUtils1.printAndWriteContent("Ignore", fileContent);
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

class BDPU{
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

    public Integer getId() {
        return id;
    }

    public Integer getPriority() {
        return priority;
    }

    public Integer getRootId() {
        return rootId;
    }

    public Integer getRootPriority() {
        return rootPriority;
    }

    public Integer getCostToRoot() {
        return costToRoot;
    }

    public Integer getBandWidth() {
        return bandWidth;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getTotalCost(){
        return this.costToRoot + this.cost;
    }
}

/**
 * 檔案管理
 */
class FileUtils1 {

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

    public static SwitchEx1 getSwitch(String filePath)  {

        SwitchEx1 s = null;
        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            s = new SwitchEx1(Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine()));

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
}