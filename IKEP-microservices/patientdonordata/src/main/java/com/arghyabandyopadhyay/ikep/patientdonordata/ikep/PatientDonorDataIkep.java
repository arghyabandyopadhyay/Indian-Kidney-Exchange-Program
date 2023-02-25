package com.arghyabandyopadhyay.ikep.patientdonordata.ikep;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

public class PatientDonorDataIkep {
    private UUID id;
    private int indexVertex;
    private String name;
    private String bloodGroup;
    private List<String> hla;
    private int age;
    private double kidneySize;
    private int pincode;
    private String nameOfDonor;
    private String bloodGroupOfDonor;
    private List<String> hlaOfDonor;
    private int ageOfDonor;
    private double kidneySizeOfDonor;
    private int pincodeOfDonor;
    private int priority;
    private List<String> societalPreference;
    private String societalDistributionOfDonor;
    private String mobileNumber;

    public PatientDonorDataIkep() {
    }

    public PatientDonorDataIkep(UUID id, int indexVertex, String name, String bloodGroup, List<String> hla, int age, double kidneySize, int pincode, String nameOfDonor, String bloodGroupOfDonor, List<String> hlaOfDonor, int ageOfDonor, double kidneySizeOfDonor, int pincodeOfDonor, int priority, List<String> societalPreference, String societalDistributionOfDonor, String mobileNumber) {
        this.id = id;
        this.indexVertex = indexVertex;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.hla = hla;
        this.age = age;
        this.kidneySize = kidneySize;
        this.pincode = pincode;
        this.nameOfDonor = nameOfDonor;
        this.bloodGroupOfDonor = bloodGroupOfDonor;
        this.hlaOfDonor = hlaOfDonor;
        this.ageOfDonor = ageOfDonor;
        this.kidneySizeOfDonor = kidneySizeOfDonor;
        this.pincodeOfDonor = pincodeOfDonor;
        this.priority = priority;
        this.societalPreference = societalPreference;
        this.societalDistributionOfDonor = societalDistributionOfDonor;
        this.mobileNumber=mobileNumber;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getIndexVertex() {
        return indexVertex;
    }

    public void setIndexVertex(int indexVertex) {
        this.indexVertex = indexVertex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public List<String> getHla() {
        return hla;
    }

    public void setHla(List<String> hla) {
        this.hla = hla;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getKidneySize() {
        return kidneySize;
    }

    public void setKidneySize(double kidneySize) {
        this.kidneySize = kidneySize;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public String getNameOfDonor() {
        return nameOfDonor;
    }

    public void setNameOfDonor(String nameOfDonor) {
        this.nameOfDonor = nameOfDonor;
    }

    public String getBloodGroupOfDonor() {
        return bloodGroupOfDonor;
    }

    public void setBloodGroupOfDonor(String bloodGroupOfDonor) {
        this.bloodGroupOfDonor = bloodGroupOfDonor;
    }

    public List<String> getHlaOfDonor() {
        return hlaOfDonor;
    }

    public void setHlaOfDonor(List<String> hlaOfDonor) {
        this.hlaOfDonor = hlaOfDonor;
    }

    public int getAgeOfDonor() {
        return ageOfDonor;
    }

    public void setAgeOfDonor(int ageOfDonor) {
        this.ageOfDonor = ageOfDonor;
    }

    public double getKidneySizeOfDonor() {
        return kidneySizeOfDonor;
    }

    public void setKidneySizeOfDonor(double kidneySizeOfDonor) {
        this.kidneySizeOfDonor = kidneySizeOfDonor;
    }

    public int getPincodeOfDonor() {
        return pincodeOfDonor;
    }

    public void setPincodeOfDonor(int pincodeOfDonor) {
        this.pincodeOfDonor = pincodeOfDonor;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<String> getSocietalPreference() {
        return societalPreference;
    }

    public void setSocietalPreference(List<String> societalPreference) {
        this.societalPreference = societalPreference;
    }

    public String getSocietalDistributionOfDonor() {
        return societalDistributionOfDonor;
    }

    public void setSocietalDistributionOfDonor(String societalDistributionOfDonor) {
        this.societalDistributionOfDonor = societalDistributionOfDonor;
    }
    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}