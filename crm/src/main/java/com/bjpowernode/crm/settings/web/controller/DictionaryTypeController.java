package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.service.DicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DictionaryTypeController {
    @Autowired
    private DicTypeService dicTypeService;

    @RequestMapping("/settings/dictionary/type/index.do")
    // 因为打开这个页面默认就会查询出内容并显示，这里用的是ModelAndView对象返回后得到的model
    public String index(Model model){
        List<DicType> dicTypeList = dicTypeService.queryAllDicTypes();
        model.addAttribute("dicTypeList",dicTypeList);
        return "settings/dictionary/type/index";
    }

    @RequestMapping("/settings/dictionary/type/toSave.do")
    public String toSave(){
        return "settings/dictionary/type/save";
    }

    @RequestMapping("/settings/dictionary/type/checkCode.do")
    public @ResponseBody Object checkCode(String code){
        DicType dicType = dicTypeService.queryDicTypeByCode(code);
        ReturnObject returnObject = new ReturnObject();
        if(dicType==null){ // 说明数据没有重复主键，可以使用
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("编码重复");
        }
        return returnObject;
    }

    @RequestMapping("/settings/dictionary/type/saveCreateDicType.do")
    public @ResponseBody Object save(DicType dicType){
        int num = dicTypeService.saveDicType(dicType);
        ReturnObject returnObject = new ReturnObject();
        if(num > 0){ // 说明保存成功
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("保存失败");
        }
        return returnObject;
    }

    @RequestMapping("/settings/dictionary/type/editDicType.do")
    // 因为前端有参数传入，所以这里接收参数
    public String editDicType(String code, Model model){
        DicType dicType = dicTypeService.queryDicTypeByCode(code);
        model.addAttribute("dicType",dicType);
        return "settings/dictionary/type/edit";
    }

    @RequestMapping("/settings/dictionary/type/saveEditDicType.do")
    public @ResponseBody Object saveEditDicType(DicType dicType){
        int num = dicTypeService.saveEdit(dicType);
        ReturnObject returnObject = new ReturnObject();
        if(num > 0){ // 说明保存成功
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("编辑失败");
        }
        return returnObject;
    }

    @RequestMapping("/settings/dictionary/type/deleteDicTypeByCodes.do")
    // 这里的参数一定要是code，虽然传递的是字符串，但是以get方法传递参数在保存时是以键值对的形式保存的
    // 字符串传过来之后会转换为一个一个的键值对的形式，而数组中的每一个元素都是名为code的键
    public @ResponseBody Object deleteDicTypeByCodes(String[] code){
        int num = dicTypeService.deleteDicTypeByCodes(code);
        ReturnObject returnObject = new ReturnObject();
        if(num > 0){ // 说明保存成功
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("删除失败");
        }
        return returnObject;
    }


}
