package com.nick;

import java.util.*;

public class OSPFMain_test1 {

    public static void main(String[] args) {

        //EXAMPLE
        //    1  2  3
        // 1[[0, 1, 0],
        // 2 [1, 1, 0],
        // 3 [0, 0, 1]]
        // Have path (1 to 2),(2 to 1),(2 to 2),(3 to 3)
                        //1  2  3  4  5  6
                        //A  B  C  D  E  F
        int[][] graph = {{0, 1, 0, 1, 0, 0}, //A 1
                         {0, 0, 1, 0, 1, 0}, //B 2
                         {0, 0, 0, 0, 0, 1}, //C 3
                         {0, 0, 0, 0, 1, 0}, //D 4
                         {0, 0, 0, 0, 0, 1}, //E 5
                         {0, 0, 0, 0, 0, 0}  //F 6
        };

                         //1  2  3  4  5  6
                         //A  B  C  D  E  F
        int[][] weight = {{0, 2, 0, 3, 0, 0}, //A 1
                          {0, 0, 1, 0, 4, 0}, //B 2
                          {0, 0, 0, 0, 0, 5}, //C 3
                          {0, 0, 0, 0, 2, 0}, //D 4
                          {0, 0, 0, 0, 0, 1}, //E 5
                          {0, 0, 0, 0, 0, 0}};//F 6

                        //3  1  2  4  5  6
                        //C  A  B  D  E  F
//        int[][] graph = {{0, 0, 0, 0, 0, 1},  //C 3
//                         {0, 1, 0, 1, 0, 0},  //A 1
//                         {1, 0, 0, 0, 1, 0},  //B 2
//                         {0, 0, 0, 0, 1, 0},  //D 4
//                         {0, 0, 0, 0, 0, 1},  //E 5
//                         {0, 0, 0, 0, 0, 0}   //F 6
//        };
//
//                         //3  1  2  4  5  6
//                         //C  A  B  D  E  F
//        int[][] weight = {{0, 0, 0, 0, 0, 5}, //C 3
//                          {0, 2, 0, 3, 0, 0}, //A 1
//                          {1, 0, 0, 0, 4, 0}, //B 2
//                          {0, 0, 0, 0, 2, 0}, //D 4
//                          {0, 0, 0, 0, 0, 1}, //E 5
//                          {0, 0, 0, 0, 0, 0}};//F 6


                        //1  2  3  4  5
//                        //3  4  5  7  8
//        int[][] graph = {{0, 1, 1, 1, 1},  //3 1
//                         {1, 0, 1, 1, 0},  //4 2
//                         {1, 1, 0, 1, 1},  //5 3
//                         {1, 1, 1, 0, 1},  //7 4
//                         {1, 0, 1, 1, 0},  //8 5
//        };
//
//                        // 1     2    3    4    5
//                        // 3     4    5    7    8
//        int[][] weight = {{0,   125, 300, 225, 275},  //3 1
//                          {125, 0,   150, 25,  0},    //4 2
//                          {300, 150, 0,   150, 175},  //5 3
//                          {225, 25,  150, 0,   200},  //7 4
//                          {275, 0,   175, 200, 0},    //8 5
//        };

                        //1  2  3  4  5
                        //3  4  5  7  8
//        int[][] graph = {{0, 1, 1, 1, 1},  //3 1
//                         {0, 0, 1, 1, 0},  //4 2
//                         {0, 0, 0, 1, 1},  //5 3
//                         {0, 0, 0, 0, 1},  //7 4
//                         {0, 0, 0, 0, 0},  //8 5
//        };
//
//                        // 1     2    3    4    5
//                        // 3     4    5    7    8
//        int[][] weight = {{0,   125, 300, 225, 275},  //3 1
//                          {0,   0,   150, 25,  0},    //4 2
//                          {0,   0,   0,   150, 175},  //5 3
//                          {0,   0,   0,   0,   200},  //7 4
//                          {0,   0,   0,   0,   0},    //8 5
//        };

                        //1  2  3  4
                        //11  12  13  14
//        int[][] graph = {{0, 1, 0, 1},  //11 1
//                         {1, 0, 1, 0},   //12 2
//                         {0, 1, 0, 1},   //13 3
//                         {1, 0, 1, 0},   //14 4
//        };
//
//                        //1  2  3  4
//                        //11  12  13  14
//        int[][] weight = {{0, 15, 0, 3},  //11 1
//                         {15, 0,  6, 0},  //12 2
//                         {0,  6,  0, 5},  //13 3
//                         {3,  0,  5, 0},  //14 4
//        };


        graphTest di = new graphTest(graph, weight);

        Integer from = 2;
        Integer to = 6;

//        Integer from = 6;
//        Integer to = 1;

        di.DIJKSTRA();
        //A=1, B=2, C=3, D=4, E=5, F=6
        //3=>1, 4=>2, 5=>3, 7=>4, 8=>5
        ArrayList path = di.getSolutionFrom(from,to);//A=1, F=6
        System.out.println(path);
        System.out.println(di.getCostFrom(from,to));
    }

    public static class graphTest {
        private int[][] G;
        private int[][] W;
        private vertex[] Map;

        public graphTest(int[][] G, int[][] W) {
            this.G = G;
            this.W = W;
            this.Map = new vertex[G[0].length];
            for(int i=0;i<G[0].length;i++){
                Map[i] = new vertex();
            }
        }

        public void DIJKSTRA() {
            INITIALIZE_SINGLE_SOURCE();
            ArrayList<Integer> S = new ArrayList<Integer>();
            ArrayList<Integer> Q = new ArrayList<Integer>();
            Map[0].setShorest(0);
            for (int i = 0; i < G[0].length; i++) {
                Q.add(i);
            }

            while (!Q.isEmpty()) {
                // u is index of Q to remove, Q.get(u) is index of Map
                int u = findMin(Q);
                S.add(Q.get(u));
                int[] con = findConnect2(Q.get(u));
                for (int i=0;i<con.length;i++){
                    RELAX(Q.get(u),con[i],W[Q.get(u)][con[i]]);
                }
                Q.remove(u);
            }
        }

        private void INITIALIZE_SINGLE_SOURCE() {
            for (int i = 0; i < Map.length; i++) {
                Map[i].setShorest(Integer.MAX_VALUE);
                Map[i].setPrevious(-1);
            }
        }

        private void RELAX(int u, int v, int w) {
            if (Map[v].getShorest() > Map[u].getShorest() + w) {
                Map[v].setShorest(Map[u].getShorest() + w);
                Map[v].setPrevious(u);
            }
        }

        private int findMin(ArrayList<Integer> Q) {
            int min = Integer.MAX_VALUE;
            int Min = Integer.MAX_VALUE;
            for (int i = 0; i < Q.size(); i++) {
                if (Map[Q.get(i)].getShorest() < min) {
                    min = Map[Q.get(i)].getShorest();
                    Min = i;
                }
            }
            return Min; // index of Q
        }

        private int[] findConnect2(int u) {
            ArrayList<Integer> con = new ArrayList<Integer>();
            for (int i = 0; i < G[0].length; i++) {
                if (G[u][i] != 0) {
                    con.add(i);
                }
            }
            int[] newCon = new int[con.size()];
            for (int i = 0; i < newCon.length; i++) {newCon[i] = con.get(i);}
            return newCon;// index of Map
        }

        public ArrayList getSolutionFrom(int A, int B){
            ArrayList solution = new ArrayList();
            int s = B-1;
            solution.add(A);
            do{
                solution.add(1,s+1);
                s = Map[s].getPrevious();
            }while(s!=-1 & s!=(A-1));
            return solution;
        }
        public int getCostFrom(int A, int B){
            return Map[B-1].getShorest()-Map[A-1].getShorest();
//            return Map[B-1].getShorest()+Map[A-1].getShorest();
        }
    }

    static class  vertex{
        private int shorest;
        private int previous;

        public void setShorest(int s){
            shorest = s;
        }
        public void setPrevious(int p){
            previous = p;
        }

        public int getShorest(){
            return shorest;
        }
        public int getPrevious(){
            return previous;
        }
    }
}