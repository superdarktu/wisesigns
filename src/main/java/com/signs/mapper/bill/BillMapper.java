package com.signs.mapper.bill;

import com.signs.model.bill.Bill;
import com.signs.util.MyMapper;

import java.util.HashMap;
import java.util.List;

public interface BillMapper extends MyMapper<Bill> {
    List<Bill> getBills(HashMap map);

    List<Bill> pageMonth(HashMap map);
}