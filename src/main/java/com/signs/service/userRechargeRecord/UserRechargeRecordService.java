package com.signs.service.userRechargeRecord;

import com.github.pagehelper.PageHelper;
import com.signs.mapper.userRechargeRecord.UserRechargeRecordMapper;
import com.signs.model.commons.PageParam;
import com.signs.model.userRechargeRecord.UserRechargeRecord;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserRechargeRecordService {

    @Resource
    private UserRechargeRecordMapper mapper;

    /**
     * 分页查询
     */
    public List<UserRechargeRecord>
    page(PageParam page, String userId) {
        if (page.getPageNo() != null && page.getPageSize() != null) PageHelper.startPage(page.getPageNo(), page.getPageSize());
        return mapper.selectByUserId(userId);
    }


}
