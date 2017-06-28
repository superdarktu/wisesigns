package com.signs.service.managerUser;

import com.github.pagehelper.PageHelper;
import com.signs.mapper.collector.CollectorMapper;
import com.signs.mapper.managerUser.ManagerUserMapper;
import com.signs.mapper.managerUserCollector.ManagerUserCollectorMapper;
import com.signs.model.collector.Collector;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.manager.Manager;
import com.signs.model.managerUser.ManagerUser;
import com.signs.model.managerUserCollector.ManagerUserCollector;
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
    private ManagerUserCollectorMapper mapper1;
    @Resource
    private CollectorMapper mapper2;

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
     * 添加管理用户
     */
    @Transactional
    public boolean createUser(ManagerUser managerUser, String collectorIds) {
        //账号是否重复
        List<ManagerUser> ManagerUsers = mapper.selectCode(managerUser.getAccount());
        if (ManagerUsers == null || ManagerUsers.size() > 0) return false;
        String id = UUID.randomUUID().toString().replace("-", "");
        managerUser.setId(id);
        managerUser.setCtime(new Date());
        mapper.insert(managerUser);
        addMUC(managerUser, collectorIds);
        return true;
    }


    private void addMUC(ManagerUser managerUser, String collectorIds) {
        //修改采集器归属
        if (collectorIds != null) {
            String[] splits = collectorIds.split(",");
            Collector collector = new Collector();
            for (String collectorId : splits) {
                collector.setId(collectorId);
                collector.setPropertyId(managerUser.getId());
                collector.setPropertyName(managerUser.getName());
                service.update(collector);

                //添加关联表，修改时先删除managerId关联的关联表数据
                mapper1.deleteByManager(managerUser.getId());
                ManagerUserCollector managerUserCollector = new ManagerUserCollector();
                String sid = UUID.randomUUID().toString().replace("-", "");
                managerUserCollector.setId(sid);
                managerUserCollector.setManagerUserId(managerUser.getId());
                managerUserCollector.setManagerUserType(managerUser.getUserType());
                Collector temp = mapper2.selectByPrimaryKey(collectorId);
                managerUserCollector.setCollectorId(collectorId);
                managerUserCollector.setCollectorId(temp.getName());
                managerUserCollector.setCollectorCode(temp.getCode());
                mapper1.insert(managerUserCollector);
            }
        }
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
        addMUC(managerUser, collectorIds);
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
