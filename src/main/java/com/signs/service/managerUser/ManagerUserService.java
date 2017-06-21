package com.signs.service.managerUser;

import com.github.pagehelper.PageHelper;
import com.signs.mapper.managerUser.ManagerUserMapper;
import com.signs.mapper.waterCard.WaterCardMapper;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.managerUser.ManagerUser;
import com.signs.model.waterCard.WaterCard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ManagerUserService {

    @Resource
    private ManagerUserMapper mapper;


    /**
     * 创水卡
     */
    @Transactional
    public boolean createCard(String account, String password, String userName, Integer userType, String tel, Float prime, Float divide, Float price) {
        //账号是否重复
        List<ManagerUser> ManagerUsers = mapper.selectCode(account);
        if (ManagerUsers == null || ManagerUsers.size() > 0) return false;
        ManagerUser managerUser = new ManagerUser();
        managerUser.setId(java.util.UUID.randomUUID().toString().replace("-", ""));
        managerUser.setCtime(new Date());
        managerUser.setAccount(account);
        managerUser.setPassword(password);
        managerUser.setUserType(userType);
        managerUser.setName(userName);
        managerUser.setPhone(tel);
        managerUser.setCostScale(prime);
        managerUser.setIvisionProportion(divide);
        managerUser.setWaterPrice(price);
        mapper.insert(managerUser);
        return true;
    }

    /**
     * 查询修改的饮水机信息
     */
    public ManagerUser gain(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 修改饮水机信息
     */
    public ManagerUser save(String id, String newAccount, String newPassword, String newUserName, Integer newUserType, String newTel, Float newPrime, Float newDivide, Float newPrice) {
//        卡号不重复
        ManagerUser user = mapper.selectByPrimaryKey(id);
        if (user == null) return null;
        ManagerUser managerUser = new ManagerUser();
        managerUser.setId(id);
        managerUser.setCtime(new Date());
        managerUser.setAccount(newAccount);
        managerUser.setPassword(newPassword);
        managerUser.setUserType(newUserType);
        managerUser.setName(newUserName);
        managerUser.setPhone(newTel);
        managerUser.setCostScale(newPrime);
        managerUser.setIvisionProportion(newDivide);
        managerUser.setWaterPrice(newPrice);
        mapper.updateByPrimaryKeySelective(managerUser);

        return managerUser;
    }

    /**
     * 根据ids删除
     */
    @Transactional
    public boolean delete(String idstr) {

        String ids[] = idstr.split(",");
        for (String id : ids) {
            ManagerUser managerUser = mapper.selectByPrimaryKey(id);
            if (managerUser == null) continue;
            mapper.delete(managerUser);
        }
        return true;
    }


    /**
     * 分页查询
     */
    public PageInfo<ManagerUser>
    page(PageParam page, String type, String status, String value) {
        if (page.getPageNo() != null && page.getPageSize() != null)
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        HashMap<String, String> hashMap = new HashMap<>();
        if (type != null) {
            hashMap.put("type", type);
        }
        if (status != null) {
            hashMap.put("status", status);
        }
        if (value != null) {
            hashMap.put("value", "%" + value + "%");
        }
        return null;
    }


}
