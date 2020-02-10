package com.ucsmy.core.tool.interceptor.domain;

import java.util.List;

/**
 * Created by ucs_zhongtingyuan on 2017/4/7.
 */
public class PageInfo<T> {
    private long count;
    private List<T> data;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void init(Pageable pageable) {
        // do nothting yet.
    }
}
