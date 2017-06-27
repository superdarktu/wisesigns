package com.signs.mapper.user;

import com.signs.model.user.User;
import com.signs.util.MyMapper;

import java.util.HashMap;
import java.util.List;

public interface UserMapper extends MyMapper<User> {
    /**
     * 分页模糊查询用户
     *
     * @param hashMap
     * @return
     */
    List<User> pageFuzzy(HashMap hashMap);
}