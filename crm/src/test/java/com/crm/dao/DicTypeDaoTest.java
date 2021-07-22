package com.crm.dao;

import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.mapper.DicTypeMapper;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DicTypeDaoTest extends BaseTest {
    @Autowired
    private DicTypeMapper dicTypeMapper;
    @Test
    public void testQueryAllDicTypes(){
        List<DicType> dicTypeList = dicTypeMapper.selectAllDicTypes();
        for(DicType dicType:dicTypeList){
            System.out.print(dicType.getCode());
            System.out.print(dicType.getName());
            System.out.print(dicType.getDescription());
            System.out.println();
        }

    }
}
