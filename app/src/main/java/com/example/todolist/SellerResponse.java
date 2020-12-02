package com.example.todolist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SellerResponse {
    public SellerResponse(SellerData data, Boolean result) {
        this.data = data;
        this.result = result;
    }

    @SerializedName("data")
    private SellerData data;

    public SellerData getData() {
        return data;
    }

    public void setData(SellerData data) {
        this.data = data;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    @SerializedName("result")
    private Boolean result;
}

class SellerData {
    public SellerData(List<Seller> items) {
        this.items = items;
    }

    public List<Seller> getItems() {
        return items;
    }

    public void setItems(List<Seller> items) {
        this.items = items;
    }

    @SerializedName("items")
    private List<Seller> items;
}