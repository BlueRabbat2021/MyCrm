package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.mapper.TranMapper;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {
    @Autowired
    private TranMapper tranMapper;
    @Override
    public int saveTran(Tran tran) {
        return tranMapper.insertTran(tran);
    }

    @Override
    public List<Tran> queryTranForPageByCondition(Map<String, Object> map) {
        return tranMapper.selectTranForPageByCondition(map);
    }

    @Override
    public long queryCountOfTranByCondition(Map<String, Object> map) {
        return tranMapper.selectCountOfTranByCondition(map);
    }
}
