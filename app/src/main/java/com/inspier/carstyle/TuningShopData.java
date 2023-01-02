package com.inspier.carstyle;

//튜닝 샵 데이터 class
public class TuningShopData {

    private int Index_num;
    private String Name;
    private String Address;
    private String Photo;
    private String PhoneNumber;

    //GET-------------------------------------------------------------------

    public int getIndex_num() {
        return Index_num;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getPhoto() {
        return Photo;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    //SET-------------------------------------------------------------------

    public void setIndex_num(int Index_num) {
        this.Index_num = Index_num;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setPhoto(String Photo) {
        this.Photo = Photo;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }
}
