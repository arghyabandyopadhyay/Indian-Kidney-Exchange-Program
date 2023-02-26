package com.arghyabandyopadhyay.ikep.ikepservice.controller;

import com.arghyabandyopadhyay.ikep.ikepservice.model.PatientDonorData;
import com.arghyabandyopadhyay.ikep.ikepservice.model.PatientDonorDataIkep;
import com.arghyabandyopadhyay.ikep.ikepservice.service.IkepExchangeCycleGeneratorService;
import com.arghyabandyopadhyay.ikep.ikepservice.util.exception.PatientDonorDataAlreadyExistsException;
import com.arghyabandyopadhyay.ikep.ikepservice.util.exception.PatientDonorDataNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
//@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("api/v1/ikep/ikepservice")
public class IkepServiceController {
    @Autowired
    IkepExchangeCycleGeneratorService ikepExchangeCycleGeneratorService;

    @GetMapping("/proposedexchanges")
    public ResponseEntity<?> getProposedExchange() {
        try {
            List<List<PatientDonorDataIkep>> exchangeCycles= ikepExchangeCycleGeneratorService.getExchangeCycles();
            return new ResponseEntity<>(exchangeCycles, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

