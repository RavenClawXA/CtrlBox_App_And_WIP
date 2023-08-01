package com.example.ctrlbox_app;

import android.util.Base64;

import com.google.zxing.BinaryBitmap;

import java.util.List;

public class Datamodels_Wip {
    private String Job;
    private String Item;
    private String Quantity;
    private String Recipient;

    public Datamodels_Wip(String Job, String Item, String Quantity, String Recipient ){
        this.Job = Job;
        this.Item = Item;
        this.Quantity = Quantity;
        this.Recipient = Recipient;
    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getRecipient() {
        return Recipient;
    }

    public void setRecipient(String recipient) {
        Recipient = recipient;
    }


}
