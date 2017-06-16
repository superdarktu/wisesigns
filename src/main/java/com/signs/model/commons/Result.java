package com.signs.model.commons;

/**
 * 返回结果封装类
 */
public class Result {

    private Boolean success;

    private String msg;

    private Object data;

    private String info;

    public Result() {
        success = true;
    }

    public void setError(String msg) {
        this.msg = msg;
        this.success = false;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
