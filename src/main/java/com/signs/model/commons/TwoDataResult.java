package com.signs.model.commons;


public class TwoDataResult {
    private int result;
    private Object recharge;
    private Object consume;

    public TwoDataResult() {
        this.result=1;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Object getRecharge() {
        return recharge;
    }

    public void setRecharge(Object recharge) {
        this.recharge = recharge;
        this.result=0;
    }

    public Object getConsume() {
        return consume;
    }

    public void setConsume(Object consume) {
        this.consume = consume;
        this.result=0;
    }
}
