package com.signs.model.commons;


import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class PageInfo<T> implements Serializable {

    private int pageSize;

    private int pageNo;

    private long totalElements;

    private int totalPages;

    private List<T> list;

    public PageInfo(List<T> list) {
        if (list instanceof Page) {
            Page page = (Page) list;
            this.pageNo = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.list = page;
            this.totalPages = page.getPages();
            this.totalElements = page.getTotal();
        } else if (list instanceof Collection) {
            this.pageNo = 1;
            this.pageSize = list.size();
            this.list = list;
            this.totalPages = 1;
            this.totalElements = (long) list.size();
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
