package com.signs.mapper.waterCard;

import com.signs.dto.waterCard.WaterCardVO;
import com.signs.model.waterCard.WaterCard;
import com.signs.util.MyMapper;

import java.util.HashMap;
import java.util.List;

public interface WaterCardMapper extends MyMapper<WaterCard> {
    /**
     * 卡号不重复
     */
    List<WaterCard> selectCode(String code);


    /**
     * 分页查询
     */
    List<WaterCard> getWaterCard(HashMap hashMap);


    String findDefaultCardNo(String userId);

    int cancelDefault(String userId);

    int cancelCard(WaterCard waterCard);

    List<WaterCardVO> getCards(WaterCard waterCard);

    void updateMoney(HashMap map);

}