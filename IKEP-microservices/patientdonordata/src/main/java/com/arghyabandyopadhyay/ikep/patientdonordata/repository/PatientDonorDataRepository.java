package com.arghyabandyopadhyay.ikep.patientdonordata.repository;

import com.arghyabandyopadhyay.ikep.patientdonordata.model.PatientDonorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientDonorDataRepository extends JpaRepository<PatientDonorData, UUID> {
    Optional<PatientDonorData> findByMobileNumber(String mobileNumber);
}