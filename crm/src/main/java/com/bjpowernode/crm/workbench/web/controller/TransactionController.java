package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class TransactionController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private TranService tranService;
    @RequestMapping("/workbench/transaction/index.do")
    public String index(Model model){
        List<DicValue> stageList = dicValueService.queryDicValuesByTypeCode("stage");
        List<DicValue> sourceList = dicValueService.queryDicValuesByTypeCode("source");
        List<DicValue> transactionTypeList = dicValueService.queryDicValuesByTypeCode("transactionType");
        model.addAttribute("stageList",stageList);
        model.addAttribute("sourceList",sourceList);
        model.addAttribute("transactionTypeList",transactionTypeList);
        return "workbench/transaction/index";
    }
    @RequestMapping("/workbench/transaction/createTran.do")
    public String createTran(Model model){
        List<User> userList = userService.queryAll();
        List<DicValue> stageList = dicValueService.queryDicValuesByTypeCode("stage");
        List<DicValue> sourceList = dicValueService.queryDicValuesByTypeCode("source");
        List<DicValue> transactionTypeList = dicValueService.queryDicValuesByTypeCode("transactionType");
        model.addAttribute("userList",userList);
        model.addAttribute("stageList",stageList);
        model.addAttribute("sourceList",sourceList);
        model.addAttribute("transactionTypeList",transactionTypeList);
        return "workbench/transaction/save";
    }
    // 客户自动补全功能
    @RequestMapping("/workbench/transaction/queryCustomerByName.do")
    public @ResponseBody Object queryCustomerByName(String customerName){
        List<Customer> customerList = customerService.queryCustomerByName(customerName);
        return customerList;

    }
    @RequestMapping("/workbench/transaction/getPossibilityByStageValue.do")
    public @ResponseBody Object getPossibilityByStageValue(String stageValue){
        // ResourceBundle默认绑定扩展名为properties的文件
        ResourceBundle resourceBundle = ResourceBundle.getBundle("possibility");
        String possibility = resourceBundle.getString(stageValue);
        return possibility;
    }
    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    public @ResponseBody Object saveCreateTran(Tran tran, String customerName, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        tran.setId(UUIDUtils.getUUID());
        tran.setCreateBy(user.getId());
        tran.setCreateTime(DateUtils.formatDateTime(new Date()));

        Map<String, Object> map = new HashMap<>();
        map.put("tran",tran);
        map.put("customerName",customerName);
        map.put(Contants.SESSION_USER,user);

        ReturnObject returnObject = new ReturnObject();

            // 调用Service层保存数据
            int ret = tranService.saveTran(tran);
            if(ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
                returnObject.setMessage("创建失败");
            }
        return returnObject;
    }
    @RequestMapping("/workbench/transaction/queryTranForPageByCondition.do")
    public @ResponseBody Object queryTranForPageByCondition(int beginNo,int pageSize,Tran tran){
        Map<String,Object> map = new HashMap<>();
        map.put("beginNo",(beginNo-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("owner",tran.getOwner());
        map.put("name",tran.getName());
        map.put("customerName",tran.getCustomerId());
        map.put("stage",tran.getStage());
        map.put("transactionType",tran.getType());
        map.put("clueSource",tran.getSource());
        map.put("contacts",tran.getContactsId());
        List<Tran> tranList = tranService.queryTranForPageByCondition(map);
        long totalRows = tranService.queryCountOfTranByCondition(map);
        Map<String,Object> retMap = new HashMap();
        retMap.put("tranList",tranList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }
}
