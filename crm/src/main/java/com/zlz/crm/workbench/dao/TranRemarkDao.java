package com.zlz.crm.workbench.dao;

import com.zlz.crm.workbench.domain.TranRemark;

import java.util.List;

public interface TranRemarkDao {
    List<TranRemark> selectRemarks(String tranId);

    int deleteRemark(String id);

    int insertRemark(TranRemark remark);
}
