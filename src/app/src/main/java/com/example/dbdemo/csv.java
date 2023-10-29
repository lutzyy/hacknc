package com.example.dbdemo;

public class csv {
    private int ID;
    private String COND;
    private String MED;


    public int getID(){
        return ID;
    }
    public void setID(int ID){
        this.ID = ID;
    }

    public String getCOND(){
        return COND;
    }
    public void setCOND(String COND){
        this.COND = COND;
    }
    public String getMED(){
        return MED;
    }
    public void setMED(String MED){
        this.MED = MED;
    }

    public String toString(){
        return "csv{" +
                "ID = '" + ID + '\'' +
                ", COND = " + COND +
                ", MED = " + MED +
                '}';
    }
}