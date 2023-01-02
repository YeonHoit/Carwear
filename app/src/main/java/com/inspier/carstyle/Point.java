package com.inspier.carstyle;

import androidx.annotation.NonNull;

//위도와 경도 값을 가지는 Class
public class Point {

    private double latitude; //위도
    private double longitude; //경도

    //위도 경도 Setter
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    //위도 경도 Getter
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
