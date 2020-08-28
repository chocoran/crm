package com.zlz.crm.workbench.service;

import com.zlz.crm.vo.PageListVo;
import com.zlz.crm.workbench.domain.Clue;
import com.zlz.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface ICustomerService {
    int addCustomer(String customerId, Clue clue, String createTime, String createBy);

    PageListVo<Customer> getCustomerVo(Map<String, Object> map);

    List<String> getCustomerName(String name);
}
