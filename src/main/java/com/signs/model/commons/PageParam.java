package com.signs.model.commons;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页参数
 */
public class PageParam {

    private Integer pageNo;

    private Integer pageSize;

    private String sortType;

    public Integer getPageNo() {
        return pageNo == null ? 1 : pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize == null ? 10000 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public Sort getSort() {
        if (sortType == null || sortType.isEmpty()) sortType = "ctime_desc";
        List<Order> result = new ArrayList<>();
        if (sortType.contains(",")) {
            String[] sortTypeList = sortType.split(",");
            result.add(getOrderByOne(sortTypeList[0]));
            for (int i = 1; i < sortTypeList.length; i++) {
                result.add(getOrderByOne(sortTypeList[i]));
            }
        } else {
            result.add(getOrderByOne(sortType));
        }
        return new Sort(result);
    }

    private Order getOrderByOne(String sortStr) {
        String[] sortTypeArray = sortStr.split("_");
        Sort.Direction type = Sort.Direction.ASC;
        if ("desc".equals(sortTypeArray[1])) type = Sort.Direction.DESC;
        return new Order(type, sortTypeArray[0]);
    }

    public PageRequest getPageRequest() {
        Sort sort = getSort();
        return new PageRequest(getPageNo() - 1, getPageSize(), sort);
    }
}
