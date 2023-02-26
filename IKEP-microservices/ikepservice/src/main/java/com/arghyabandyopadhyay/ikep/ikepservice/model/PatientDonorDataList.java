package com.arghyabandyopadhyay.ikep.ikepservice.model;

import java.util.ArrayList;
import java.util.List;

public class PatientDonorDataList {
    private List<PatientDonorData> patientDonorData;

    public List<PatientDonorData> getPatientDonors() {
        return patientDonorData;
    }

    public void setPatientDonors(List<PatientDonorData> patientDonorData) {
        this.patientDonorData = patientDonorData;
    }

    public PatientDonorDataList() {
        patientDonorData = new ArrayList<>();
    }
    // standard constructor and getter/setter
}
