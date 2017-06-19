package com.signs.model.commons;

/**
 * 返回结果封装类
 */
public class Result {

    private int result;

    private String msg;

    private Object data;

    private String info;

    public Result() {
        result = 1;
    }

    public void setError(String msg) {
        this.msg = msg;
        this.result = 1;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
        this.result = 0;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
