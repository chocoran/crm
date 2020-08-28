package com.zlz.crm.workbench.dao;

import com.zlz.crm.workbench.domain.Clue;
import com.zlz.crm.workbench.domain.Contacts;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ContactsDao {

    int insertContact(@Param("id") String contactId,@Param("customerId") String customerId,@Param("createBy") String createBy,@Param("createTime") String createTime,@Param("clue") Clue clue);

    int selectCount(Map<String, Object> map);

    List<Contacts> selectList(Map<String, Object> map);

    List<Contacts> selectContactsByLike(@Param("fullname") String fullname);
}
