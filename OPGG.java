package com.github.fullerzz;

import java.io.*;
import java.util.*;

public class OPGG {

    private final String discordName;
    private final String opLink;
    private ArrayList<OPGG> opggList = new ArrayList<>();

    public OPGG(String dName, String sName){
        discordName = dName;
        if (!sName.contains("op.gg"))
            opLink = configureLink(sName);
        else
            opLink = sName;
    }

    public String getDiscordName(){
        return discordName;
    }

    public String getopLink(){
        return opLink;
    }
    private String configureLink(String sName){
        String result = "http://na.op.gg/summoner/userName=";
        if (sName.contains(" "))
            result += sName.replaceAll(" ", "+");
        else
            result += sName;

        System.out.println("End of link: " + result);
        return result;
    }

}
