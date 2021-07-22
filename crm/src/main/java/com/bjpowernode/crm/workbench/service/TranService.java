package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    int saveTran(Tran tran);
    List<Tran> queryTranForPageByCondition(Map<String,Object> map);
    long queryCountOfTranByCondition(Map<String,Object> map);
}
