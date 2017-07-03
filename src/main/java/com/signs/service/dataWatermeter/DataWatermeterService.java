package com.signs.service.dataWatermeter;


import com.alibaba.fastjson.JSONObject;
import com.signs.mapper.dataWatermeter.DataWatermeterMapper;
import com.signs.model.dataWatermeter.DataWatermeter;
import com.signs.util.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class DataWatermeterService {

    @Resource
    private DataWatermeterMapper mapper;

    public void create(JSONObject object ,Integer type){

        DataWatermeter dataWatermeter = new DataWatermeter();
        dataWatermeter.setCode(object.get("水表编号").toString());
        dataWatermeter.setExportPressure(Double.valueOf(object.get("回水压力").toString()));
        dataWatermeter.setImportPressure(Double.valueOf(object.get("供水压力").toString()));
        dataWatermeter.setExportTemp(Double.valueOf(object.get("回水温度").toString()));
        dataWatermeter.setImportTemp(Double.valueOf(object.get("供水温度").toString()));
        dataWatermeter.setMomentFlow(Double.valueOf(object.get("瞬时流量").toString()));
        dataWatermeter.setTotalFlow(Double.valueOf(object.get("累计流量").toString()));
        dataWatermeter.setStatus(Integer.valueOf(object.get("状态码").toString()));
        dataWatermeter.setGetTime(DateUtils.strToDate(object.get("采集时间").toString(),"ac"));
        dataWatermeter.setBatch(object.get("批号").toString());
        dataWatermeter.setWifi(Integer.valueOf(object.get("无线是否存在").toString()));
        dataWatermeter.setSendInterval(Integer.valueOf(object.get("发送间隔").toString()));
        dataWatermeter.setSignalIntensity(Integer.valueOf(object.get("信号强度").toString()));
        dataWatermeter.setBand(object.get("频段").toString());
        dataWatermeter.setkNum(object.get("修正系数K").toString());
        dataWatermeter.setStatus(Integer.valueOf(object.get("状态码").toString()));
        dataWatermeter.setType(type);
        dataWatermeter.setTime(new Date());
        mapper.insert(dataWatermeter);
    }
}
