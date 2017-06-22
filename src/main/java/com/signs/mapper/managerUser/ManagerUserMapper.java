package com.signs.mapper.managerUser;

import com.signs.model.managerUser.ManagerUser;
import com.signs.util.MyMapper;

import java.util.HashMap;
import java.util.List;

public interface ManagerUserMapper extends MyMapper<ManagerUser> {

    /**
     * 饮水机获取价格
     */
    ManagerUser selectPrice(String tableNumber);

    /**
     * 账号不重复
     */
    List<ManagerUser> selectCode(String account);

    /**
     * 获取管理用户
     */
    List<ManagerUser> getManagerUser(HashMap hashMap);

}