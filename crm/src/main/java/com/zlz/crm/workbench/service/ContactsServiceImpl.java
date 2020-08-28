package com.zlz.crm.workbench.service;

import com.zlz.crm.util.SqlSessionUtil;
import com.zlz.crm.util.UUIDUtil;
import com.zlz.crm.vo.PageListVo;
import com.zlz.crm.workbench.dao.ContactsDao;
import com.zlz.crm.workbench.domain.Clue;
import com.zlz.crm.workbench.domain.Contacts;

import java.util.List;
import java.util.Map;

public class ContactsServiceImpl implements IContactsService{
    private ContactsDao dao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);

    @Override
    public int addContact(String contactId, String customerId, String createBy, String createTime, Clue clue) {
        return dao.insertContact(contactId,customerId,createBy,createTime,clue);
    }

    @Override
    public PageListVo<Contacts> showContacts(Map<String, Object> map) {
        int total = dao.selectCount(map);
        List<Contacts> pageList = dao.selectList(map);
        PageListVo<Contacts> vo = new PageListVo<>();
        vo.setPageList(pageList);
        vo.setTotal(total);
        return vo;
    }

    @Override
    public List<Contacts> getContactsByLike(String fullname) {
        return dao.selectContactsByLike(fullname);
    }
}
