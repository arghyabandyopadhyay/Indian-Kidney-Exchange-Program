package com.arghyabandyopadhyay.ikep.patientdonordata.controller;

import com.arghyabandyopadhyay.ikep.patientdonordata.model.PatientDonorData;
import com.arghyabandyopadhyay.ikep.patientdonordata.service.PatientDonorDataService;
import com.arghyabandyopadhyay.ikep.patientdonordata.util.exception.PatientDonorDataAlreadyExistsException;
import com.arghyabandyopadhyay.ikep.patientdonordata.util.exception.PatientDonorDataNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
//@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("api/v1/ikep/patientdonordata")
public class PatientDonorDataController {
    @Autowired
    PatientDonorDataService patientDonorDataService;

    @GetMapping()
    public ResponseEntity<?> getPatientDonorData(@RequestParam(name="identifier",required = false) UUID id, @RequestParam(name="mobile", required = false) String mobileNo) {
        PatientDonorData patientDonorData = null;
        try {
            if(id==null && mobileNo==null)throw new PatientDonorDataNotExistsException();
            patientDonorData = id!=null?patientDonorDataService.getPatientDonorData(id):patientDonorDataService.getPatientDonorDataUsingMobileNumber(mobileNo);
            return new ResponseEntity<>(patientDonorData, HttpStatus.OK);
        } catch (PatientDonorDataNotExistsException e) {
            return new ResponseEntity<>("Patient-Donor Data with specified details not found",HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllPatientDonorData() {
        List<PatientDonorData> patientDonorData = null;
        try {
            patientDonorData = patientDonorDataService.getAllPatientDonorData();
            return new ResponseEntity<>(patientDonorData, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<?> addPatientDonorData(@RequestBody() PatientDonorData patientDonorData){
        try {
            PatientDonorData newPatientDonorData = patientDonorDataService.addPatientDonorData(patientDonorData);
            return new ResponseEntity<>(newPatientDonorData,HttpStatus.CREATED);
        } catch (PatientDonorDataAlreadyExistsException  e) {
            return new ResponseEntity<String>("Patient-Donor Data already exists",HttpStatus.CONFLICT);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/csv")
    public ResponseEntity<?> addPatientDonorDataUsingCSV(@RequestBody() String patientDonorDataCsv){
        try {
            String[] ar=patientDonorDataCsv.split(",");
            PatientDonorData patientDonorData=new PatientDonorData(ar[0],ar[2], Arrays.asList(ar[3],ar[4],ar[5],ar[6],ar[7],ar[8]),Integer.parseInt(ar[9]),Double.parseDouble(ar[10]),Integer.parseInt(ar[11]),ar[12],ar[13], Arrays.asList(ar[14],ar[15],ar[16],ar[17],ar[18],ar[19]),Integer.parseInt(ar[20]),Double.parseDouble(ar[21]),Integer.parseInt(ar[22]),Integer.parseInt(ar[23]),Arrays.asList(ar[24].split(":")),ar[25],ar[1]);
            PatientDonorData newPatientDonorData = patientDonorDataService.addPatientDonorData(patientDonorData);
            return new ResponseEntity<>(newPatientDonorData,HttpStatus.CREATED);
        } catch (PatientDonorDataAlreadyExistsException  e) {
            return new ResponseEntity<String>("Patient-Donor Data already exists",HttpStatus.CONFLICT);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/csvmultiple")
    public ResponseEntity<?> addPatientDonorDataUsingCSVMultiple(@RequestBody() String patientDonorDataCsvMultiple){
        try {
            String[] patientDonorDataCsvSplit=patientDonorDataCsvMultiple.split("\n");
            List<PatientDonorData> patientDonorDataList=new ArrayList<>();
            for(String patientDonorDataCsv : patientDonorDataCsvSplit){
                String temp=patientDonorDataCsv.substring(0,patientDonorDataCsv.length()-1);
                String[] ar=temp.split(",");
                PatientDonorData patientDonorData=new PatientDonorData(ar[0],ar[2], Arrays.asList(ar[3],ar[4],ar[5],ar[6],ar[7],ar[8]),Integer.parseInt(ar[9]),Double.parseDouble(ar[10]),Integer.parseInt(ar[11]),ar[12],ar[13], Arrays.asList(ar[14],ar[15],ar[16],ar[17],ar[18],ar[19]),Integer.parseInt(ar[20]),Double.parseDouble(ar[21]),Integer.parseInt(ar[22]),Integer.parseInt(ar[23]),Arrays.asList(ar[24].split(":")),ar[25],ar[1]);
                PatientDonorData newPatientDonorData = patientDonorDataService.addPatientDonorData(patientDonorData);
                patientDonorDataList.add(newPatientDonorData);
            }
            return new ResponseEntity<>(patientDonorDataList,HttpStatus.CREATED);
        } catch (PatientDonorDataAlreadyExistsException  e) {
            return new ResponseEntity<String>("Patient-Donor Data already exists",HttpStatus.CONFLICT);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateCustomerDetails(@RequestBody() PatientDonorData patientDonorData){
        try {
            patientDonorDataService.updatePatientDonorData(patientDonorData);
            return new ResponseEntity<>("Patient-Donor Data updated successfully",HttpStatus.OK);
        } catch (PatientDonorDataNotExistsException e) {
            return new ResponseEntity<>("Patient-Donor Data with specified details not found",HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteCustomerDetails(@RequestParam(name="identifier") UUID id){
        try {
            patientDonorDataService.deletePatientDonorData(id);
            return new ResponseEntity<>("Patient-Donor Data deleted successfully",HttpStatus.OK);
        } catch (PatientDonorDataNotExistsException e) {
            return new ResponseEntity<>("Patient-Donor Data with specified details not found",HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/all")
    public ResponseEntity<?> deleteCustomerDetails(@RequestParam(name="identifier") String id){
        try {
            if(id==null||!id.equals("yeSdeleteAllikep@*1234"))throw new PatientDonorDataNotExistsException();
            patientDonorDataService.deleteAllPatientDonorData();
            return new ResponseEntity<>("Patient-Donor Data deleted successfully",HttpStatus.OK);
        } catch (PatientDonorDataNotExistsException e) {
            return new ResponseEntity<>("Patient-Donor Data with specified details not found",HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

