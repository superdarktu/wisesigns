package com.signs.mapper.user;

import com.signs.model.user.User;
import com.signs.util.MyMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserMapper extends MyMapper<User> {
    /**
     * 分页模糊查询用户
     *
     * @param hashMap
     * @return
     */
    List<User> pageFuzzy(HashMap hashMap);

    /**
     * 查询用户下卡号
     */
    List<User> pageCard(Map hashMap);

    void updateMoney(HashMap map);
}