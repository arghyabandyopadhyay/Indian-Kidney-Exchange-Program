package com.arghyabandyopadhyay.ikep.ikepservice.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Graph {
    private final List<List<PatientDonorDataIkep>> adj;
    public Graph(int n){
        adj=new ArrayList<>(n);
        for(long i=0;i<n;i++){
            adj.add(new LinkedList<>());
        }
    }
    public void addEdge(int i, PatientDonorDataIkep j){
        adj.get(i).add(j);
    }
    public List<List<PatientDonorDataIkep>> getAdj(){
        return this.adj;
    }
}
