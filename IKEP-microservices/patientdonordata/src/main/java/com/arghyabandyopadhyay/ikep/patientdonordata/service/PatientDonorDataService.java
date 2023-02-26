package com.arghyabandyopadhyay.ikep.patientdonordata.service;


import com.arghyabandyopadhyay.ikep.patientdonordata.model.PatientDonorData;
import com.arghyabandyopadhyay.ikep.patientdonordata.util.exception.PatientDonorDataAlreadyExistsException;
import com.arghyabandyopadhyay.ikep.patientdonordata.util.exception.PatientDonorDataNotExistsException;

import java.util.List;
import java.util.UUID;

public interface PatientDonorDataService {

    public PatientDonorData addPatientDonorData(PatientDonorData patientDonorData) throws PatientDonorDataAlreadyExistsException;
    public PatientDonorData getPatientDonorData(UUID id) throws PatientDonorDataNotExistsException;
    public PatientDonorData getPatientDonorDataUsingMobileNumber(String mobileNo) throws PatientDonorDataNotExistsException;
    public void updatePatientDonorData(PatientDonorData patientDonorData)throws PatientDonorDataNotExistsException;
    public void deletePatientDonorData(UUID id)throws PatientDonorDataNotExistsException;
    public void deleteAllPatientDonorData();

    public List<PatientDonorData> getAllPatientDonorData();
}
