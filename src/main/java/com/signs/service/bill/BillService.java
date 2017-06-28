package com.signs.service.bill;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.signs.mapper.bill.BillMapper;
import com.signs.model.bill.Bill;
import com.signs.model.commons.PageInfo;
import com.signs.model.commons.PageParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service
public class BillService {

    @Resource
    private BillMapper mapper;

    /**
     * 查询账单
     */
    public PageInfo<Bill> page(PageParam page, Integer type, String value) {
        if (page.getPageNo() != null && page.getPageSize() != null) {
            PageHelper.startPage(page.getPageNo(), page.getPageSize());
        }
        HashMap hashMap = new HashMap();
        if (type != null) {
            hashMap.put("type", type);
        }
        if (value != null) {
            hashMap.put("value", "%" + value + "%");
        }
        return new PageInfo(mapper.getBills(hashMap));
    }

    /**
     * 查询用户下年账单
     */
    public JSONObject pageMonth(String id, Date date) {
        HashMap hashMap = new HashMap();
        hashMap.put("id", id);
        if (date == null) {
            hashMap.put("date", new Date());
        }
        hashMap.put("date", date);
        List<Bill> bills = mapper.pageMonth(hashMap);
        JSONObject jmap = new JSONObject();
        for (Bill bill : bills) {
            jmap.put(bill.getName(), bill.getIncome());
        }
        return jmap;
    }
}
