package com.zlz.crm.workbench.controller;

import com.zlz.crm.util.PrintJson;
import com.zlz.crm.util.ServiceFactory;
import com.zlz.crm.vo.PageListVo;
import com.zlz.crm.workbench.domain.Contacts;
import com.zlz.crm.workbench.service.ContactsServiceImpl;
import com.zlz.crm.workbench.service.IContactsService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ContactsController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/contacts/showContacts.do".equals(path)) {
            showContacts(request, response);
        }
    }

    private void showContacts(HttpServletRequest request, HttpServletResponse response) {
        String owner = request.getParameter("owner");
        String fullname = request.getParameter("fullname");
        String birth = request.getParameter("birth");
        String source = request.getParameter("source");
        String company = request.getParameter("company");
        int pageCount = Integer.parseInt(request.getParameter("pageCount"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        int passCount = (pageCount - 1) * pageSize;
        Map<String, Object> map = new HashMap<>();
        map.put("owner", owner);
        map.put("fullname", fullname);
        map.put("birth", birth);
        map.put("company", company);
        map.put("source", source);
        map.put("pageSize", pageSize);
        map.put("passCount", passCount);

        IContactsService service = (IContactsService) ServiceFactory.getService(new ContactsServiceImpl());
        PrintJson.printJsonObj(response,service.showContacts(map));
    }
}
