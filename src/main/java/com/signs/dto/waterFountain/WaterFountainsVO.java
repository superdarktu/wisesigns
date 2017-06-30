package com.signs.dto.waterFountain;

import com.signs.model.waterFountains.WaterFountains;

public class WaterFountainsVO extends WaterFountains{

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
