package com.crm.service;

import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.service.DicTypeService;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DicTypeServiceTest extends BaseTest {
    @Autowired
    private DicTypeService dicTypeService;
    @Test
    public void testQueryAllDicTypes(){
        List<DicType> dicTypeList = dicTypeService.queryAllDicTypes();
        for(DicType dicType:dicTypeList){
            System.out.print(dicType.getCode());
            System.out.print(dicType.getName());
            System.out.print(dicType.getDescription());
            System.out.println();
        }
    }

}
