package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicTypeService;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;
    @RequestMapping("/workbench/clue/index.do")
    public String index(Model model){
        List<User> userList = userService.queryAll();
        List<DicValue> appellationList = dicValueService.queryDicValuesByTypeCode("appellation");
        List<DicValue> sourceList = dicValueService.queryDicValuesByTypeCode("source");
        List<DicValue> clueStateList = dicValueService.queryDicValuesByTypeCode("clueState");
        model.addAttribute("userList",userList);
        model.addAttribute("appellationList",appellationList);
        model.addAttribute("sourceList",sourceList);
        model.addAttribute("clueStateList",clueStateList);
        return "workbench/clue/index";
    }
    @RequestMapping("/workbench/clue/saveCreateClue.do")
    public @ResponseBody Object saveCreateClue(Clue clue, HttpSession session){
        User user = (User)session.getAttribute(Contants.SESSION_USER);
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DateUtils.formatDateTime(new Date()));
        ReturnObject returnObject = new ReturnObject();
        int ret = clueService.saveCreateClue(clue);
        if(ret > 0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("????????????");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String clueId,Model model){
        // ????????????
        Clue clue = clueService.queryClueForDetailById(clueId);
        // ????????????
        // ??????????????????????????????????????????
        List<Activity> activityList = activityService.queryActivityForDetailByClueId(clueId);
        // ??????model??????????????????????????????????????????????????????????????????????????????????????????????????????
        model.addAttribute("clue",clue);
        model.addAttribute("activityList",activityList);
        return "workbench/clue/detail";
    }
    @RequestMapping("/workbench/clue/searchActivity.do")
    public @ResponseBody Object searchActivity(String activityName,String clueId){
        HashMap map=new HashMap();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        List<Activity> activityList=activityService.queryActivityNoBoundById(map);
        return activityList;
    }
    @RequestMapping("/workbench/clue/saveBundActivity.do")
    // ?????????????????????????????????activityId????????????clueId?????????activityId???????????????
    public @ResponseBody Object saveBundActivity(String clueId, String[] activityId){
        ClueActivityRelation relation = null;
        List<ClueActivityRelation> relationList = new ArrayList<>();
        for(String ai:activityId){
            relation = new ClueActivityRelation();
            relation.setId(UUIDUtils.getUUID());
            relation.setClueId(clueId);
            relation.setActivityId(ai);
            relationList.add(relation);
        }
        ReturnObject returnObject = new ReturnObject();
        // ??????activityId??????????????????????????????????????????????????????????????????
        List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);
        // ??????????????????????????????????????????????????????????????????
        int ret = clueActivityRelationService.saveClueActivityRelationByList(relationList);

        if(ret > 0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(activityList);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("????????????");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/clue/saveUnbundActivity.do")
    // ??????????????????????????????????????????????????????????????????????????????????????????
    public @ResponseBody Object saveUnbundActivity(String clueId,String activityId){
        ClueActivityRelation relation = new ClueActivityRelation();
        relation.setClueId(clueId);
        relation.setActivityId(activityId);
        ReturnObject returnObject = new ReturnObject();
        int ret = clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);
        if(ret > 0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("??????????????????");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/clue/convertClue.do")
    public String convertClue(String id,Model model){
        // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        Clue clue = clueService.queryClueForDetailById(id);
        List<DicValue> stageList = dicValueService.queryDicValuesByTypeCode("stage");
        model.addAttribute("clue",clue);
        model.addAttribute("stageList",stageList);
        return "workbench/clue/convert";
    }
    @RequestMapping("/workbench/clue/saveConvertClue.do")
    public @ResponseBody Object saveConvertClue(String clueId,String isCreateTran,String amountOfMoney, String tradeName,
                                                String expectedClosingDate,String stage,String activityId,HttpSession session){
        Map<String,Object> map = new HashMap<>();
        map.put("clueId",clueId);
        map.put("isCreateTran",isCreateTran);
        map.put("amountOfMoney",amountOfMoney);
        map.put("tradeName",tradeName);
        map.put("expectedClosingDate",expectedClosingDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("sessionUser",session.getAttribute(Contants.SESSION_USER));
        ReturnObject returnObject = new ReturnObject();
        try{
            clueService.saveConvert(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setMessage("????????????");
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("????????????");
        }
        return returnObject;
    }
}
