package com.signs.mapper.managerUser;

import com.signs.model.managerUser.ManagerUser;
import com.signs.util.MyMapper;

import java.util.HashMap;
import java.util.List;

public interface ManagerUserMapper extends MyMapper<ManagerUser> {

    /**
     * 通过表编号获取获取水价
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

    /**
     * 管理用户登录
     */
    ManagerUser login(HashMap hashMap);


}