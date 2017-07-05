package com.signs.mapper.waterFountains;

import com.signs.dto.waterFountain.WaterFountainsVO;
import com.signs.model.waterFountains.WaterFountains;
import com.signs.util.MyMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface WaterFountainsMapper extends MyMapper<WaterFountains> {
    List<WaterFountains> selectCode(String code);

    List<WaterFountains> selectTableCode(String tableCode);

    Integer updateWaterFountain(HashMap hashMap);

    List<WaterFountains> pagePublic();

    /**
     * 分页查询饮水机信息
     */
    List<WaterFountains> getDispenser(HashMap hashMap);


    List<WaterFountainsVO> getPublic(Map<String, Object> map);

    /**
     * 获取最后一条
     */
    WaterFountains getLastDispenser();

    /**
     * 查询水卡编号是否存在
     */

    String tableCodeExist(String id);

    /**
     * 查询水卡编号是否已被使用
     */

    String tableCodeUsed(String id);


}