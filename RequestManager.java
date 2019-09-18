package com.github.fullerzz;

import java.io.*;
import java.util.ArrayList;

public class RequestManager {

    ArrayList<Request> requests;

    public RequestManager() {
        requests = new ArrayList<>();
    }

    public void addRequest(Request userRequest){
        requests.add(userRequest);
    }
    public void loadData(){
        // Code to populate arraylist using data from requests.txt
        BufferedReader br = null;
        try {
            File file = new File("C:\\Users\\Zach\\Documents\\Code\\Java Discord Bot\\src\\requests.txt");

            if (!file.exists())
                return;

            br = new BufferedReader(new FileReader(file));

            String currentLine, request;
            String requesterName = "";
            int iterationCount = 0;

            while ((currentLine = br.readLine()) != null){
                if (iterationCount % 2 == 0){
                    requesterName = currentLine;
                } else {
                    request = currentLine;
                    Request usrRequest = new Request(requesterName, request);
                    addRequest(usrRequest);
                }
                iterationCount++;
            }

            System.out.println("Requests data loaded successfully.");

        } catch (IOException e){
            e.printStackTrace();
        }
        finally{
            try {
                if (br != null)
                    br.close();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void populateFile(){
        BufferedWriter bw = null;
        try {
            File file = new File("C:\\Users\\Zach\\Documents\\Code\\Java Discord Bot\\src\\requests.txt");

            if (!file.exists()){
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);

            for (int i = 0; i < requests.size(); i++){
                bw.write(requests.get(i).requesterName);
                bw.newLine();
                bw.write(requests.get(i).request);
                bw.newLine();
            }

            System.out.println("File has been successfully written.");

        } catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                if (bw != null){
                    bw.close();
                }
            } catch (Exception ex){
                System.out.println("Error closing BufferedWriter: " + ex);
            }
        }
    }
}
