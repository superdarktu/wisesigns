package com.signs.dto.watermeter;

import com.signs.util.ExcelNull;

public class WatermeterExcel {

    private String code;

    @ExcelNull
    private String totalCode;

    private String collectorCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTotalCode() {
        return totalCode;
    }

    public void setTotalCode(String totalCode) {
        this.totalCode = totalCode;
    }

    public String getCollectorCode() {
        return collectorCode;
    }

    public void setCollectorCode(String collectorCode) {
        this.collectorCode = collectorCode;
    }
}
