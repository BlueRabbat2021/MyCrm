package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    // 保存创建的市场活动
    int saveActivity(Activity activity);
    // 查询市场活动列表按多条件condition和分页page
    List<Activity> queryActivityForPageByCondition(Map<String,Object> map);
    // 根据条件查询市场活动的总数
    long queryCountOfActivityByCondition(Map<String,Object> map);
    // 根据id查询市场活动（用于编辑）
    Activity queryActivityById(String id);
    // 保存修改的市场活动
    int saveEditActivity(Activity activity);
    // 根据ids进行批量删除
    int deleteActivityByIds(String[] ids);
    // 导出时要抓取市场活动表中所有的数据
    List<Activity> queryActivityForDetailByIds(String[] ids);
    // 导入将excel文件中多个市场活动导入到数据库的市场活动表
    int saveActivityByList(List<Activity> activityList);
    // 进入详情页面（用于详情页面）
    Activity queryActivityForDetailById(String id);
    // 在其他模块中，需要市场活动模块的支持
    List<Activity> queryAllActivityForDetail();
    // 根据市场活动名称，查询所有的市场活动
    List<Activity> queryActivityForDetailByName(String name);
    // 根据ClueId获取关联的市场活动
    List<Activity> queryActivityForDetailByClueId(String clueId);
    // 与该线索无关的市场活动
    List<Activity> queryActivityNoBoundById(Map<String,Object> map);
}
