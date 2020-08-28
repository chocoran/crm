package com.zlz.crm.workbench.controller;

import com.zlz.crm.setting.domain.User;
import com.zlz.crm.setting.service.IUserService;
import com.zlz.crm.setting.service.UserServiceImpl;
import com.zlz.crm.util.DateTimeUtil;
import com.zlz.crm.util.PrintJson;
import com.zlz.crm.util.ServiceFactory;
import com.zlz.crm.util.UUIDUtil;
import com.zlz.crm.workbench.domain.Contacts;
import com.zlz.crm.workbench.domain.Tran;
import com.zlz.crm.workbench.domain.TranRemark;
import com.zlz.crm.workbench.service.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/tran/showTran.do".equals(path)) {
            showTran(request, response);
        } else if ("/workbench/tran/toDetail.do".equals(path)) {
            toDetail(request, response);
        } else if ("/workbench/tran/getActivity.do".equals(path)) {
            getActivity(request, response);
        } else if ("/workbench/tran/getContacts.do".equals(path)) {
            getContacts(request, response);
        } else if ("/workbench/tran/getUser.do".equals(path)) {
            getUser(request, response);
        } else if ("/workbench/tran/getCustomerName.do".equals(path)) {
            getCustomerName(request, response);
        } else if ("/workbench/tran/getPossibility.do".equals(path)) {
            getPossibility(request, response);
        } else if ("/workbench/tran/saveTran.do".equals(path)) {
            saveTran(request, response);
        } else if ("/workbench/tran/getRemark.do".equals(path)) {
            getRemark(request, response);
        } else if ("/workbench/tran/deleteRemark.do".equals(path)) {
            deleteRemark(request, response);
        } else if ("/workbench/tran/addRemark.do".equals(path)) {
            addRemark(request, response);
        } else if ("/workbench/tran/editTran.do".equals(path)) {
            editTran(request, response);
        } else if ("/workbench/tran/toEdit.do".equals(path)) {
            toEdit(request, response);
        } else if ("/workbench/tran/getHistory.do".equals(path)) {
            getHistory(request, response);
        }
    }

    private void getHistory(HttpServletRequest request, HttpServletResponse response) {
        String tranId = request.getParameter("tranId");
        ITranService service = (ITranService) ServiceFactory.getService(new TranServiceImpl());
        PrintJson.printJsonObj(response,service.showHistory(tranId));
    }

    private void toEdit(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ITranService service = (ITranService) ServiceFactory.getService(new TranServiceImpl());
        request.getSession().setAttribute("editTran", service.getTran(id));
        try {
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/edit.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editTran(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        ITranService service = (ITranService) ServiceFactory.getService(new TranServiceImpl());
        Tran allTran = service.getAllTran(id);

        boolean stageChange = false;
        if (!allTran.getStage().equals(stage)){
            stageChange = true;
        }

        Tran tran = new Tran();
        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setSource(source);
        tran.setType(type);
        if (activityId == null || activityId == "") {
            activityId = allTran.getActivityId();
        }
        if (contactsId == null || contactsId == "") {
            contactsId = allTran.getContactsId();
        }
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setEditBy(editBy);
        tran.setEditTime(editTime);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);
        Map<String, Object> map = new HashMap<>();
        map.put("customerName", customerName);
        map.put("tran", tran);
        map.put("stageChange", stageChange);

        service = (ITranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = service.saveEditTran(map);
        if (flag) {
            try {
                response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void addRemark(HttpServletRequest request, HttpServletResponse response) {
        String tranId = request.getParameter("tranId");
        String noteContent = request.getParameter("noteContent");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        TranRemark remark = new TranRemark();
        remark.setCreateBy(createBy);
        remark.setCreateTime(createTime);
        remark.setEditFlag(editFlag);
        remark.setId(id);
        remark.setNoteContent(noteContent);
        remark.setTranId(tranId);

        ITranService service = (ITranService) ServiceFactory.getService(new TranServiceImpl());
        PrintJson.printJsonFlag(response, service.addRemark(remark));
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ITranService service = (ITranService) ServiceFactory.getService(new TranServiceImpl());
        PrintJson.printJsonFlag(response, service.deleteRemark(id));
    }

    private void getRemark(HttpServletRequest request, HttpServletResponse response) {
        String tranId = request.getParameter("tranId");
        ITranService service = (ITranService) ServiceFactory.getService(new TranServiceImpl());
        PrintJson.printJsonObj(response, service.getTranRemarks(tranId));

    }

    private void saveTran(HttpServletRequest request, HttpServletResponse response) {
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran tran = new Tran();
        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setSource(source);
        tran.setType(type);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);
        Map<String, Tran> map = new HashMap<>();
        map.put(customerName, tran);
        ITranService service = (ITranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = service.saveTran(map);
        if (flag) {
            try {
                response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getPossibility(HttpServletRequest request, HttpServletResponse response) {
        String stage = request.getParameter("stage");
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        String possibility = rb.getString(stage);
        PrintJson.printJsonObj(response, possibility);
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("customerName");
        ICustomerService service = (ICustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        PrintJson.printJsonObj(response, service.getCustomerName(name));
    }

    private void getUser(HttpServletRequest request, HttpServletResponse response) {
        IUserService service = (IUserService) ServiceFactory.getService(new UserServiceImpl());
        PrintJson.printJsonObj(response, service.getUsers());
    }

    private void getContacts(HttpServletRequest request, HttpServletResponse response) {
        String fullname = request.getParameter("contactsLike");
        IContactsService service = (IContactsService) ServiceFactory.getService(new ContactsServiceImpl());
        PrintJson.printJsonObj(response, service.getContactsByLike(fullname));
    }

    private void getActivity(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("activityLike");
        IActivityService service = (IActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PrintJson.printJsonObj(response, service.getActivityByLike(name));
    }

    private void toDetail(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ITranService service = (ITranService) ServiceFactory.getService(new TranServiceImpl());
        request.getSession().setAttribute("tran", service.getTran(id));
        try {
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/detail.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showTran(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String customer = request.getParameter("customer");
        String contact = request.getParameter("contact");
        String source = request.getParameter("source");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        int pageCount = Integer.parseInt(request.getParameter("pageCount"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        int passCount = (pageCount - 1) * pageSize;

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("customer", customer);
        map.put("contact", contact);
        map.put("source", source);
        map.put("stage", stage);
        map.put("type", type);
        map.put("pageSize", pageSize);
        map.put("passCount", passCount);

        ITranService service = (ITranService) ServiceFactory.getService(new TranServiceImpl());
        PrintJson.printJsonObj(response, service.showTran(map));
    }
}
