package com.kks.portfolio_android.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kks.portfolio_android.model.Items;

import java.util.List;

public class LikeRes {

    @SerializedName("cnt")
    int cnt;

    @SerializedName("items")
    @Expose
    private List<Items> items = null;

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
