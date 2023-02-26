package com.arghyabandyopadhyay.ikep.ikepservice.model;

import java.util.List;

public class GraphData {
    public int[][] w;
    public List<PatientDonorDataIkep> vertices;
    public GraphData(int[][] w, List<PatientDonorDataIkep> vertices){
        this.w=w;
        this.vertices=vertices;
    }
}
