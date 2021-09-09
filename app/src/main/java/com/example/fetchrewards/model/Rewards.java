package com.example.fetchrewards.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rewards {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("listId")
    @Expose
    private int lid;
    @SerializedName("name")
    @Expose
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
