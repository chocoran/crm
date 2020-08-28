package com.zlz.crm.workbench.service;

import com.zlz.crm.vo.PageListVo;
import com.zlz.crm.workbench.domain.Tran;
import com.zlz.crm.workbench.domain.TranHistory;
import com.zlz.crm.workbench.domain.TranRemark;

import java.util.List;
import java.util.Map;

public interface ITranService {
    int addTran(Tran tran);

    PageListVo<Tran> showTran(Map<String, Object> map);

    boolean saveTran(Map<String, Tran> map);

    Tran getTran(String id);

    List<TranRemark> getTranRemarks(String tranId);

    boolean deleteRemark(String id);

    boolean addRemark(TranRemark remark);

    boolean editTran(Tran tran);

    Tran getAllTran(String id);

    boolean saveEditTran(Map<String, Object> map);

    List<TranHistory> showHistory(String tranId);
}
