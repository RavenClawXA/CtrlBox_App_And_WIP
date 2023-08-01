package com.example.ctrlbox_app;

public class Datamodels {
    private String BoxId;

    private  String GetFrom;
    private String SendTo;
    private  String TransDate;
    private String TransType ;

    public Datamodels(String BoxId, String GetFrom, String SendTo, String TransDate, String TransType ) {

        //String object =  "{\"BoxId\":\"1222\",\"Vendor\",\"VendorName\",\"Trandate\"}";
        this.BoxId = BoxId;
        this.GetFrom = GetFrom;
        this.SendTo = SendTo;
        this.TransDate = TransDate;
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

    public void setGetFrom(String getGetFrom) {
        GetFrom = getGetFrom;
    }

    public String getSendTo() {return SendTo;}

    public void setSendTo(String getSendTo) {
        SendTo = getSendTo;
    }


    public String getTransDate() {
        return TransDate;
    }

    public void setTransDate(String transDate) {
        TransDate = transDate;
    }

    public String getTransType() {
        return TransType;
    }

    public void setTransType(String transType) {
        TransType = transType;
    }
}

