package com.ucsmy.core.tool.interceptor.domain;

/**
 * Created by ucs_zhongtingyuan on 2017/4/7.
 */
public class PageRequest implements Pageable {
    private int page;
    private int size;

    public PageRequest() {
        this.page = 1;
        this.size = 10;
    }

    public PageRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getPageNumber() {
        return this.page;
    }

    @Override
    public int getPageSize() {
        return this.size;
    }

    @Override
    public int getOffset() {
        return (this.page - 1) * this.size;
    }
}
