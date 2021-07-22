package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.mapper.DicValueMapper;
import com.bjpowernode.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DicValueServiceImpl implements DicValueService {
    @Autowired
    private DicValueMapper dicValueMapper;

    @Override
    public List<DicValue> queryAllDicValues() {
        return dicValueMapper.selectAllDicValues();
    }

    @Override
    public int saveDicValue(DicValue dicValue) {
        return dicValueMapper.insertDicValue(dicValue);
    }

    @Override
    public int deleteDicValuesByIds(String[] ids) {
        return dicValueMapper.deleteDicValuesByIds(ids);
    }

    @Override
    public DicValue queryDicValueById(String id) {
        return dicValueMapper.selectDicValueById(id);
    }

    @Override
    public int saveEditDicValue(DicValue dicValue) {
        return dicValueMapper.updateDicValue(dicValue);
    }

    @Override
    public int deleteDicValuesByTypeCodes(String[] typeCodes) {
        return dicValueMapper.deleteDicValuesByTypeCodes(typeCodes);
    }

    @Override
    public List<DicValue> queryDicValuesByTypeCode(String typeCode) {
        return dicValueMapper.selectDicValuesByTypeCode(typeCode);
    }
}
