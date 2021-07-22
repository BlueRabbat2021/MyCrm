package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.workbench.domain.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class Bs_TypeaheadTestController {
    // 自动补全测试
    @RequestMapping("/workbench/transaction/typeahead.do")
    public @ResponseBody
    Object typeahead(String customerName){
        // 模拟一些数据
        List<Customer> customerList = new ArrayList<>();
        Customer customer = new Customer();
        customer.setId("001");
        customer.setName("阿里巴巴");
        customerList.add(customer);

        customer = new Customer();
        customer.setId("002");
        customer.setName("动力节点");
        customerList.add(customer);

        customer = new Customer();
        customer.setId("003");
        customer.setName("字节跳动");
        customerList.add(customer);

        customer = new Customer();
        customer.setId("004");
        customer.setName("国庆节");
        customerList.add(customer);
        return customerList;
    }
}
