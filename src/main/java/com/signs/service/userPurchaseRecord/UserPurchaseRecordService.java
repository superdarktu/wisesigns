package com.signs.service.userPurchaseRecord;

import com.github.pagehelper.PageHelper;
import com.signs.mapper.user.UserMapper;
import com.signs.mapper.userPurchaseRecord.UserPurchaseRecordMapper;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import com.signs.model.userPurchaseRecord.UserPurchaseRecord;
import com.signs.model.waterCard.WaterCard;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class UserPurchaseRecordService {

    @Resource
    private UserPurchaseRecordMapper mapper;

    /**
     * 分页查询
     */
    public List<UserPurchaseRecord>
    page(PageParam page, String userId) {
        if (page.getPageNo() != null && page.getPageSize() != null)
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        return mapper.selectByUserId(userId);
    }


}
