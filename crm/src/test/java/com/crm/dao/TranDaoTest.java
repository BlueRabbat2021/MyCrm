package com.crm.dao;

import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.mapper.TranMapper;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranDaoTest extends BaseTest {
    @Autowired
    private TranMapper tranMapper;
    @Test
    public void testSelectTranForPageByCondition(){
        Map<String,Object> map = new HashMap<>();
        map.put("owner","李");
        map.put("name","");
        map.put("customerName","");
        map.put("stage","");
        map.put("type","");
        map.put("source","");
        map.put("contactsId","");
        map.put("beginNo",0);
        map.put("pageSize",10);
        List<Tran> tranList = tranMapper.selectTranForPageByCondition(map);
        System.out.println(tranList.size());
        long num = tranMapper.selectCountOfTranByCondition(map);
        System.out.println(num);

    }
}
