package com.zlz.crm.workbench.service;

import com.zlz.crm.vo.PageListVo;
import com.zlz.crm.workbench.domain.Clue;
import com.zlz.crm.workbench.domain.Contacts;

import java.util.List;
import java.util.Map;

public interface IContactsService {
    int addContact(String contactId, String customerId, String createBy, String createTime, Clue clue);

    PageListVo<Contacts> showContacts(Map<String, Object> map);

    List<Contacts> getContactsByLike(String fullname);
}
