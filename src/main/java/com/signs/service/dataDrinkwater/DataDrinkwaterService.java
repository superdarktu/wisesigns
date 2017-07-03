package com.signs.service.dataDrinkwater;

import com.alibaba.fastjson.JSONObject;
import com.signs.mapper.dataDrinkwater.DataDrinkwaterMapper;
import com.signs.model.dataDrinkwater.DataDrinkwater;
import com.signs.util.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class DataDrinkwaterService {

    @Resource
    private DataDrinkwaterMapper mapper;

    public void create(JSONObject object){


        DataDrinkwater dataDrinkwater = new DataDrinkwater();
        dataDrinkwater.setCode(object.get("表编号").toString());
        dataDrinkwater.setExportPressure(Double.valueOf(object.get("回水压力").toString()));
        dataDrinkwater.setImportPressure(Double.valueOf(object.get("供水压力").toString()));
        dataDrinkwater.setExportTemp(Double.valueOf(object.get("回水温度").toString()));
        dataDrinkwater.setImportTemp(Double.valueOf(object.get("供水温度").toString()));
        dataDrinkwater.setMomentFlow(Double.valueOf(object.get("瞬时流量").toString()));
        dataDrinkwater.setTotalFlow(Double.valueOf(object.get("累计流量").toString()));
        dataDrinkwater.setStatus(Integer.valueOf(object.get("状态码").toString()));
        dataDrinkwater.setTapStatus(Integer.valueOf(object.get("阀门状态").toString()));
        dataDrinkwater.setTime(new Date());
        dataDrinkwater.setGetTime(DateUtils.strToDate(object.get("采集时间").toString()));
        mapper.insert(dataDrinkwater);
    }
}
