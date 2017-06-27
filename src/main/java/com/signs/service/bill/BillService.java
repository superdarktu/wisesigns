package com.signs.service.bill;

import com.github.pagehelper.PageHelper;
import com.signs.mapper.bill.BillMapper;
import com.signs.model.bill.Bill;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;


@Service
public class BillService {
    @Resource
    private BillMapper mapper;
    public PageInfo<Bill> page(PageParam page,Integer type,String value){
        if (page.getPageNo() != null && page.getPageSize() != null) {
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        }
        HashMap hashMap=new HashMap();
        if (type != null) {
            hashMap.put("type",type);
        }
        if (value != null) {
            hashMap.put("value","%"+value+"%");
        }
        return new PageInfo(mapper.getBills(hashMap)) ;
    }

}
