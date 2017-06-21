package com.signs.service.user;

import com.github.pagehelper.PageHelper;
import com.signs.mapper.user.UserMapper;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.waterCard.WaterCard;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
public class UserService {

    @Resource
    private UserMapper mapper;

    /**
     * 分页查询
     */
    public PageInfo<WaterCard>
    page(PageParam page, String status, String value) {
        if (page.getPageNo() != null && page.getPageSize() != null)
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        HashMap<String, String> hashMap = new HashMap<>();
        if (status != null) {
            hashMap.put("status", status);
        }
        if (value != null) {
            hashMap.put("value", "%" + value + "%");
        }
        return new PageInfo(mapper.pageFuzzy(hashMap));
    }


}
