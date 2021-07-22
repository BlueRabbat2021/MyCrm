package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRemarkService activityRemarkService;
    @RequestMapping("/workbench/activity/index.do")
    public String index(Model model){
        List<User> userList = userService.queryAll();
        model.addAttribute("userList",userList);
        return "workbench/activity/index";
    }
    @RequestMapping("/workbench/activity/queryActivityForPageByCondition.do")
//    前端传入的数据中pageNo和pageSize都是字符串类型，这里使用int类型是因为springmvc中提供了自动转换功能，可以将字符串类型的
//    数字转换为整型的数字
    public @ResponseBody Object queryActivityForPageByCondition(int pageNo,int pageSize,String name,String owner,
                                                                String startDate,String endDate){
        Map<String,Object> map = new HashMap();
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
//        得到分页查询结果
        List<Activity> activityList = activityService.queryActivityForPageByCondition(map);
//       得到总记录数
        Long totalRows = activityService.queryCountOfActivityByCondition(map);
        // 需要将结果返回给前端，因为实体类接收不了带有起始页和每页记录数
        // 所以使用map集合接收
        Map<String,Object> retMap = new HashMap();
        retMap.put("activityList",activityList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    public @ResponseBody Object saveCreateActivity(Activity activity, HttpSession session){ //
        // 首先将activity对象补全
        // create_by字段需要使用User对象的id属性
        User user = (User)session.getAttribute(Contants.SESSION_USER);

        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        activity.setCreateBy(user.getId());

        int num = activityService.saveActivity(activity);
        ReturnObject returnObject = new ReturnObject();
        if(num > 0 ){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("保存失败");
        }
        return returnObject;
    }

    // 批量导出
    @RequestMapping("/workbench/activity/exportAllActivity.do")
    public void exportAllActivity(HttpServletResponse response){
        List<Activity> activityList = activityService.queryAllActivityForDetail();
        // 在服务器创建excel用于存储数据

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");

        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        for(int i = 0; i < activityList.size();i++){
            Activity activity = activityList.get(i);
            row = sheet.createRow(i + 1);
            cell = row.createCell(0);
            cell.setCellValue(activity.getId());
            cell = row.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell = row.createCell(2);
            cell.setCellValue(activity.getName());
            cell = row.createCell(3);
            cell.setCellValue(activity.getStartDate());
            cell = row.createCell(4);
            cell.setCellValue(activity.getEndDate());
            cell = row.createCell(5);
            cell.setCellValue(activity.getCost());
            cell = row.createCell(6);
            cell.setCellValue(activity.getDescription());
        }

        // 下载
        // 浏览器默认情况下会返回html，我们需要返回Excel文件，所以需要设置返回值类型
        // octet是固定写法，stream是流
        response.setContentType("application/octet-stream;charset=UTF-8");
        // 这里设置文件名，必须这样设置，否则都是默认值
        try {
            String fileName= URLEncoder.encode("市场活动","UTF-8");
            response.addHeader("Content-Disposition","attachment;filename="+fileName+".xls");
            OutputStream out = response.getOutputStream();
            wb.write(out); // 文件输出的位置，由浏览器决定
            out.flush(); // 清空缓存
            wb.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 测试上传表单
    @RequestMapping("/workbench/activity/fileUpload.do")
    public @ResponseBody Object fileUpload(String username, MultipartFile myFile){
        // 获取文件名
        // System.out.println(myFile.getName()); // 这个获取的是对象名
        // System.out.println(myFile.getOriginalFilename()); // 这个获取的是文件名.扩展名
        String fileName = myFile.getOriginalFilename();
        File file = new File("D:\\testDir",fileName);
        ReturnObject returnObject = new ReturnObject();
        // 将文件保存到指定目录
        try {
            myFile.transferTo(file);
            returnObject.setMessage("上传成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/importActivity.do")
    // 这里的参数username只是用于验证创建的表单对象可以传递参数
    public @ResponseBody Object importActivity(MultipartFile file,String username,HttpSession session){
        User user = (User)session.getAttribute(Contants.SESSION_USER);
        Map<String,Object> retMap = new HashMap<>();
        // 上传上来的Excel表中，每一行都是一个Activity对象，所以需要Activity对象的List集合来接收
        List<Activity> activityList = new ArrayList<>();
        try {
            // 将上传的Excel文件转换成一个输入流
            InputStream is = file.getInputStream();
            HSSFWorkbook wb = new HSSFWorkbook(is);
            // 读第一张表，也可以传入表名读取
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = null;
            // 读取行，跳过第一行，直接读取数据
            for(int i = 1; i<= sheet.getLastRowNum(); i++){
                row = sheet.getRow(i);
                // 用市场活动对象接收数据
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateBy(user.getId());
                activity.setCreateTime(DateUtils.formatDateTime(new Date()));

                // 列
                for(int j = 0; j < row.getLastCellNum(); j++){
                    cell = row.getCell(j);
                    String cellValue = getCellValue(cell);
                    if(j == 0){
                        activity.setName(cellValue);
                    }else if(j == 1){
                        activity.setStartDate(cellValue);
                    }else if(j == 2){
                        activity.setEndDate(cellValue);
                    }else if(j == 3){
                        activity.setCost(cellValue);
                    }else if(j == 4){
                        activity.setDescription(cellValue);
                    }
                }
                // 将市场活动对象放到市场活动集合中
                activityList.add(activity);
            }
            int ret = activityService.saveActivityByList(activityList);
            retMap.put("code",Contants.RETURN_OBJECT_CODE_SUCCESS);
            retMap.put("count",ret);
        } catch (IOException e) {
            e.printStackTrace();
            retMap.put("code",Contants.RETURN_OBJECT_CODE_FILE);
            retMap.put("message","导入文件失败");
        }
        return retMap;
    }
    // 判断单元格类型用对应的方法来获取值，并转成String类型

    public static String getCellValue(HSSFCell cell){
        String ret = "";
        switch (cell.getCellType()){
            case HSSFCell.CELL_TYPE_STRING:
                ret = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                ret = cell.getBooleanCellValue() + "";
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                ret = cell.getNumericCellValue() + "";
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                ret = cell.getCellFormula() + "";
                break;
            default:
                ret = "";
        }
        return ret;
    }
    // 修改
    @RequestMapping("/workbench/activity/editActivity.do")
    public @ResponseBody Object editActivity(String id){
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }
    // 更新
    @RequestMapping("/workbench/activity/saveEditActivity.do")
    public @ResponseBody Object saveEditActivity(Activity activity){
        int ret = activityService.saveEditActivity(activity);
        ReturnObject returnObject = new ReturnObject();
        if(ret > 0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("更新失败");
        }
        return returnObject;
    }
    // 批量删除
    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    public @ResponseBody Object deleteActivityByIds(String[] id){
        int ret = activityService.deleteActivityByIds(id);
        ReturnObject returnObject = new ReturnObject();
        if(ret > 0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("删除失败");
        }
        return returnObject;
    }
    //选择导出
    @RequestMapping("/workbench/activity/exportActivitySelective.do")
    public void exportActivitySelective(String[] id,HttpServletResponse response){
        List<Activity> activityList = activityService.queryActivityForDetailByIds(id);
        // 在服务器创建Excel表格用于存储数据
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");

        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        response.setContentType("application/octet-stream;charset=UTF-8");

        for(int i = 0; i < activityList.size();i++){
            Activity activity = activityList.get(i);
            row = sheet.createRow(i + 1);
            cell = row.createCell(0);
            cell.setCellValue(activity.getId());
            cell = row.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell = row.createCell(2);
            cell.setCellValue(activity.getName());
            cell = row.createCell(3);
            cell.setCellValue(activity.getStartDate());
            cell = row.createCell(4);
            cell.setCellValue(activity.getEndDate());
            cell = row.createCell(5);
            cell.setCellValue(activity.getCost());
            cell = row.createCell(6);
            cell.setCellValue(activity.getDescription());
        }
        try {
            String fileName= URLEncoder.encode("市场活动","UTF-8");
            response.addHeader("Content-Disposition","attachment;filename="+fileName+".xls");
            OutputStream out = response.getOutputStream();
            wb.write(out); // 文件输出的位置，由浏览器决定
            out.flush(); // 清空缓存
            wb.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 详细跳转
    @RequestMapping("/workbench/activity/detail.do")
    public String detail(String id,Model model){
        Activity activity = activityService.queryActivityForDetailById(id);
        List<ActivityRemark> remarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
        model.addAttribute("activity",activity);
        model.addAttribute("remarkList",remarkList);
        return "workbench/activity/detail";
    }
    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    public @ResponseBody Object saveCreateActivityRemark(ActivityRemark activityRemark,HttpSession session){
        User user = (User)session.getAttribute(Contants.SESSION_USER);
        activityRemark.setId(UUIDUtils.getUUID());
        activityRemark.setEditTime(DateUtils.formatDateTime(new Date()));
        activityRemark.setCreateBy(user.getId());
        activityRemark.setEditFlag("0");
        int ret = activityRemarkService.saveActivityRemark(activityRemark);
        ReturnObject returnObject = new ReturnObject();
        if(ret > 0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(activityRemark);
            returnObject.setMessage("评论成功");
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("评论失败");
        }
        return returnObject;
    }
    // 删除备注
    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    public @ResponseBody Object deleteActivityRemarkById(String id){
        int ret = activityRemarkService.deleteActivityRemarkById(id);
        ReturnObject returnObject = new ReturnObject();
        if(ret > 0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("删除失败");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/activity/saveEditActivityRemark.do")
    public @ResponseBody Object saveEditActivityRemark(ActivityRemark activityRemark,HttpSession session){
        User user = (User)session.getAttribute(Contants.SESSION_USER);
        activityRemark.setEditFlag("1");
        activityRemark.setEditBy(user.getId());
        activityRemark.setEditTime(DateUtils.formatDateTime(new Date()));
        int ret = activityRemarkService.saveEditActivityRemark(activityRemark);
        ReturnObject returnObject = new ReturnObject();
        if(ret > 0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(activityRemark);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FILE);
            returnObject.setMessage("备注修改失败");
        }
        return returnObject;
    }
}
