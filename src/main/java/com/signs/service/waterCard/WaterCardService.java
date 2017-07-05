package com.signs.service.waterCard;

import com.github.pagehelper.PageHelper;
import com.signs.mapper.waterCard.WaterCardMapper;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.waterCard.WaterCard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class WaterCardService {

    @Resource
    private WaterCardMapper mapper;

    /**
     * 创水卡
     */
    @Transactional
    public boolean createCard(String cardNumberi, String password, Integer type) {
        //编号是否重复
        List<WaterCard> cards = mapper.selectCode(cardNumberi);
        if (cards == null || cards.size() > 0) return false;
        WaterCard waterCard = new WaterCard();
        waterCard.setId(java.util.UUID.randomUUID().toString().replace("-", ""));
        waterCard.setCode(cardNumberi);
        waterCard.setPassword(password);
        waterCard.setPhone("暂时没有");
        waterCard.setStatus(1);
        waterCard.setType(type);
        waterCard.setCtime(new Date());
        mapper.insert(waterCard);
        return true;
    }

    /**
     * 根据ids删除
     */
    @Transactional
    public boolean delete(String idstr) {

        String ids[] = idstr.split(",");
        for (String id : ids) {
            WaterCard waterCard = mapper.selectByPrimaryKey(id);
            if (waterCard == null) continue;
            mapper.delete(waterCard);
        }
        return true;
    }

    /**
     * 水卡编号唯一
     *
     * @return
     */
    public boolean selectCode(String code) {
        List<WaterCard> waterCards = mapper.selectCode(code);
        return waterCards.size() > 0;
    }

    /**
     * 分页查询
     */
    public PageInfo page(PageParam page, String type, String status, String value) {
        if (page != null && page.getPageNo() != null && page.getPageSize() != null)
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        HashMap<String, String> hashMap = new HashMap<>();
//        type为0时表示搜索全部
        if (type != null && !type.equals("0")) hashMap.put("type", type);
        if (status != null && !status.equals("0")) hashMap.put("status", status);
        if (value != null) hashMap.put("value", "%" + value + "%");
        return new PageInfo(mapper.getWaterCard(hashMap));
    }
    /**
     * 找到默认卡
     */
    public String selectDefaultCardNo(String userId) {

        return mapper.findDefaultCardNo(userId);
    }


}
