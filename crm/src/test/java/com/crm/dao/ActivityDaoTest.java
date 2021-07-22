package com.crm.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityDaoTest extends BaseTest {
    @Autowired
    private ActivityMapper activityMapper;
    @Test
    public void testinsertActivity(){
        Activity activity = new Activity();
        activity.setId("50");
        activity.setOwner("lisi");
        activity.setName("活动");
        activity.setStartDate("2020-01-01");
        activity.setEndDate("2020-02-01");
        activity.setCost("10000");
        activity.setDescription("没什么可以说的");
        activity.setCreateTime("2020-01-02");
        activity.setCreateBy("王五");
        int num = activityMapper.insertActivity(activity);
        System.out.println(num);
    }
    @Test
    public void testSelectActivityForPageByCondition(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("beginNo",0);
        map.put("pageSize",10);
        map.put("name","3");
        map.put("owner","四");
        map.put("startDate","2021-06-14");
        map.put("endDate","2021-09-16");
        List<Activity> activityList = activityMapper.selectActivityForPageByCondition(map);
        System.out.println(activityList.size());
    }

    @Test
    public void testSelectCountOfActivityByCondition(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("name","");
        map.put("owner","");
        map.put("startDate","2021-06-14");
        map.put("endDate","2021-09-16");
        long num = activityMapper.selectCountOfActivityByCondition(map);
        System.out.println(num);
    }
    @Test
    public void testSelectActivityById(){
        Activity activity = activityMapper.selectActivityById("1");
        System.out.println(activity.toString());
    }
    @Test
    public void testUpdateActivity(){
        Activity activity = new Activity();
        activity.setId("10");
        activity.setOwner("隔壁老王");
        activity.setName("春游");
        activity.setStartDate("2021-10-1");
        activity.setEndDate("2021-10-6");
        activity.setCost("50000");
        activity.setDescription("就是春游");
        activity.setEditBy("楼上老赵");
        activity.setEditTime("2021-10-1");
        int num = activityMapper.updateActivity(activity);
        System.out.println(num);
    }
    @Test
    public void testDeleteActivityByIds(){
        String[] arrayStr = {"9","10","50"};
        int num = activityMapper.deleteActivityByIds(arrayStr);
        System.out.println(num);
    }
    @Test
    public void testSelectActivityForDetailByIds(){
        String[] arrayStr = {"1","2","3","4","5","6"};
        List<Activity> activityList = activityMapper.selectActivityForDetailByIds(arrayStr);
        System.out.println(activityList.size());
    }
    @Test
    public void testInsertActivityByList(){
        Activity activity1 = new Activity();
        activity1.setId("100");
        activity1.setOwner("周庄");
        activity1.setName("梦游");
        activity1.setStartDate("2021-01-01");
        activity1.setEndDate("2021-02-01");
        activity1.setCost("30000");
        activity1.setDescription("半夜");
        activity1.setCreateTime("2021-01-12");
        activity1.setCreateBy("打更人");
        Activity activity2 = new Activity();
        activity2.setId("200");
        activity2.setOwner("小明");
        activity2.setName("穿越");
        activity2.setStartDate("2021-01-01");
        activity2.setEndDate("2021-02-01");
        activity2.setCost("30000");
        activity2.setDescription("路边");
        activity2.setCreateTime("2021-01-12");
        activity2.setCreateBy("路人");

        List<Activity> activityList = new ArrayList<>();
        activityList.add(activity1);
        activityList.add(activity2);
        int num = activityMapper.insertActivityByList(activityList);
        System.out.println(num);
    }
    @Test
    public void testSelectActivityForDetailById(){
        Activity activity = activityMapper.selectActivityById("1");
        System.out.println(activity);
    }
    @Test
    public void testSelectAllActivityForDetail(){
        List<Activity> activityList = activityMapper.selectAllActivityForDetail();
        System.out.println(activityList.size());
    }
    @Test
    public void testSelectActivityForDetailByName(){
        List<Activity> activityList = activityMapper.selectActivityForDetailByName("穿越");
        System.out.println(activityList.size());
    }
}
