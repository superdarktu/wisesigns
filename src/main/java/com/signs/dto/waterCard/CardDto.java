package com.signs.dto.waterCard;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


public class CardDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String cardNo;
    private Integer type;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
