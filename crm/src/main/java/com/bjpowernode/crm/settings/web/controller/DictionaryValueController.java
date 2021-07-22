package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicTypeService;
import com.bjpowernode.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;
import java.util.List;

@Controller
public class DictionaryValueController {
    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private DicTypeService dicTypeService;
    @RequestMapping("/settings/dictionary/value/index.do")
    // 因为打开这个页面默认就会查询出内容并显示，这里用的是ModelAndView对象返回后得到的model
    public String indexValue(Model model){
        List<DicValue> dicValueList = dicValueService.queryAllDicValues();
        model.addAttribute("dicValueList",dicValueList);
        return "settings/dictionary/value/index";
    }

    @RequestMapping("/settings/dictionary/value/toSaveValue.do")
    public String toSaveValue(Model model){
        List<DicType> dicTypeList = dicTypeService.queryAllDicTypes();
        model.addAttribute("dicTypeList",dicTypeList);
        return "settings/dictionary/value/save";
    }
    @RequestMapping("/settings/dictionary/value/saveCreateDicValue.do")
    public @ResponseBody Object saveCreateDicValue(String dicTypeCode,String dicValue,
                                                   String text,String orderNo){
        DicValue newDicValue = new DicValue();
        newDicValue.setId(UUIDUtils.getUUID());
        newDicValue.setTypeCode(dicTypeCode);
        newDicValue.setOrderNo(orderNo);
        newDicValue.setValue(dicValue);
        newDicValue.setText(text);

        int ret = dicValueService.saveDicValue(newDicValue);
        ReturnObject returnObject = new ReturnObject();
        if(ret > 0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("新增字典值失败！");
        }
        return returnObject;
    }

    @RequestMapping("/settings/dictionary/value/editDicValue.do")
    public String editDicValue(String id, Model model){
        DicValue dicValue = dicValueService.queryDicValueById(id);
        model.addAttribute("dicValue",dicValue);
        return "settings/dictionary/value/edit";
    }
    @RequestMapping("/settings/dictionary/value/saveEditDicValue.do")
    public @ResponseBody Object saveEditDicValue(DicValue dicValue){
        int ret = dicValueService.saveEditDicValue(dicValue);
        ReturnObject returnObject = new ReturnObject();
        if(ret > 0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("字典值更新失败");
        }
        return returnObject;
    }
    @RequestMapping("/settings/dictionary/value/deleteDicValueByIds.do")
    // 当前端传来的数据是键值对的字符串时，可以使用数据接收参数
    public @ResponseBody Object deleteDicValueByIds(String[] id){
        int ret = dicValueService.deleteDicValuesByIds(id);
        ReturnObject returnObject = new ReturnObject();
        if(ret > 0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("删除失败");
        }
        return returnObject;
    }
}
