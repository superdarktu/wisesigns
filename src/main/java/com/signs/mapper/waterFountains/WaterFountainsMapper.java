package com.signs.mapper.waterFountains;

import com.signs.model.waterFountains.WaterFountains;
import com.signs.util.MyMapper;

<<<<<<< HEAD
import java.util.HashMap;
import java.util.List;

public interface WaterFountainsMapper extends MyMapper<WaterFountains> {
    List<WaterFountains> selectCode(String code);


    /**
     * 分页查询饮水机信息
     *
     */
    List<WaterFountains> getDispenser(HashMap hashMap);


=======
public interface WaterFountainsMapper extends MyMapper<WaterFountains> {
>>>>>>> 10ebedd3891062fa9b739dc8fd412779decd410e
}