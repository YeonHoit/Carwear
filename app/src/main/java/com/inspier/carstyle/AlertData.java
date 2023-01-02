package com.inspier.carstyle;

//Alert Data 타입 세팅 class
public class AlertData {

    private int Alert_Index_Num;
    private String Alert_Title;
    private String Alert_Contents;
    private String Alert_Date;
    private String Alert_Type;
    private String Alert_FromUser;
    private String Alert_ToUser;
    private int Alert_Target_Num;

    //GET-------------------------------------------------------------------

    public int getAlert_Index_Num() {
        return Alert_Index_Num;
    }

    public String getAlert_Title() {
        return Alert_Title;
    }

    public String getAlert_Contents() {
        return Alert_Contents;
    }

    public String getAlert_Date() {
        return Alert_Date;
    }

    public String getAlert_Type() {
        return Alert_Type;
    }

    public String getAlert_FromUser() {
        return Alert_FromUser;
    }

    public String getAlert_ToUser() {
        return Alert_ToUser;
    }

    public int getAlert_Target_Num() {
        return Alert_Target_Num;
    }

    //SET-------------------------------------------------------------------


    public void setAlert_Index_Num(int Alert_Index_Num) {
        this.Alert_Index_Num = Alert_Index_Num;
    }

    public void setAlert_Title(String Alert_Title) {
        this.Alert_Title = Alert_Title;
    }

    public void setAlert_Contents(String Alert_Contents) {
        this.Alert_Contents = Alert_Contents;
    }

    public void setAlert_Date(String Alert_Date) {
        this.Alert_Date = Alert_Date;
    }

    public void setAlert_Type(String Alert_Type) {
        this.Alert_Type = Alert_Type;
    }

    public void setAlert_FromUser(String Alert_FromUser) {
        this.Alert_FromUser = Alert_FromUser;
    }

    public void setAlert_ToUser(String Alert_ToUser) {
        this.Alert_ToUser = Alert_ToUser;
    }

    public void setAlert_Target_Num(int Alert_Target_Num) {
        this.Alert_Target_Num = Alert_Target_Num;
    }
}
