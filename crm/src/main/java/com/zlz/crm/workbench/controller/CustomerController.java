package com.zlz.crm.workbench.controller;

import com.zlz.crm.util.PrintJson;
import com.zlz.crm.util.ServiceFactory;
import com.zlz.crm.workbench.service.CustomerServiceImpl;
import com.zlz.crm.workbench.service.ICustomerService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomerController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/customer/showCustomer.do".equals(path)){
            showCustomer(request,response);
        }
    }

    private void showCustomer(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        int pageCount = Integer.parseInt(request.getParameter("pageCount"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        int passCount = (pageCount - 1) * pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("phone",phone);
        map.put("website",website);
        map.put("pageSize",pageSize);
        map.put("passCount",passCount);

        ICustomerService service = (ICustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        PrintJson.printJsonObj(response,service.getCustomerVo(map));

    }
}
