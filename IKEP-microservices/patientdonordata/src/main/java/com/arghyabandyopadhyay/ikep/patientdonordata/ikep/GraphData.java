package com.arghyabandyopadhyay.ikep.patientdonordata.ikep;

import java.util.List;

public class GraphData {
    int[][] w;
    List<PatientDonorDataIkep> vertices;
    GraphData(int[][] w, List<PatientDonorDataIkep> vertices){
        this.w=w;
        this.vertices=vertices;
    }
}
