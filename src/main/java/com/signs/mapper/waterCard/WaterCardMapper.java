package com.signs.mapper.waterCard;

import com.signs.model.waterCard.WaterCard;
import com.signs.util.MyMapper;

<<<<<<< HEAD
import java.util.HashMap;
import java.util.List;

public interface WaterCardMapper extends MyMapper<WaterCard> {
    /**
     * 卡号不重复
     * @param code
     * @return
     */
    List<WaterCard> selectCode(String code);


    /**
     * 分页查询
     * @param hashMap
     * @return
     */
    List<WaterCard> getWaterCard(HashMap hashMap);
=======
public interface WaterCardMapper extends MyMapper<WaterCard> {
>>>>>>> 10ebedd3891062fa9b739dc8fd412779decd410e
}