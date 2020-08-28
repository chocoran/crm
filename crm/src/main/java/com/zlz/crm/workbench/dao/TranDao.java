package com.zlz.crm.workbench.dao;

import com.zlz.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int insertTran(Tran tran);

    int insertTranAll(Tran tran);

    int selectCount(Map<String, Object> map);

    List<Tran> selectList(Map<String, Object> map);

    Tran selectTran(String id);

    int updateTran(Tran tran);

    Tran selectAllTran(String id);
}
