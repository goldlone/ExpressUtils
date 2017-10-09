package cn.goldlone.expressutils.model;

import java.util.ArrayList;

/**
 * Created by CN on 2017/8/27.
 */

public class StationExpress {
    // 请求状态
    private boolean success;
    // 查询到多少条信息
    private int totalRecords;
    // 查询到多少页信息
    private int totalPages;
    // 整个数据库有多少页信息
    private int pageSize;
    // 当前页数(从1开始)
    private int pageNum;
    // 快递信息
    private ArrayList<ExpressInfo> list;

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public ArrayList<ExpressInfo> getList() {
        return list;
    }

    public void setList(ArrayList<ExpressInfo> list) {
        this.list = list;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public String toString() {
        return "StationExpress{" +
                "success=" + success +
                ", totalRecords=" + totalRecords +
                ", totalPages=" + totalPages +
                ", pageSize=" + pageSize +
                ", pageNum=" + pageNum +
                ", list=" + list +
                '}';
    }
}
