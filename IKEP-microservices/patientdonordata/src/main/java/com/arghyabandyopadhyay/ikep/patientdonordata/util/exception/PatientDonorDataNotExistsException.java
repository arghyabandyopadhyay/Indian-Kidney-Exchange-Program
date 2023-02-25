package com.arghyabandyopadhyay.ikep.patientdonordata.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND,reason = "Transaction with specified details not found")
public class PatientDonorDataNotExistsException extends Exception{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
}

