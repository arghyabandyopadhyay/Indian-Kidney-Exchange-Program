package com.arghyabandyopadhyay.ikep.ikepservice.service;

import com.arghyabandyopadhyay.ikep.ikepservice.model.PatientDonorDataIkep;

import java.util.List;

public interface IkepExchangeCycleGeneratorService {
    public List<List<PatientDonorDataIkep>> getExchangeCycles();
}
