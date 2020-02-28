package com.maxpetroleum.tmapp.Model;

import java.util.ArrayList;

public class GradeRate {

    String LastUpdated;
    ArrayList<Rate> list;

    public GradeRate(String lastUpdated, ArrayList<Rate> list) {
        LastUpdated = lastUpdated;
        this.list = list;
    }

    public String getLastUpdated() {
        return LastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        LastUpdated = lastUpdated;
    }

    public ArrayList<Rate> getList() {
        return list;
    }

    public void setList(ArrayList<Rate> list) {
        this.list = list;
    }
}
