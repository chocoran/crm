package com.zlz.crm.workbench.dao;

import com.zlz.crm.workbench.domain.Tran;
import com.zlz.crm.workbench.domain.TranHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TranHistoryDao {

    int insertHistoryTran(@Param("id") String tranId,@Param("tran") Tran tran);

    List<TranHistory> selectAll(String tranId);
}
