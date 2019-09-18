package com.github.fullerzz;

import java.io.*;
import java.util.ArrayList;

public class opggManager {

    private ArrayList<OPGG> opggList = new ArrayList<>();

    public void addObject(OPGG obj){
        opggList.add(obj);
    }

    public boolean search(String dcordName){
        boolean elementExists = false;

        if (opggList.isEmpty())
            return false;

        for (int i = 0; i < opggList.size(); i++){
            if (opggList.get(i).getDiscordName().equals(dcordName))
                elementExists = true;
        }
        return elementExists;
    }

    public String getLink(String dcordName){
        String link = "";
        for (int i = 0; i < opggList.size(); i++) {
            if (opggList.get(i).getDiscordName().equals(dcordName)){
                link = opggList.get(i).getopLink();
            }
        }
        return link;
    }

    public void loadData(){
        // Code to populate arraylist using data from opgg.txt
        BufferedReader br = null;
        try {
            File file = new File("C:\\Users\\Zach\\Documents\\Code\\Java Discord Bot\\src\\opgg.txt");

            if (!file.exists())
                return;

            br = new BufferedReader(new FileReader(file));

            String currentLine, link;
            String dcord = "";
            int iterationCount = 0;

            while ((currentLine = br.readLine()) != null){
                if (iterationCount % 2 == 0){
                    dcord = currentLine;
                } else {
                    link = currentLine;
                    OPGG user = new OPGG(dcord, link);
                    addObject(user);
                }
                iterationCount++;
            }

            System.out.println("OPGG data loaded successfully.");

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
            File file = new File("C:\\Users\\Zach\\Documents\\Code\\Java Discord Bot\\src\\opgg.txt");

            if (!file.exists()){
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);

            for (int i = 0; i < opggList.size(); i++){
                bw.write(opggList.get(i).getDiscordName());
                bw.newLine();
                bw.write(opggList.get(i).getopLink());
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
