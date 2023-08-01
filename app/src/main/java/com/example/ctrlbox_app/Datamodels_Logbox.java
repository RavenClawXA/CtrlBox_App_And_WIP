package com.example.ctrlbox_app;

public class Datamodels_Logbox {
    private String BoxId;
    private  String GetFrom;
    private String SendTo;
    private String TransType ;

    public Datamodels_Logbox(String BoxId, String GetForm, String SendTo, String TransType){
        this.BoxId = BoxId;
        this.GetFrom = GetForm;
        this.SendTo = SendTo;
        this.TransType = TransType;
    }

    public String getBoxId() {
        return BoxId;
    }

    public void setBoxId(String boxId) {
        BoxId = boxId;
    }

    public String getGetFrom() {
        return GetFrom;
    }

    public void setGetFrom(String getFrom) {
        GetFrom = getFrom;
    }

    public String getSendTo() {
        return SendTo;
    }

    public void setSendTo(String sendTo) {
        SendTo = sendTo;
    }

    public String getTransType() {
        return TransType;
    }

    public void setTransType(String transType) {
        TransType = transType;
    }
}
