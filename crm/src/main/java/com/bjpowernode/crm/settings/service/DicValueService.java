package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueService {
    List<DicValue> queryAllDicValues();

    int saveDicValue(DicValue dicValue);

    int deleteDicValuesByIds(String[] id);

    DicValue queryDicValueById(String id);

    int saveEditDicValue(DicValue dicValue);

    int deleteDicValuesByTypeCodes(String[] typeCodes);

    List<DicValue> queryDicValuesByTypeCode(String typeCode);
}
