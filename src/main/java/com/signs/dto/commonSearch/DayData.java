package com.signs.dto.commonSearch;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2017/6/29.
 */
public class DayData {
    private String hours;
    private Object data;

    public Object getHours() {
        return hours;
    }

    public void setHours(Object hours) {
        this.hours ="\"00\",\"01\",\"02\",\"03\",\"04\",\"05\",\"06\",\"07\",\"08\",\"09\",\"10\",\"11\",\"12\",\"13\",\"14\",\"15\",\"16\",\"17\",\"18\",\"19\",\"20\",\"21\",\"22\",\"23\",\"24\"";
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 25; i++) {
            if (i<10){
                System.out.print("\\\"0"+i+"\\\",");
            }
            else {
                System.out.print("\\\""+i+"\\\",");
            }
        }
    }
}
