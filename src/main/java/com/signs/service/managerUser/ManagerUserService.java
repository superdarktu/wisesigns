package com.signs.service.managerUser;

import com.github.pagehelper.PageHelper;
import com.signs.mapper.managerUser.ManagerUserMapper;
import com.signs.model.collector.Collector;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.managerUser.ManagerUser;
import com.signs.service.collector.CollectorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class ManagerUserService {

    @Resource
    private ManagerUserMapper mapper;

    @Resource
    private CollectorService service;


    /**
     * 管理用户登录
     */
    public ManagerUser login(String userName, String password) {
        HashMap hashMap = new HashMap();
        hashMap.put("userName", userName);
        hashMap.put("password", password);
        return mapper.login(hashMap);
    }

    /**
     * 创管理用户
     */
    @Transactional
    public boolean createUser(String account, String password, String userName, Integer userType, String tel, Float prime, Float divide, Float price, String collectorIds) {
//        账号是否重复
        List<ManagerUser> ManagerUsers = mapper.selectCode(account);
        if (ManagerUsers == null || ManagerUsers.size() > 0) return false;
        String id = UUID.randomUUID().toString().replace("-", "");
        //        修改采集器归属
        if (collectorIds != null) {
            String[] splits = collectorIds.split(",");
            Collector collector = new Collector();
            for (String collectorId : splits) {
                collector.setId(collectorId);
                collector.setPropertyId(id);
                collector.setPropertyName(userName);
                service.update(collector);
            }
        }
        ManagerUser managerUser = new ManagerUser();
        managerUser.setId(id);
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
     * 查询修改的管理用户信息
     */
    public ManagerUser gain(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 修改管理用户信息
     */
    @Transactional
    public ManagerUser save(ManagerUser managerUser, String collectorIds) {
//        卡号不重复
        ManagerUser user = mapper.selectByPrimaryKey(managerUser.getId());
        if (user == null) return null;
        String[] splits = collectorIds.split(",");
        Collector collector = new Collector();
        for (String collectorId : splits) {
            collector.setId(collectorId);
            collector.setPropertyId(managerUser.getId());
            collector.setPropertyName(managerUser.getName());
            service.update(collector);
        }
        mapper.updateByPrimaryKeySelective(managerUser);
        return managerUser;
    }


    public Boolean modifyPassword(String id, String oldPassword, String newPassword) {
        ManagerUser managerUser = mapper.selectByPrimaryKey(id);
        if (managerUser == null) return false;
        if (managerUser.getPassword().equals(oldPassword)) {
            ManagerUser user = new ManagerUser();
            user.setPassword(newPassword);
            user.setId(id);
            mapper.updateByPrimaryKeySelective(user);
            return true;
        }
        return false;


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
    public PageInfo<ManagerUser> page(PageParam page, String type, String status, String value) {
        if (page.getPageNo() != null && page.getPageSize() != null)
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        HashMap<String, String> hashMap = new HashMap<>();
        if (type != null) hashMap.put("type", type);
        if (status != null) hashMap.put("status", status);
        if (value != null) hashMap.put("value", "%" + value + "%");
        return new PageInfo(mapper.getManagerUser(hashMap));
    }

}
