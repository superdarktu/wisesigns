package com.signs.dto.waterCard;

import com.signs.model.waterCard.WaterCard;

public class WaterCardVO extends WaterCard{

    private Float purchase;

    private Float recharge;

    public Float getPurchase() {
        return purchase;
    }

    public void setPurchase(Float purchase) {
        this.purchase = purchase;
    }

    public Float getRecharge() {
        return recharge;
    }

    public void setRecharge(Float recharge) {
        this.recharge = recharge;
    }
}
