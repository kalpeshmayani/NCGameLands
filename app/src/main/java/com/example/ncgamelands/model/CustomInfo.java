package com.example.ncgamelands.model;


import com.example.ncgamelands.util.AppUtil;

public class CustomInfo {

    int id;
    String label;
    String latitude;
    String longitude;

    public CustomInfo() {
    }


    public CustomInfo(int id, String label, String latitude, String longitude) {
        this.id = id;
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return AppUtil.getValue(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return AppUtil.getValue(label);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLatitude() {
        return AppUtil.getValue(latitude);
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return AppUtil.getValue(longitude);
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}