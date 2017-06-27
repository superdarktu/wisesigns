package com.signs.dto.commonSearch;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class SearchVO {

    private String id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date1;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date2;

    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
