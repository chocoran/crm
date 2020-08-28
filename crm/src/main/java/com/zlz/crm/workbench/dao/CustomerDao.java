package com.zlz.crm.workbench.dao;

import com.zlz.crm.workbench.domain.Clue;
import com.zlz.crm.workbench.domain.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CustomerDao {

    int insertCustomer(@Param("id") String customerId,@Param("createBy") String createBy,@Param("createTime") String createTime,@Param("clue") Clue clue);

    int selectTotal(Map<String, Object> map);

    List<Customer> selectList(Map<String, Object> map);

    List<String> selectNameByLike(@Param("name") String name);

    String selectCustomer(String name);

    int insertCustomerWithIdAndName(@Param("id") String customerId,@Param("name") String customerName,@Param("createBy") String createBy,@Param("createTime") String createTime);

}
