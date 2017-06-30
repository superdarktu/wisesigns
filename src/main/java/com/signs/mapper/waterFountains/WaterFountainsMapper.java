package com.signs.mapper.waterFountains;

import com.signs.model.waterFountains.WaterFountains;
import com.signs.util.MyMapper;

import java.util.HashMap;
import java.util.List;

public interface WaterFountainsMapper extends MyMapper<WaterFountains> {
    List<WaterFountains> selectCode(String code);

    List<WaterFountains> selectTableCode(String tableCode);

    Integer updateWaterFountain(HashMap hashMap);

    /**
     * 分页查询饮水机信息
     */
    List<WaterFountains> getDispenser(HashMap hashMap);

    /**
     * 获取最后一条
     */
    WaterFountains getLastDispenser();

    /**
     *  查询水卡编号是否存在
      */

    String tableCodeExist(String id);

    /**
     * 查询水卡编号是否已被使用
     */

    String tableCodeUsed(String id);


}