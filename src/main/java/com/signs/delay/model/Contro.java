package com.signs.delay.model;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Contro implements Delayed {

    private String collectorCode;
    private String memberId;
    private long submitTime;
    private String cardNo;
    private Integer type;


    public Contro(Integer type,String cardNo,String collectorCode,String memberId,long workTime){
        super();
        this.type = type;
        this.cardNo =cardNo;
        this.collectorCode = collectorCode;
        this.memberId = memberId;
        this.submitTime = TimeUnit.NANOSECONDS.convert(workTime, TimeUnit.MILLISECONDS)+System.nanoTime();
    }

    public Contro(Integer type,String memberId,long workTime){
        super();
        this.type = type;
        this.memberId = memberId;
        this.submitTime = TimeUnit.NANOSECONDS.convert(workTime, TimeUnit.MILLISECONDS)+System.nanoTime();
    }

    @Override
    public int compareTo(Delayed o) {
        // TODO Auto-generated method stub
        long result = this.getDelay(TimeUnit.NANOSECONDS)
                - o.getDelay(TimeUnit.NANOSECONDS);
        if (result < 0)
            return -1;
        else if (result > 0)
            return 1;
        return 0;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        // TODO Auto-generated method stub
        return unit.convert(this.submitTime - System.nanoTime(),  TimeUnit.NANOSECONDS);
    }

    public String getCollectorCode() {
        return collectorCode;
    }


    public String getMemberId() {
        return memberId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public Integer getType() {
        return type;
    }
}