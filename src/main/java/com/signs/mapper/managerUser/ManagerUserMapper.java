package com.signs.mapper.managerUser;

import com.signs.model.managerUser.ManagerUser;
import com.signs.util.MyMapper;

public interface ManagerUserMapper extends MyMapper<ManagerUser> {

    /**
     * 饮水机获取价格
     * @param tableNumber
     * @return
     */
    ManagerUser selectPrice(String tableNumber);

}