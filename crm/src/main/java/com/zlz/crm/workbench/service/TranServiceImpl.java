package com.zlz.crm.workbench.service;

import com.zlz.crm.util.DateTimeUtil;
import com.zlz.crm.util.SqlSessionUtil;
import com.zlz.crm.util.UUIDUtil;
import com.zlz.crm.vo.PageListVo;
import com.zlz.crm.workbench.dao.CustomerDao;
import com.zlz.crm.workbench.dao.TranDao;
import com.zlz.crm.workbench.dao.TranHistoryDao;
import com.zlz.crm.workbench.dao.TranRemarkDao;
import com.zlz.crm.workbench.domain.Tran;
import com.zlz.crm.workbench.domain.TranHistory;
import com.zlz.crm.workbench.domain.TranRemark;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TranServiceImpl implements ITranService {
    private TranDao dao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private TranHistoryDao historyDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private TranRemarkDao remarkDao = SqlSessionUtil.getSqlSession().getMapper(TranRemarkDao.class);

    @Override
    public int addTran(Tran tran) {
        return dao.insertTran(tran);
    }

    @Override
    public PageListVo<Tran> showTran(Map<String, Object> map) {
        int total = dao.selectCount(map);
        List<Tran> pageList = dao.selectList(map);
        PageListVo vo = new PageListVo();
        vo.setTotal(total);
        vo.setPageList(pageList);
        return vo;
    }

    @Override
    public boolean saveTran(Map<String, Tran> map) {
        boolean flag = true;
        String customerName = (String) map.keySet().toArray()[0];
        Tran tran = map.get(customerName);
        String id = customerDao.selectCustomer(customerName);
        if (id == null) {
            String customerId = UUIDUtil.getUUID();
            tran.setCustomerId(customerId);
            String createBy = tran.getCreateBy();
            String createTime = DateTimeUtil.getSysTime();
            customerDao.insertCustomerWithIdAndName(customerId, customerName, createBy, createTime);
        } else {
            tran.setCustomerId(id);
        }
        int count = dao.insertTranAll(tran);
        String tranId = UUIDUtil.getUUID();
        count += historyDao.insertHistoryTran(tranId, tran);
        if (count != 2) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Tran getTran(String id) {
        return dao.selectTran(id);
    }

    @Override
    public List<TranRemark> getTranRemarks(String tranId) {
        return remarkDao.selectRemarks(tranId);
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = remarkDao.deleteRemark(id);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean addRemark(TranRemark remark) {
        boolean flag = true;
        int count = remarkDao.insertRemark(remark);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean editTran(Tran tran) {
        boolean flag = true;
        int count = dao.updateTran(tran);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Tran getAllTran(String id) {
        return dao.selectAllTran(id);
    }

    @Override
    public boolean saveEditTran(Map<String, Object> map) {
        boolean flag = true;
        String customerName = (String) map.get("customerName");
        Tran tran = (Tran) map.get("tran");
        boolean stageChange = (boolean) map.get("stageChange");
        String id = customerDao.selectCustomer(customerName);
        if (id == null) {
            String customerId = UUIDUtil.getUUID();
            tran.setCustomerId(customerId);
            String createBy = tran.getCreateBy();
            String createTime = DateTimeUtil.getSysTime();
            customerDao.insertCustomerWithIdAndName(customerId, customerName, createBy, createTime);
        } else {
            tran.setCustomerId(id);
        }
        int count = dao.updateTran(tran);
        if (stageChange){
            String tranId = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();
            tran.setCreateTime(createTime);
            tran.setCreateBy(tran.getEditBy());
            historyDao.insertHistoryTran(tranId, tran);
        }
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public List<TranHistory> showHistory(String tranId) {
        List<TranHistory> list = historyDao.selectAll(tranId);
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        for (TranHistory th : list){
            th.setTranId(rb.getString(th.getStage()));
        }
        return list;
    }
}
