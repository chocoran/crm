package com.zlz.crm.workbench.dao;

import com.zlz.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    Integer getRemarks(String[] id);

    Integer deleteRemarks(String[] id);

    List<ActivityRemark> getIdRemarks(String id);

    int deleteRemark(String id);

    ActivityRemark getRemark(String id);

    int updateRemark(ActivityRemark remark);

    int insertRemark(ActivityRemark remark);
}
