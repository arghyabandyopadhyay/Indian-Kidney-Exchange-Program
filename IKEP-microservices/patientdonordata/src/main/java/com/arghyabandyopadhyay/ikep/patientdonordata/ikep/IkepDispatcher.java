package com.arghyabandyopadhyay.ikep.patientdonordata.ikep;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class IkepDispatcher {
    public static void main(String[] args) {
        IkepExchangeCycleGenerator ikep=new IkepExchangeCycleGenerator(32,3,20,40,0.15,6,6,6,6,6,1,2,3,true);
        ikep.init();
        String fileName="Simulation/sample1000_simulation2";
        File myReader=new File("Database/"+fileName+".txt");
        Scanner sc = null;
        try {
            sc = new Scanner(myReader);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,fileName+".txt file is missing","Error", JOptionPane.ERROR_MESSAGE);
        }
        int l=0;
        while (true){
            assert sc != null;
            if (!sc.hasNextLine()) break;
            sc.nextLine();
            ++l;
        }
        PatientDonorDataIkep[] vertices =new PatientDonorDataIkep[l];
        int vIndex=0;
        try {
            sc = new Scanner(myReader);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,fileName+".txt file is missing","Error", JOptionPane.ERROR_MESSAGE);
        }
        //takes input from the file formatted in comma separated values.
        while (sc.hasNextLine()) {
            String s=sc.nextLine();
            String[] ar=s.split(",");
            vertices[vIndex]=new PatientDonorDataIkep(UUID.fromString(""),vIndex,ar[0],ar[2], Arrays.asList(ar[3],ar[4],ar[5],ar[6],ar[7],ar[8]),Integer.parseInt(ar[9]),Double.parseDouble(ar[10]),Integer.parseInt(ar[11]),ar[12],ar[13], Arrays.asList(ar[14],ar[15],ar[16],ar[17],ar[18],ar[19]),Integer.parseInt(ar[20]),Double.parseDouble(ar[21]),Integer.parseInt(ar[22]),Integer.parseInt(ar[23]),Arrays.asList(ar[24].split(":")),ar[25],ar[25]);
            vIndex++;
        }
        //Runs the IKEP algorithm for the received list of vertices.
        //prints all the details of the vertices.
//        ikep.printVertices(vertices);
        long start = System.currentTimeMillis();
        //gets all the cycles proposed by the IKEP
        List<List<PatientDonorDataIkep>> cycles=ikep.ikep(vertices);
        ikep.println("The Proposed Kidney Exchanges are as follows:");
        int i=1;
        int j=0;
        for(List<PatientDonorDataIkep> cycle:cycles){
            ikep.println("Exchange no."+i++);
            for(PatientDonorDataIkep v:cycle){
                j++;
                ikep.print("("+v.getName()+","+v.getNameOfDonor()+") ");
            }
            ikep.println();
        }
        ikep.println("No of Exchanges Proposed:"+(i-1));
        ikep.println("No of pairs matched:"+j);
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        long minutes = (elapsedTime / 1000) / 60;

        // formula for conversion for
        // milliseconds to seconds
        long seconds = (elapsedTime / 1000) % 60;
        // Print the output
        ikep.println("Time Elapsed= "+ minutes + " minutes and "
                + seconds + " seconds.");
//        ikep.println("ML:"+ikep.getMaxCycleLength()+" EF:"+ikep.getEdgeFilter()+" EP:"+(i-1)+" PM:"+j);
    }
}