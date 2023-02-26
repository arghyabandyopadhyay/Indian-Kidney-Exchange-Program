package com.arghyabandyopadhyay.ikep.ikepservice.service;

import com.arghyabandyopadhyay.ikep.ikepservice.model.Graph;
import com.arghyabandyopadhyay.ikep.ikepservice.model.GraphData;
import com.arghyabandyopadhyay.ikep.ikepservice.model.PatientDonorDataIkep;

import java.util.*;

public class IkepExchangeCycleGeneratorBeta {
    private final double sPrefMax;
    private int maxCycleLength;
    private int edgeFilter;
    private int ageFilter;//D_a
    private double ageReducingCoefficient;//alpha_a
    private int maxAgeScore;//V_a
    private int maxABOScore;//V_b
    private int maxPinScore;//V_p
    private int maxKidneyScore;//V_k
    private int kidneyFilter;//D_k
    private int pincodeFilter;//D_p
    private int pincodeReducingCoeff1;//alpha_p1
    private int pincodeReducingCoeff2;//alpha_p2
    private boolean usingSAS;

    IkepExchangeCycleGeneratorBeta(){
        sPrefMax=32;
        maxCycleLength=3;
        edgeFilter=10;
        ageFilter=40;
        ageReducingCoefficient=0.15;
        maxAgeScore=6;
        maxABOScore=6;
        maxPinScore=6;
        maxKidneyScore=6;
        kidneyFilter=3;
        pincodeFilter=1;
        pincodeReducingCoeff1=2;
        pincodeReducingCoeff2=3;
        usingSAS=true;
    }

    IkepExchangeCycleGeneratorBeta(int sPrefMax, int maxCycleLength, int edgeFilter, int ageFilter, double ageReducingCoefficient, int maxAgeScore, int maxABOScore, int maxPinScore, int maxKidneyScore, int kidneyFilter, int pincodeFilter, int pincodeReducingCoeff1, int pincodeReducingCoeff2, boolean usingSAS){
        this.sPrefMax=sPrefMax;
        this.maxCycleLength=maxCycleLength;
        this.edgeFilter=edgeFilter;
        this.ageFilter=ageFilter;
        this.ageReducingCoefficient=ageReducingCoefficient;
        this.maxAgeScore=maxAgeScore;
        this.maxABOScore=maxABOScore;
        this.maxPinScore=maxPinScore;
        this.maxKidneyScore=maxKidneyScore;
        this.kidneyFilter=kidneyFilter;
        this.pincodeFilter=pincodeFilter;
        this.pincodeReducingCoeff1=pincodeReducingCoeff1;
        this.pincodeReducingCoeff2=pincodeReducingCoeff2;
        this.usingSAS=usingSAS;
    }

    HashMap<String,Integer> bloodCode=new HashMap<>();

    // Array of ABO compatibility.
    boolean[][] pdBloodCompatibility={
            {true,false,false,false,false,false,false,false},
            {true,true,false,false,false,false,false,false},
            {true,false,true,false,false,false,false,false},
            {true,true,true,true,false,false,false,false},
            {true,false,false,false,true,false,false,false},
            {true,true,false,false,true,true,false,false},
            {true,false,true,false,true,false,true,false},
            {true,true,true,true,true,true,true,true},
    };

    public  void init(){
        bloodCode.put("O-",0);
        bloodCode.put("O+",1);
        bloodCode.put("B-",2);
        bloodCode.put("B+",3);
        bloodCode.put("A-",4);
        bloodCode.put("A+",5);
        bloodCode.put("AB-",6);
        bloodCode.put("AB+",7);
    }

    public  List<List<PatientDonorDataIkep>> ikep(PatientDonorDataIkep[] vertices){
        int iteration=0;
        List<List<PatientDonorDataIkep>> C=new ArrayList<>();
        List<List<PatientDonorDataIkep>> CCompliment=new ArrayList<>();
        while (vertices.length!=0){
            //Generates the compatibility matrix for the vertices.
            int[][] w=genCompatibilityMatrix(vertices);
            //Prints the compatibility matrix formed for the particular iteration.
//            println("Starting");
//            printCompatibilityMatrix(w,vertices);
            GraphData graphData=filteredData(w, vertices.length, vertices);
            w=graphData.w;
            vertices=graphData.vertices.toArray(new PatientDonorDataIkep[0]);
//            println("Optimized");
//            printCompatibilityMatrix(w,vertices);
            //Generates the priority queue for the vertices based on the object member "priority"
            Queue<PatientDonorDataIkep> queue=new LinkedList<>(Arrays.asList(vertices));
            //Finds a cycle for the top most vertex in the priority queue.
            List<PatientDonorDataIkep> cycle=getCycle(w,vertices.length,queue,vertices);
            // If there is a cycle for the particular vertex, adds it to the list of cycles.
            if(cycle!=null){
                if(cycle.size()<=maxCycleLength)C.add(cycle);
                else CCompliment.add(cycle);
                //removes the vertices in the cycle from the vertex array and then returns a new array.
                vertices=removeCycleAndGetNewMatrix(cycle,vertices,w);
            }
            else{
                break;
            }
        }
        return C;
    }
    public  int[][] genCompatibilityMatrix(PatientDonorDataIkep[] vertices){
        int[][] w=new int[vertices.length][vertices.length];
        for(int i=0;i<vertices.length;i++){
            Arrays.fill(w[i],0);
        }
        for(int i=0;i<vertices.length;i++){
            for(int j=0;j<vertices.length;j++){
                int bij=aboScore(vertices[j].getBloodGroupOfDonor(),vertices[i].getBloodGroup());
                if(bij==0){
                    w[i][j]=0;
                    continue;
                }
                int hij=hlaScore(vertices[j].getHlaOfDonor(),vertices[i].getHla());
                if(hij==0)continue;
                double aij=ageScore(vertices[j].getAgeOfDonor(),vertices[i].getAge());
                double kij=kidneyScore(vertices[j].getKidneySizeOfDonor(),vertices[i].getKidneySize());
                int pij=pinScore(vertices[j].getPincode(),vertices[i].getPincode());
                double gw=bij+hij+aij+kij+pij;
                if(usingSAS){
                    double sas=sasScore(vertices[i].getSocietalPreference(),vertices[j].getSocietalDistributionOfDonor());
                    w[i][j]=(int)Math.round(gw*sas);
                }
                else w[i][j]=(int)Math.round(gw);
            }
        }
        return w;
    }
    public  List<PatientDonorDataIkep> getCycle(int[][] w, int length, Queue<PatientDonorDataIkep> queue,PatientDonorDataIkep[] vertices) {
        Graph g=generateGraphUsingMaxRowValue(w,length,vertices);
        while(!queue.isEmpty()){
            PatientDonorDataIkep v=queue.poll();
            List<PatientDonorDataIkep> cycle=getDFSCycle(g,v);
            if(cycle!=null)return cycle;
        }
        return null;
    }
    public GraphData filteredData(int[][] w,int length,PatientDonorDataIkep[] vertices){
        while(true){
            int[][] newW;
            List<PatientDonorDataIkep> newVertices=new ArrayList<>();
            List<Integer> rejectedIndex=new ArrayList<>();
            int i=0;
            int ind=0;
            for (int[] ints : w) {
                int max=ints[0];
                for (int j = 1; j < length; j++) {
                    if(ints[j]>max) {
                        max = ints[j];
                    }
                    else if(ints[j]==max){
                        max = ints[j];
                    }
                }
                if(max>=edgeFilter){
                    vertices[i].setIndexVertex(ind++);
                    newVertices.add(vertices[i]);
                }
                else{
                    rejectedIndex.add(i);
                    for (int[] ints1:w) {
                        ints1[i]=-1;
                    }
                    Arrays.fill(w[i],-1);
                }
                i++;
            }
            newW=new int[newVertices.size()][newVertices.size()];
            int r=0;
            int c=0;
            for (int k=0;k<length;k++) {
                if(rejectedIndex.contains(k))continue;
                for (int j = 0; j < length; j++) {
                    if(rejectedIndex.contains(j))continue;
                    newW[r][c++]=w[k][j];
                }
                r++;
                c=0;
            }
            GraphData graphData=new GraphData(newW,newVertices);
            if(isOptimized(newW,newVertices.size())) {
                return graphData;
            }
            else{
                w=newW;
                vertices=newVertices.toArray(new PatientDonorDataIkep[0]);
                length=newVertices.size();
            }
        }
    }
    boolean isOptimized(int[][] w,int size){
        int i=0;
        for (int[] ints : w) {
            int max=ints[0];
            for (int j = 1; j < size; j++) {
                if(ints[j]>max) {
                    max = ints[j];
                }
                else if(ints[j]==max){
                    max = ints[j];
                }
            }
            if(max<edgeFilter)
            {
                return false;
            }
        }
        return true;
    }

    public  Graph generateGraphUsingMaxRowValue(int[][] w,int size,PatientDonorDataIkep[] vertices){
        Graph g=new Graph(size);
        int i=0;
        for (int[] ints : w) {
            int max=ints[0];
            PatientDonorDataIkep max_val=vertices[0];
            for (int j = 1; j < size; j++) {
                if(ints[j]>max) {
                    max = ints[j];
                    max_val=vertices[j];
                }
                else if(ints[j]==max && vertices[j].getPriority()>max_val.getPriority()){
                    max = ints[j];
                    max_val=vertices[j];
                }
            }
            if(max>=edgeFilter)
            {
                g.addEdge(i++,max_val);
            }
            else g.addEdge(i++,null);
        }
        return g;
    }

    public  List<PatientDonorDataIkep> getDFSCycle(Graph graph,PatientDonorDataIkep vertex){
        List<PatientDonorDataIkep> cycle=new ArrayList<>();
        PatientDonorDataIkep start=vertex;
        cycle.add(vertex);
        vertex=graph.getAdj().get(vertex.getIndexVertex()).get(0);
        while (vertex!=null){
            if(start == vertex) {
                return cycle;
            }
//            else if(cycle.size()>=maxCycleLength)return null;
            else if(!cycle.contains(vertex)) {
                cycle.add(vertex);
            }
            else return  cycle.subList(cycle.indexOf(vertex),cycle.size());
            vertex=graph.getAdj().get(vertex.getIndexVertex()).get(0);
        }
        return null;
    }
    public  PatientDonorDataIkep[] removeCycleAndGetNewMatrix(List<PatientDonorDataIkep> cycle,PatientDonorDataIkep[] vertices,int[][] w){
        PatientDonorDataIkep [] p1 =new PatientDonorDataIkep[Math.max(vertices.length - cycle.size(), 0)];
        int vIndex=0;
        if(p1.length>0){
            for (int j = 0; j < w.length; j++) {
                if(cycle.contains(vertices[j]))continue;
                vertices[j].setIndexVertex(vIndex);
                p1[vIndex++]=vertices[j];
            }
        }
        return p1;
    }

    public  int aboScore(String d,String p){
        if(pdBloodCompatibility[bloodCode.get(p)][bloodCode.get(d)])return maxABOScore;
        else return 0;
    }
    public  int hlaScore(List<String> d, List<String> p){
        int n=0;
        HashMap<String,Integer> freq=new HashMap<>();
        for(int i=0;i<6;i++){
            freq.put(d.get(i),1);
        }
        for(int i=0;i<6;i++){
            if(freq.containsKey(p.get(i))&&freq.get(p.get(i))==1)n++;
        }
        return n;
    }
    public  double ageScore(int d, int p){
        int diff=d-p;
        if(diff<0)return maxAgeScore;
        else if(diff<=ageFilter) return maxAgeScore-ageReducingCoefficient*diff;
        else return 0;
    }
    public  double kidneyScore(double d, double p){
        double diff=Math.abs(d-p);
        if(diff<=kidneyFilter) return maxKidneyScore-diff;
        else return 0;
    }
    public  int pinScore(int d, int p){
        if(d==p) return maxPinScore;
        else if(city(d)==city(p)) return maxPinScore-pincodeFilter;
        else if(subZone(d)==subZone(p)) return maxPinScore-pincodeReducingCoeff1*pincodeFilter;
        else if(zone(d)==zone(p)) return maxPinScore-pincodeReducingCoeff2*pincodeFilter;
        else return 0;
    }
    public  int city(int pinCode){
        //returns the first three digits of the pin-code which signify the zone, sub-zone and the sorting district.
        return pinCode/1000;
    }
    public  int subZone(int pinCode){
        //returns the first two digits of the pin-code which signify the zone and the sub-zone.
        return pinCode/10000;
    }
    public  int zone(int pinCode){
        //returns the first digit of the pin-code which signify the zone.
        return pinCode/100000;
    }
    public  double sasScore(List<String> sPref,String sd){
        double n=1;
        if(sPref.size()==1&&sPref.get(0).equals("")){
            return 1;
        }
        else{
            for(String s : sPref){
                if(s.equals(sd)) {
                    return 1/n;
                }
                n++;
            }
            return 1/(sPrefMax+1);
        }
    }

//    public  void printVertices(PatientDonorDataIkep[] vertices){
//        int i=0;
//        for (PatientDonorDataIkep vertex : vertices) {
//            println(vertex.getPriority());
//            println("Name:\t\t" + vertex.getName() + ",\t\t\t\tDonor's Name:\t\t" + vertex.getDName());
//            println("Blood Group\t" + vertex.getB() + ", \t\t\t\tDonor's Blood Group\t" + vertex.getDB());
//            println("Kidney Size\t" + vertex.getK() + ",\t\t\t\tDonor's Kidney Size\t" + vertex.getDK());
//            println("PinCode\t\t" + vertex.getPin() + ",\t\t\t\tDonor's PinCode\t\t" + vertex.getDPin());
//            print("HLA:\t\t");
//            for (String h : vertex.getH()) print(h + " ");
//            print(", \tDonor's HLA\t\t");
//            for (String h : vertex.getDH()) print(h + " ");
//            println();
//            println();
//        }
//        println();
//    }
//    public void printCompatibilityMatrix(int[][] w, PatientDonorDataIkep[] vertices){
//        int i=0;
//        println("Compatibility Matrix:");
//        print("\t");
//        for (int j = 0; j < w.length; j++) {
//            print(vertices[j].getDName()+ "\t");
//        }
//        println();
//        for (int[] ints : w) {
//            print(vertices[i++].getName()+"\t");
//            for (int j = 0; j < w.length; j++) {
//                print(ints[j] + "\t");
//            }
//            println();
//        }
//        println();
//    }

    //Getter and Setter
    public int getEdgeFilter(){
        return this.edgeFilter;
    }
    public int getMaxCycleLength(){
        return this.maxCycleLength;
    }
    public int getAgeFilter(){
        return this.ageFilter;
    }
    public double getAgeReducingCoefficient(){
        return this.ageReducingCoefficient;
    }
    public int getMaxAgeScore(){
        return this.maxAgeScore;
    }
    public int getMaxABOScore(){
        return this.maxABOScore;
    }
    public int getMaxPinScore(){
        return this.maxPinScore;
    }
    public int getKidneyFilter(){
        return this.kidneyFilter;
    }
    public int getPincodeFilter(){
        return this.pincodeFilter;
    }
    public int getPincodeReducingCoeff1(){
        return this.pincodeReducingCoeff1;
    }
    public int getMaxKidneyScore(){
        return this.maxKidneyScore;
    }
    public int getPincodeReducingCoeff2(){
        return this.pincodeReducingCoeff2;
    }

    public void setEdgeFilter(int edgeFilter){
        this.edgeFilter=edgeFilter;
    }
    public void setMaxCycleLength(int maxCycleLength){
        this.maxCycleLength=maxCycleLength;
    }
    public void setAgeFilter(int ageFilter){
        this.ageFilter=ageFilter;
    }
    public void setAgeReducingCoefficient(double ageReducingCoefficient){
        this.ageReducingCoefficient=ageReducingCoefficient;
    }
    public void setMaxAgeScore(int maxAgeScore){
        this.maxAgeScore=maxAgeScore;
    }
    public void setMaxABOScore(int maxABOScore){
        this.maxABOScore=maxABOScore;
    }
    public void setMaxPinScore(int maxPinScore){
        this.maxPinScore=maxPinScore;
    }
    public void setKidneyFilter(int kidneyFilter){
        this.kidneyFilter=kidneyFilter;
    }
    public void setPincodeFilter(int pincodeFilter){
        this.pincodeFilter=pincodeFilter;
    }
    public void setPincodeReducingCoeff1(int pincodeReducingCoeff1){
        this.pincodeReducingCoeff1=pincodeReducingCoeff1;
    }
    public void setMaxKidneyScore(int maxKidneyScore){
        this.maxKidneyScore=maxKidneyScore;
    }
    public void setPincodeReducingCoeff2(int pincodeReducingCoeff2){
        this.pincodeReducingCoeff2=pincodeReducingCoeff2;
    }


    public  void println(String s){
        System.out.println(s);
    }
    public  void println(int i){
        System.out.println(i);
    }
    public  void println(double i){
        System.out.println(i);
    }
    public  void println(char i){
        System.out.println(i);
    }
    public  void println(long i){
        System.out.println(i);
    }
    public  void println(){
        System.out.println();
    }
    public  void print(String s){
        System.out.print(s);
    }
    public  void print(int i){
        System.out.print(i);
    }
    public  void print(double i){
        System.out.print(i);
    }
    public  void print(char i){
        System.out.print(i);
    }
    public  void print(long i){
        System.out.print(i);
    }

}

