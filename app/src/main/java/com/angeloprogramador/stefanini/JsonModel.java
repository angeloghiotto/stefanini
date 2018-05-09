package com.angeloprogramador.stefanini;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JsonModel {
    public ArrayList<MyObject> getData() {
        return data;
    }

    public void setData(ArrayList<MyObject> data) {
        this.data = data;
    }

    @SerializedName("data")
    public ArrayList<MyObject> data;

    static public class MyObject {
        @SerializedName("id")
        public String id;
        @SerializedName("coord")
        public Coordinates coord;
        @SerializedName("country")
        public String country;
        @SerializedName("name")
        public String name;
        @SerializedName("zoom")
        public int zoom;
    }

    static public class Coordinates{
        @SerializedName("lon")
        public Double lon;
        @SerializedName("lat")
        public Double lat;
    }
}