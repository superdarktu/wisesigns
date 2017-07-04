package com.signs.service.dataTotalmeter;

import com.alibaba.fastjson.JSONObject;
import com.signs.mapper.dataTotalmeter.DataTotalmeterMapper;
import com.signs.model.dataTotalmeter.DataTotalmeter;
import com.signs.util.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class DataTotalmeterService {

    @Resource
    private DataTotalmeterMapper mapper;

    public void create(JSONObject object , Integer type){

        DataTotalmeter dataTotalmeter = new DataTotalmeter();
        dataTotalmeter.setCode(object.get("总表编号").toString());
        dataTotalmeter.setExportPressure(Double.valueOf(object.get("回水压力").toString()));
        dataTotalmeter.setImportPressure(Double.valueOf(object.get("供水压力").toString()));
        dataTotalmeter.setExportTemp(Double.valueOf(object.get("回水温度").toString()));
        dataTotalmeter.setImportTemp(Double.valueOf(object.get("供水温度").toString()));
        dataTotalmeter.setMomentFlow(Double.valueOf(object.get("瞬时流量").toString()));
        dataTotalmeter.setTotalFlow(Double.valueOf(object.get("累计流量").toString()));
        dataTotalmeter.setStatus(Integer.valueOf(object.get("状态码").toString()));
        dataTotalmeter.setGetTime(DateUtils.strToDate(object.get("采集时间").toString(),"ac"));
        dataTotalmeter.setBatch(object.get("批号").toString());
        dataTotalmeter.setkNum(object.get("修正系数K").toString());
        dataTotalmeter.setStatus(Integer.valueOf(object.get("状态码").toString()));
        dataTotalmeter.setTotalHeat(Double.valueOf(object.get("累计热量").toString()));
        dataTotalmeter.setCooding(Double.valueOf(object.get("累冷量").toString()));
        dataTotalmeter.setLastBuy(object.get("最后一次购买热量或时间").toString());
        dataTotalmeter.setLeaveBuy(object.get("剩余热量或时间").toString());
        dataTotalmeter.setBuyOrder(object.get("订购序列").toString());
        dataTotalmeter.setCardMode(Integer.valueOf(object.get("卡模式").toString()));
        dataTotalmeter.setHaveCard(Integer.valueOf(object.get("是否带卡").toString()));
        dataTotalmeter.setType(type);
        dataTotalmeter.setTime(new Date());
        mapper.insert(dataTotalmeter);
    }
}