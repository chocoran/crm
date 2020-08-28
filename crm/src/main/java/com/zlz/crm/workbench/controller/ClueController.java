package com.zlz.crm.workbench.controller;

import com.zlz.crm.setting.domain.DicValue;
import com.zlz.crm.setting.domain.User;
import com.zlz.crm.setting.service.IUserService;
import com.zlz.crm.setting.service.UserServiceImpl;
import com.zlz.crm.util.DateTimeUtil;
import com.zlz.crm.util.PrintJson;
import com.zlz.crm.util.ServiceFactory;
import com.zlz.crm.util.UUIDUtil;
import com.zlz.crm.vo.PageListVo;
import com.zlz.crm.workbench.domain.Activity;
import com.zlz.crm.workbench.domain.Clue;
import com.zlz.crm.workbench.domain.ClueActivityRelation;
import com.zlz.crm.workbench.domain.Tran;
import com.zlz.crm.workbench.service.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/clue/getUsers.do".equals(path)) {
            getUsers(request, response);
        } else if ("/workbench/clue/getSelects.do".equals(path)) {
            getSelects(request, response);
        } else if ("/workbench/clue/getClues.do".equals(path)) {
            getClues(request, response);
        } else if ("/workbench/clue/saveClue.do".equals(path)) {
            saveClue(request, response);
        } else if ("/workbench/clue/clueDetail.do".equals(path)) {
            clueDetail(request, response);
        } else if ("/workbench/clue/getActivity.do".equals(path)) {
            getActivity(request, response);
        } else if ("/workbench/clue/removeRelation.do".equals(path)) {
            removeRelation(request, response);
        } else if ("/workbench/clue/showActivity.do".equals(path)) {
            showActivity(request, response);
        } else if ("/workbench/clue/saveRelation.do".equals(path)) {
            saveRelation(request, response);
        } else if ("/workbench/clue/showActivityForRelation.do".equals(path)) {
            showActivityForRelation(request, response);
        } else if ("/workbench/clue/saveTran.do".equals(path)) {
            saveTran(request, response);
        }
    }

    private void saveTran(HttpServletRequest request, HttpServletResponse response) {
        boolean flag = Boolean.parseBoolean(request.getParameter("booleanTran"));
        String clueId = request.getParameter("clueId");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        int success = 0;

        IClueService service = (IClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue = service.getClue(clueId);

        ICustomerService customerService = (ICustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        String customerId = UUIDUtil.getUUID();
        success += customerService.addCustomer(customerId,clue,createTime,createBy);

        IContactsService contactsService = (IContactsService) ServiceFactory.getService(new ContactsServiceImpl());
        String contactId = UUIDUtil.getUUID();
        success += contactsService.addContact(contactId,customerId,createBy,createTime,clue);

        if (flag){
            Tran tran = new Tran();

            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id =UUIDUtil.getUUID();
            tran.setActivityId(activityId);
            tran.setCreateBy(createBy);
            tran.setId(id);
            tran.setCreateTime(createTime);
            tran.setStage(stage);
            tran.setName(name);
            tran.setMoney(money);
            tran.setExpectedDate(expectedDate);
            tran.setOwner(createBy);
            tran.setContactsId(contactId);
            tran.setCustomerId(customerId);
            tran.setSource(clue.getSource());

            ITranService tranService = (ITranService) ServiceFactory.getService(new TranServiceImpl());
            tranService.addTran(tran);
        }

        service = (IClueService) ServiceFactory.getService(new ClueServiceImpl());
        success += service.removeClue(clueId);

        if (success == 3){
            try {
                response.sendRedirect(request.getContextPath() + "/workbench/clue/index.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void showActivityForRelation(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");

        IClueService service = (IClueService) ServiceFactory.getService(new ClueServiceImpl());

        List<ClueActivityRelation> relation = service.getRelation(id);

        IActivityService activityService = (IActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activities =  activityService.getSameActivityForShow(name,relation);
        PrintJson.printJsonObj(response,activities);
    }

    private void saveRelation(HttpServletRequest request, HttpServletResponse response) {
        String[] aId = request.getParameterValues("aId");
        String cId = request.getParameter("cId");
        IClueService service = (IClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = service.saveRelation(aId,cId);
        PrintJson.printJsonFlag(response,flag);

    }

    private void showActivity(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");

        IClueService service = (IClueService) ServiceFactory.getService(new ClueServiceImpl());

        List<ClueActivityRelation> relation = service.getRelation(id);

        IActivityService activityService = (IActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activities =  activityService.getActivityForShow(name,relation);
        PrintJson.printJsonObj(response,activities);
    }

    private void removeRelation(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        IClueService service = (IClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = service.removeRelation(id);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getActivity(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        IClueService service = (IClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<ClueActivityRelation> relations= service.getRelation(id);
        IActivityService activityService = (IActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String, Activity> map = activityService.getRelation(relations);
        PrintJson.printJsonObj(response,map);
    }

    private void clueDetail(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        IClueService service = (IClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue = service.getClue(id);
        request.getSession().setAttribute("clue",clue);
        try {
            response.sendRedirect(request.getContextPath() + "/workbench/clue/detail.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveClue(HttpServletRequest request, HttpServletResponse response) {
        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setMphone(mphone);
        clue.setWebsite(website);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);

        IClueService service = (IClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = service.saveClue(clue);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getClues(HttpServletRequest request, HttpServletResponse response) {
        String fullname = request.getParameter("fullname");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String mphone = request.getParameter("mphone");
        String source = request.getParameter("source");
        String owner = request.getParameter("owner");
        String state = request.getParameter("state");
        int pageCount = Integer.parseInt(request.getParameter("pageCount"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        int passCount = (pageCount - 1) * pageSize;
        Map<String, Object> map = new HashMap<>();
        map.put("fullname", fullname);
        map.put("company", company);
        map.put("phone", phone);
        map.put("mphone", mphone);
        map.put("source", source);
        map.put("owner", owner);
        map.put("state", state);
        map.put("passCount", passCount);
        map.put("pageSize", pageSize);

        IClueService service = (IClueService) ServiceFactory.getService(new ClueServiceImpl());
        PageListVo<Clue> vo = service.getPageListVo(map);
        PrintJson.printJsonObj(response,vo);
    }

    private void getSelects(HttpServletRequest request, HttpServletResponse response) {
        String typeCode = request.getParameter("typeCode");
        List<DicValue> dicValues = (List<DicValue>) request.getServletContext().getAttribute(typeCode);
        PrintJson.printJsonObj(response, dicValues);
    }

    private void getUsers(HttpServletRequest request, HttpServletResponse response) {
        IUserService service = (IUserService) ServiceFactory.getService(new UserServiceImpl());
        PrintJson.printJsonObj(response, service.getUsers());
    }
}
