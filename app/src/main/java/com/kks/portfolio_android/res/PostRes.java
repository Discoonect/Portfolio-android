package com.kks.portfolio_android.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kks.portfolio_android.model.Items;

import java.util.List;

public class PostRes {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("items")
    @Expose
    private List<Items> items = null;
    @SerializedName("cnt")
    @Expose
    private Integer cnt;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }
}