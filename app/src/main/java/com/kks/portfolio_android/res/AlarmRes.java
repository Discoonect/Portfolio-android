package com.kks.portfolio_android.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kks.portfolio_android.model.Items;

import java.util.List;

public class AlarmRes {

    @SerializedName("success")
    private Boolean success;

    @SerializedName("cnt")
    private Integer cnt;

    @SerializedName("items")
    @Expose
    private List<Items> items = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }
}
