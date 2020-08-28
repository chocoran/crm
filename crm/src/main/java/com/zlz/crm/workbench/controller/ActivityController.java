package com.zlz.crm.workbench.controller;

import com.zlz.crm.vo.PageListVo;
import com.zlz.crm.setting.domain.User;
import com.zlz.crm.setting.service.IUserService;
import com.zlz.crm.setting.service.UserServiceImpl;
import com.zlz.crm.util.DateTimeUtil;
import com.zlz.crm.util.PrintJson;
import com.zlz.crm.util.ServiceFactory;
import com.zlz.crm.util.UUIDUtil;
import com.zlz.crm.workbench.domain.Activity;
import com.zlz.crm.workbench.domain.ActivityRemark;
import com.zlz.crm.workbench.service.ActivityServiceImpl;
import com.zlz.crm.workbench.service.IActivityService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/activity/getUsers.do".equals(path)) {
            getUsers(request, response);
        } else if ("/workbench/activity/save.do".equals(path)) {
            save(request, response);
        } else if ("/workbench/activity/pageList.do".equals(path)) {
            pageList(request, response);
        } else if ("/workbench/activity/delete.do".equals(path)) {
            delete(request, response);
        } else if ("/workbench/activity/edit.do".equals(path)) {
            edit(request, response);
        } else if ("/workbench/activity/editSave.do".equals(path)) {
            editSaving(request, response);
        } else if ("/workbench/activity/detail.do".equals(path)) {
            showDetail(request, response);
        } else if ("/workbench/activity/getRemarks.do".equals(path)) {
            getRemarks(request, response);
        } else if ("/workbench/activity/removeRemark.do".equals(path)) {
            removeRemark(request, response);
        } else if ("/workbench/activity/editRemark.do".equals(path)) {
            editRemark(request, response);
        } else if ("/workbench/activity/saveRemark.do".equals(path)) {
            saveRemark(request, response);
        }
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        String noteContent = request.getParameter("noteContent");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String activityId = ((Activity) request.getSession().getAttribute("activity")).getId();
        String editFlag = "0";
        ActivityRemark remark = new ActivityRemark();
        remark.setEditFlag(editFlag);
        remark.setNoteContent(noteContent);
        remark.setActivityId(activityId);
        remark.setId(id);
        remark.setCreateBy(createBy);
        remark.setCreateTime(createTime);

        IActivityService service = (IActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.addRemark(remark);
        PrintJson.printJsonFlag(response, flag);
    }

    private void editRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        ActivityRemark remark = new ActivityRemark();
        remark.setId(id);
        remark.setEditBy(editBy);
        remark.setEditTime(editTime);
        remark.setNoteContent(noteContent);
        remark.setEditFlag("1");

        IActivityService service = (IActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.editRemark(remark);
        PrintJson.printJsonFlag(response, flag);
    }

    private void removeRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");

        IActivityService service = (IActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.removeRemark(id);
        PrintJson.printJsonFlag(response, flag);

    }

    private void getRemarks(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        IActivityService service = (IActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> remarks = service.getRemarks(id);
        PrintJson.printJsonObj(response, remarks);
    }

    private void showDetail(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        IActivityService service = (IActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        request.getSession().setAttribute("activity",service.getActivityDetail(id));
        try {
            response.sendRedirect(request.getContextPath() + "/workbench/activity/detail.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editSaving(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        final String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        Activity activity = new Activity();
        activity.setId(id);
        activity.setName(name);
        activity.setOwner(owner);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditBy(editBy);
        activity.setEditTime(editTime);

        IActivityService service = (IActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.editActivity(activity);
        PrintJson.printJsonFlag(response, flag);

    }

    private void edit(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        IActivityService service = (IActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity = service.getActivity(id);
        Map<String, Activity> map = new HashMap<>();
        map.put("activity", activity);
        PrintJson.printJsonObj(response, map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        String[] id = request.getParameterValues("id");
        IActivityService service = (IActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PrintJson.printJsonFlag(response, service.deleteActitity(id));
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        int pageCount = Integer.parseInt(request.getParameter("pageCount"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        int passCount = (pageCount - 1) * pageSize;
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("passCount", passCount);
        map.put("pageSize", pageSize);

        IActivityService service = (IActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PageListVo<Activity> vo = service.getPageListVo(map);
        PrintJson.printJsonObj(response, vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        String id = UUIDUtil.getUUID();
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        IActivityService service = (IActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity act = new Activity();

        act.setId(id);
        act.setName(name);
        act.setOwner(owner);
        act.setStartDate(startDate);
        act.setEndDate(endDate);
        act.setDescription(description);
        act.setCost(cost);
        act.setCreateTime(createTime);
        act.setCreateBy(createBy);

        boolean success = service.saveActivity(act);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", success);
        PrintJson.printJsonObj(response, map);
    }

    private void getUsers(HttpServletRequest request, HttpServletResponse response) {
        IUserService service = (IUserService) ServiceFactory.getService(new UserServiceImpl());
        PrintJson.printJsonObj(response, service.getUsers());
    }
}
