package com.example.james.weatherdemo.Model;

public class CityDto {

    private int ALL_ID;
    private String ALL_CITY_NAME;
    private String ALL_CITY_CODE;
    private String ALL_LAT;
    private String ALL_LONG;

    public CityDto() {
    }

    public String getALL_LAT() {
        return ALL_LAT;
    }

    public void setALL_LAT(String ALL_LAT) {
        this.ALL_LAT = ALL_LAT;
    }

    public String getALL_LONG() {
        return ALL_LONG;
    }

    public void setALL_LONG(String ALL_LONG) {
        this.ALL_LONG = ALL_LONG;
    }

    public String getALL_CITY_CODE() {
        return ALL_CITY_CODE;
    }

    public void setALL_CITY_CODE(String ALL_CITY_CODE) {
        this.ALL_CITY_CODE = ALL_CITY_CODE;
    }

    public String getALL_CITY_NAME() {
        return ALL_CITY_NAME;
    }

    public void setALL_CITY_NAME(String ALL_CITY_NAME) {
        this.ALL_CITY_NAME = ALL_CITY_NAME;
    }

    public int getALL_ID() {
        return ALL_ID;
    }

    public void setALL_ID(int ALL_ID) {
        this.ALL_ID = ALL_ID;
    }
}
