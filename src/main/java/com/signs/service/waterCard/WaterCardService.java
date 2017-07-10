package com.signs.service.waterCard;

import com.github.pagehelper.PageHelper;
import com.signs.dto.waterCard.WaterCardVO;
import com.signs.mapper.user.UserMapper;
import com.signs.mapper.userPurchaseRecord.UserPurchaseRecordMapper;
import com.signs.mapper.userRechargeRecord.UserRechargeRecordMapper;
import com.signs.mapper.waterCard.WaterCardMapper;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.user.User;
import com.signs.model.userPurchaseRecord.UserPurchaseRecord;
import com.signs.model.userRechargeRecord.UserRechargeRecord;
import com.signs.model.waterCard.WaterCard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class WaterCardService {

    @Resource
    private WaterCardMapper mapper;

    @Resource
    private UserMapper userMapper;


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
        waterCard.setPhone("");
        waterCard.setBalance(0f);
        waterCard.setDef(0);
        waterCard.setStatus(2);
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

    /**
     * 获取用户的所有卡
     * @param userId
     * @param type
     * @return
     */
    public List<WaterCardVO> getCardByUser(String userId, Integer type){

        WaterCard waterCard = new WaterCard();
        waterCard.setUserId(userId);
        waterCard.setType(type);
        return mapper.getCards(waterCard);
    }

    /**
     * 设置默认卡
     * @param userId
     * @param cardId
     * @return
     */
    @Transactional
    public boolean setDefault(String userId,String cardId){

        WaterCard waterCard = new WaterCard();
        waterCard.setUserId(userId);
        waterCard.setDef(0);
        mapper.cancelDefault(userId);

        waterCard.setDef(1);
        waterCard.setId(cardId);
        if(mapper.updateByPrimaryKeySelective(waterCard) > 0) return  true;
        return false;
    }

    /**
     * 绑定卡
     * @param userId
     * @param waterCard
     * @return
     */
    public boolean bindCard(String userId,WaterCard waterCard,String remark){

        waterCard.setStatus(2);
        WaterCard temp = mapper.selectOne(waterCard);
        if(temp == null || !StringUtil.isEmpty(temp.getUserId())){
            return  false;
        }
        User user  = userMapper.selectByPrimaryKey(userId);

        WaterCard search = new WaterCard();
        search.setUserId(userId);
        search.setDef(1);

        List<WaterCard> list = mapper.select(search);

        if( list != null && list.size() == 0) temp.setDef(1);
        temp.setUserId(userId);
        temp.setPhone(user.getPhone());
        temp.setRemark(remark);
        temp.setStatus(1);
        if(mapper.updateByPrimaryKeySelective(temp) > 0) return true;
        return false;
    }


    /**
     * 修改
     * @param waterCard
     * @return
     */
    public boolean update(WaterCard waterCard){

        return mapper.updateByPrimaryKeySelective(waterCard) > 0 ;
    }

    /**
     * 换卡
     * @param cardId
     * @param newCardCode
     * @param newPassword
     * @return
     */
    @Transactional
    public boolean changeCard(String cardId,String newCardCode,String newPassword,String remark){

        WaterCard temp  = new WaterCard();
        temp.setCode(newCardCode);
        temp.setPassword(newPassword);

        WaterCard newCard = mapper.selectOne(temp);
        Assert.notNull(newCard,"新卡号或密码错误");

        WaterCard waterCard = mapper.selectByPrimaryKey(cardId);
        newCard.setBalance(waterCard.getBalance());
        newCard.setStatus(1);
        newCard.setUserId(waterCard.getUserId());
        newCard.setPhone(waterCard.getPhone());
        newCard.setDef(waterCard.getDef());
        newCard.setRemark(remark);
        mapper.cancelCard(waterCard);
        return mapper.updateByPrimaryKeySelective(newCard) > 0 ? true : false;
    }


    public WaterCard query(String code){

        WaterCard waterCard = new WaterCard();
        waterCard.setCode(code);
        return mapper.selectOne(waterCard);
    }

}
