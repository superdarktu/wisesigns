package com.signs.dto.Collector;

import com.signs.model.collector.Collector;

public class CollectorVO extends Collector {

    private Integer tuiguan;

    private Integer touzi;

    private Integer tablenum;

    public Integer getTablenum() {
        return tablenum;
    }

    public void setTablenum(Integer tablenum) {
        this.tablenum = tablenum;
    }

    public Integer getTuiguan() {
        return tuiguan;
    }

    public void setTuiguan(Integer tuiguan) {
        this.tuiguan = tuiguan;
    }

    public Integer getTouzi() {
        return touzi;
    }

    public void setTouzi(Integer touzi) {
        this.touzi = touzi;
    }
}
