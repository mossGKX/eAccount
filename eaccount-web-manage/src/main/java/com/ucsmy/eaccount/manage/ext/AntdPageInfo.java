package com.ucsmy.eaccount.manage.ext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ucsmy.core.tool.interceptor.domain.PageInfo;
import com.ucsmy.core.tool.interceptor.domain.Pageable;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class AntdPageInfo<T> extends PageInfo<T> {
    private Pagination pagination;

    public AntdPageInfo(PageInfo<T> info, Pageable pageable) {
        super();
        this.setData(info.getData());
        pagination = Pagination.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .total(info.getCount())
                .pages((int)(info.getCount() / pageable.getPageSize()) + (info.getCount() % pageable.getPageSize() == 0 ? 0 : 1))
                .build();
    }

    public AntdPageInfo() {
        super();
        pagination = Pagination.builder().build();
    }

    @Override
    @JsonProperty("rows")
    public List<T> getData() {
        return super.getData();
    }

    @Override
    @JsonIgnore
    public long getCount() {
        return super.getCount();
    }

    @Data
    @Builder
    private static class Pagination {
        private int page;
        private int size;
        private long total;
        private int pages;
    }
}
