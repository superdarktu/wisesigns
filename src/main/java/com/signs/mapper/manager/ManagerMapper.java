package com.signs.mapper.manager;

import com.signs.model.manager.Manager;
import com.signs.util.MyMapper;

import java.util.HashMap;
import java.util.List;

public interface ManagerMapper extends MyMapper<Manager> {

    /**
     * 登录查询
     *
     * @param hm
     * @return
     */
    Manager login(HashMap<String, Object> hm);

    /**
     * 检查用户名是否存在
     *
     * @param username
     * @return
     */
    Manager isHaveUsername(String username);


    /**
     * 查询所有用户
     *
     * @param keyWord
     * @return
     */
    List<Manager> findByAdmin(String keyWord);

    /**
     * 模糊查询
     *
     * @param hm
     * @return
     */
    List<Manager> findData(HashMap<String, Object> hm);


}