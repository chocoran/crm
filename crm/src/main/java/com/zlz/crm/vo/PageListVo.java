package com.zlz.crm.vo;

import java.util.List;
import java.util.Objects;

public class PageListVo<T> {
    private List<T> pageList;
    private Integer total;

    public List<T> getPageList() {
        return pageList;
    }

    public void setPageList(List<T> pageList) {
        this.pageList = pageList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageListVo<?> that = (PageListVo<?>) o;
        return Objects.equals(pageList, that.pageList) &&
                Objects.equals(total, that.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageList, total);
    }

    @Override
    public String toString() {
        return "PageListVo{" +
                "pageList=" + pageList +
                ", total=" + total +
                '}';
    }
}
