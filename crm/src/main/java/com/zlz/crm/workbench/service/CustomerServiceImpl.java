package com.zlz.crm.workbench.service;

import com.zlz.crm.util.SqlSessionUtil;
import com.zlz.crm.util.UUIDUtil;
import com.zlz.crm.vo.PageListVo;
import com.zlz.crm.workbench.dao.CustomerDao;
import com.zlz.crm.workbench.domain.Clue;
import com.zlz.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public class CustomerServiceImpl implements ICustomerService{
    private CustomerDao dao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public int addCustomer(String customerId, Clue clue, String createTime, String createBy) {
        return dao.insertCustomer(customerId,createBy,createTime,clue);
    }

    @Override
    public PageListVo<Customer> getCustomerVo(Map<String, Object> map) {
        int total = dao.selectTotal(map);
        List<Customer> pageList = dao.selectList(map);
        PageListVo<Customer> vo = new PageListVo<>();
        vo.setTotal(total);
        vo.setPageList(pageList);
        return vo;
    }

    @Override
    public List<String> getCustomerName(String name) {
        return dao.selectNameByLike(name);
    }
}
