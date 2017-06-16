package com.signs.model.commons;


public class Device {

    private String code;

    private String name;

    private Integer mode;

    private Integer num;

    private String id;

    private Object list1;

    private Object list2;

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getList1() {
        return list1;
    }

    public void setList1(Object list1) {
        this.list1 = list1;
    }

    public Object getList2() {
        return list2;
    }

    public void setList2(Object list2) {
        this.list2 = list2;
    }
}
