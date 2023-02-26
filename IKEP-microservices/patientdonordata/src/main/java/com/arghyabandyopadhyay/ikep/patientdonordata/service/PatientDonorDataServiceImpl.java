package com.arghyabandyopadhyay.ikep.patientdonordata.service;


import com.arghyabandyopadhyay.ikep.patientdonordata.model.PatientDonorData;
import com.arghyabandyopadhyay.ikep.patientdonordata.repository.PatientDonorDataRepository;
import com.arghyabandyopadhyay.ikep.patientdonordata.util.exception.PatientDonorDataAlreadyExistsException;
import com.arghyabandyopadhyay.ikep.patientdonordata.util.exception.PatientDonorDataNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientDonorDataServiceImpl implements PatientDonorDataService {

    @Autowired
    PatientDonorDataRepository patientDonorDataRepository;
    private final RestTemplate restTemplate;
    @Autowired
    public PatientDonorDataServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public PatientDonorData addPatientDonorData(PatientDonorData patientDonorData) throws PatientDonorDataAlreadyExistsException {
        PatientDonorData added = null;
        try{
            Optional<PatientDonorData> temp=patientDonorDataRepository.findByMobileNumber(patientDonorData.getMobileNumber());
            if(temp.isEmpty())throw new EntityNotFoundException();
            throw new PatientDonorDataAlreadyExistsException();
        }catch(EntityNotFoundException e){
            if(patientDonorData.getSocietalPreference().size()==1&&patientDonorData.getSocietalPreference().get(0).isEmpty())patientDonorData.setSocietalPreference(null);
            added = patientDonorDataRepository.save(patientDonorData);
        }
        return added;
    }

    @Override
    public PatientDonorData getPatientDonorData(UUID id) throws PatientDonorDataNotExistsException {
        Optional<PatientDonorData> optionalPatientDonorData= patientDonorDataRepository.findById(id);
        if(optionalPatientDonorData.isPresent()) return optionalPatientDonorData.get();
        else throw new PatientDonorDataNotExistsException();
    }
    @Override
    public PatientDonorData getPatientDonorDataUsingMobileNumber(String mobileNumber) throws PatientDonorDataNotExistsException {
        Optional<PatientDonorData> optionalPatientDonorData= patientDonorDataRepository.findByMobileNumber(mobileNumber);
        if(optionalPatientDonorData.isPresent()) return optionalPatientDonorData.get();
        else throw new PatientDonorDataNotExistsException();
    }

    @Override
    public void updatePatientDonorData(PatientDonorData patientDonorData) throws PatientDonorDataNotExistsException {
        Optional<PatientDonorData> optionalPatientDonorData= patientDonorDataRepository.findById(patientDonorData.getId());
        if(optionalPatientDonorData.isPresent()) patientDonorDataRepository.save(patientDonorData);
        else throw new PatientDonorDataNotExistsException();
    }

    @Override
    public void deletePatientDonorData(UUID id) throws PatientDonorDataNotExistsException {
        Optional<PatientDonorData> optionalPatientDonorData= patientDonorDataRepository.findById(id);
        if(optionalPatientDonorData.isPresent()) patientDonorDataRepository.delete(optionalPatientDonorData.get());
        else throw new PatientDonorDataNotExistsException();
    }
    @Override
    public void deleteAllPatientDonorData() {
        patientDonorDataRepository.deleteAll();
    }

    @Override
    public List<PatientDonorData> getAllPatientDonorData() {
        return patientDonorDataRepository.findAll();
    }
}
